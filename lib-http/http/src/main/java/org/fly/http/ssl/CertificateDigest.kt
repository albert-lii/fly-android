package org.fly.http.ssl

/**
 * 证书摘要信息
 */
data class CertificateDigest(
    var hostname: String, // 域名
    var hash: String, // 证书的public key
    var certCN: String // 证书公用名称，为证书中CN字段的值
)