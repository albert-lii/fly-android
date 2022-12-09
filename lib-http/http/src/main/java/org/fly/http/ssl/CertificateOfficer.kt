package org.fly.http.ssl

import org.fly.http.exception.CertVerifyException
import java.security.cert.Certificate

/**
 * 证书校验管理
 */
interface CertificateOfficer {

    /**
     * 是否开启证书校验
     */
    fun pinningEnabled(): Boolean

    /**
     * 设置接口白名单，白名单中的接口不用进行证书校验
     */
    fun whiteApis(): List<String>

    /**
     * 证书校验并返回结果
     *
     * @return true-校验通过; false-校验不通过;
     */
    fun peek(
        hostname: String,
        certificate: Certificate,
        exception: CertVerifyException
    ): Boolean
}

class DefaultCertificateOfficer : CertificateOfficer {

    override fun pinningEnabled(): Boolean = false

    override fun whiteApis(): List<String> = emptyList()

    override fun peek(
        hostname: String,
        certificate: Certificate,
        exception: CertVerifyException
    ): Boolean {
        return true
    }
}
