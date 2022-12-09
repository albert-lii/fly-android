package org.fly.http.ssl

import okhttp3.CertificatePinner
import org.fly.http.exception.CertOutDateException
import org.fly.http.exception.CertReplacedException
import org.fly.http.exception.CertUnknownException
import java.security.cert.Certificate
import java.security.cert.CertificateExpiredException
import java.security.cert.CertificateNotYetValidException
import java.security.cert.X509Certificate
import java.util.*


/**
 * 域名证书校验的具体实现
 */
class CertificatePinnerProxyImpl(
    private val originTrustCertificates: List<CertificateDigest>, // 信任证书
    private val officer: CertificateOfficer // 证书校验管理
) : CertificatePinnerProxy {
    private val trustCertificates = mutableListOf<CertificateDigest>()
        .apply { addAll(originTrustCertificates) }
    private var certificatePinner: CertificatePinner =
        createCertificatePinner(originTrustCertificates)

    override fun shouldCheck(url: String): Boolean {
        if (!officer.pinningEnabled() || officer.whiteApis().find { url.contains(it) } != null) {
            return false
        }
        return true
    }

    override fun check(hostname: String, url: String, peerCertificate: Certificate): Boolean {
        // 解析当前请求的证书信息
        val x509 = CertUtil.decodeCert(peerCertificate.encoded)
        // 获取信任证书列表
        val certs = CertUtil.getTrustCerts()
        // 获取证书的public key的hash值
        val hash = CertificatePinner.pin(peerCertificate)
        // 先进行一次证书强校验，
        val isPassed = certs.find { it.hash == hash } != null
        if (!isPassed && x509 != null) {
            /**
             * 如果强校验失败，则进行弱校验。弱校验的目的是防止出现因为服务端证书信息更新后未及时同步到客户端，出现
             * 大规模的用户无法使用应用的状况；但是弱校验理论上是不能无限制使用，正常安全逻辑中会规定弱校验的使用次数，
             * 例如在应用生命周期内弱校验使用一次过后，下次必须得过强校验才能请求通过，不再走弱校验。无限使用弱校验，
             * 会使强校验的存在目的失效。（PS：强弱具体使用，还是看当前的业务需求）
             *
             * 此处弱校验不使用OkHttp自带的校验：
             *     certificatePinner.check(hostname, listOf(peerCertificate))
             * 因为此校验在hostname与证书域名匹配失败后，会直接宣布失败，不会匹配CN
             * 此处使用自定义弱校验
             */
            return checkCertWeakly(hostname, x509, certs, peerCertificate)
        }
        x509?.let {
            // 强校验通过，最后查看证书是否在有效期内
            val isValidDate = checkCertDate(it)
            if (!isValidDate) {
                val certOutDateException = CertOutDateException(
                    "Certificate is out date!, cert date from ${x509.notBefore} to ${x509.notAfter}"
                )
                if (!officer.peek(hostname, peerCertificate, certOutDateException)) {
                    throw certOutDateException
                }
            }
        }
        return true
    }

    override fun trustCert(certificateDigest: CertificateDigest) {
        if (!trustCertificates.contains(certificateDigest)) {
            trustCertificates.add(certificateDigest)
            certificatePinner = createCertificatePinner(trustCertificates)
        }
    }

    override fun distrustCert(certificateDigest: CertificateDigest) {
        if (trustCertificates.contains(certificateDigest)) {
            trustCertificates.remove(certificateDigest)
            certificatePinner = createCertificatePinner(trustCertificates)
        }
    }

    private fun createCertificatePinner(trustCertificates: List<CertificateDigest>): CertificatePinner {
        return CertificatePinner.Builder()
            .apply {
                trustCertificates
                    .forEach {
                        try {
                            add(it.hostname, it.hash)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            }
            .build()
    }

    /**
     * 基于证书域名与CN信息进行弱校验
     */
    private fun checkCertWeakly(
        hostname: String,
        x509: X509Certificate,
        certs: List<CertificateDigest>,
        peerCertificate: Certificate
    ): Boolean {
        // 校验证书公用域名是否有效
        val isValidCN = checkCertHostname(x509, certs)
        if (!isValidCN) {
            // CN不在已知的信任证书列表中，抛出异常
            val certUnknownException =
                CertUnknownException("${x509.subjectX500Principal.name} is unknown")
            if (!officer.peek(hostname, peerCertificate, certUnknownException)) {
                throw certUnknownException
            }
        } else {
            // CN是有效的，随后检查证书是否在有效期内
            val isValidDate = checkCertDate(x509)
            if (isValidDate) {
                // 证书在有效期内，提示更新证书
                val certReplacedException = CertReplacedException(
                    "Peer replaced certificate, new certificate is ${x509.subjectX500Principal.name}"
                )
                if (!officer.peek(hostname, peerCertificate, certReplacedException)) {
                    throw certReplacedException
                }
            } else {
                // 证书已经过期
                val certOutDateException = CertOutDateException(
                    "Certificate is out date!, cert date from ${x509.notBefore} to ${x509.notAfter}"
                )
                if (!officer.peek(hostname, peerCertificate, certOutDateException)) {
                    throw certOutDateException
                }
            }
        }
        return true
    }

    /**
     * 校验证书公用名称是否有效
     */
    private fun checkCertHostname(
        x509: X509Certificate,
        trustCertificates: List<CertificateDigest>
    ): Boolean {
        val host = CertUtil.getCN(x509) // 请求携带证书的公用名称，即证书的CN
        if (CertificateStore.getInstance().isUpdateFromRemote()) {
            // 本次应用启动后，已经从远程更新了证书信息，我们直接对这些最新证书信息进行强校验
            return when {
                host.isNullOrBlank() -> false
                else -> trustCertificates.find { it.certCN == host } != null
            }
        } else {
            // 本次应用启动后，还没有从远程更新证书信息，我们只能对之前的证书本地缓存信息进行校验
            // 对缓存信息校验，会放宽要求，防止远程证书信息有特殊变更，客户端未能及时收到，导致校验失败，影响大规模线上用户的使用
            return when {
                host.isNullOrBlank() -> false
                host.startsWith(SSLConstants.HOST_WILDCARD) -> {
                    val hostEnd = host.substring(SSLConstants.HOST_WILDCARD.length)
                    // 请求携带证书的CN以通配符'*.'开头，检查信任的证书域名是否包含或等于CN信息中的顶级域名
                    val isExisted = trustCertificates.find {
                        it.hostname.endsWith(".$hostEnd") || it.hostname == hostEnd
                    } != null
                    if (!isExisted) {
                        // 如果域名与CN比对失败，则使用CN与CN比对
                        trustCertificates.find { it.certCN == host } != null
                    } else {
                        true
                    }
                }
                else -> {
                    val isExisted = (trustCertificates.find {
                        when {
                            // 信任的证书域名以通配符'*.'开头，检查请求携带证书的CN包含信任的证书的顶级域名或相等
                            it.hostname.startsWith(SSLConstants.HOST_WILDCARD) -> {
                                val hostnameEnd = it.hostname.substring(SSLConstants.HOST_WILDCARD.length)
                                host.endsWith(".${hostnameEnd}") || host == hostnameEnd
                            }
                            it.hostname.startsWith(SSLConstants.HOST_WILDCARD2) -> {
                                val hostnameEnd = it.hostname.substring(SSLConstants.HOST_WILDCARD2.length)
                                host.endsWith(".${hostnameEnd}") || host == hostnameEnd
                            }
                            // 检查证书域名与请求携带证书的CN相同
                            it.hostname == host -> true
                            // 检查CN是否是信任证书的顶级域名
                            it.hostname.length > host.length && it.hostname.endsWith(host) -> {
                                val arr = it.hostname.split(host)
                                return arr[0].endsWith(".")
                            }
                            // 检查信任证书的域名是否是CN的顶级域名
                            it.hostname.length < host.length && host.endsWith(it.hostname) -> {
                                val arr = host.split(it.hostname)
                                return arr[0].endsWith(".")
                            }
                            else -> false
                        }
                    } != null)
                    if (!isExisted) {
                        // 如果域名与CN比对失败，则使用CN与CN比对
                        trustCertificates.find { it.certCN == host } != null
                    } else {
                        true
                    }
                }
            }
        }
    }

    /**
     * 校验证书是否在有效期
     */
    private fun checkCertDate(x509: X509Certificate): Boolean =
        // 日期是当前设备的日期时间
        try {
            x509.checkValidity(Date())
            true
        } catch (e: CertificateExpiredException) {
            false
        } catch (e: CertificateNotYetValidException) {
            false
        }
}