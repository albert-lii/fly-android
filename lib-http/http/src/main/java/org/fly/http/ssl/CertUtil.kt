package org.fly.http.ssl

import java.io.ByteArrayInputStream
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.util.*

object CertUtil {
    // 当前的证书
    private var currentCerts = Collections.synchronizedList(mutableListOf<CertificateDigest>())

    // 报警记录，
    private var alertRecord: Hashtable<String, Pair<Int, Long>> = Hashtable()

    /**
     * 解析证书信息
     */
    @Throws(CertificateException::class)
    fun decodeCert(certBytes: ByteArray): X509Certificate? {
        val factory = CertificateFactory.getInstance("X.509") // 设置证书类型，X.509是一种格式标准
        return factory.generateCertificate(ByteArrayInputStream(certBytes)) as? X509Certificate
    }

    /**
     * 获取证书的公用名称
     */
    fun getCN(x509: X509Certificate): String? =
        DistinguishedNameParser(x509.subjectX500Principal).findMostSpecific("cn")

    /**
     * 获取信任的证书
     */
    fun getTrustCerts(): List<CertificateDigest> {
        if (currentCerts.isEmpty()) {
            val certs = CertificateStore.getInstance().getAll()
            currentCerts.clear()
            currentCerts.addAll(certs)
        }
        return currentCerts
    }

    /**
     * 更新证书缓存
     */
    fun updateCertCache() {
        val certs = CertificateStore.getInstance().getAll()
        currentCerts.clear()
        currentCerts.addAll(certs)
    }

    /**
     * 更新报警记录，对于证书校验不通过的情况，理应向后端报警，报警接口应在白名单中
     */
    fun updateAlertRecord(domain: String): Boolean {
        if (alertRecord.containsKey(domain)) {
            val pair: Pair<Int, Long>? = alertRecord[domain]
            if (pair == null) {
                alertRecord[domain] = Pair<Int, Long>(1, System.currentTimeMillis())
            } else {
                val oldCount = pair.first
                val lastTime = pair.second
                // 每个域名超过10次报警或者报警间隔小于5s则不更新报警记录
                if (oldCount >= 10 || ((System.currentTimeMillis() - lastTime) < 5 * 1000L)) {
                    return false
                } else {
                    alertRecord[domain] = Pair<Int, Long>(1, System.currentTimeMillis())
                }
            }
        } else {
            alertRecord[domain] = Pair<Int, Long>(1, System.currentTimeMillis())
        }
        return true
    }
}