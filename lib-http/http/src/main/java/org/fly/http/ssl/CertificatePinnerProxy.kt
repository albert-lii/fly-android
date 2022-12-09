package org.fly.http.ssl

import java.security.cert.Certificate

/**
 * 域名证书校验代理，用于管理信任证书并提供CertificatePinner
 */
interface CertificatePinnerProxy {

    /**
     * 是否对域名证书进行检验
     */
    fun shouldCheck(url: String): Boolean

    /**
     * 检验域名证书
     */
    fun check(hostname: String, url: String, peerCertificate: Certificate): Boolean

    /**
     * 信任新证书
     *
     * @param certificateDigest 数字域名证书
     */
    fun trustCert(certificateDigest: CertificateDigest)

    /**
     * 不信任证书
     *
     * @param certificateDigest 数字域名证书
     */
    fun distrustCert(certificateDigest: CertificateDigest)
}