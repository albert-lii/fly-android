package org.fly.http.exception

/**
 * 证书校验异常
 */
open class CertVerifyException : NetworkException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}

/**
 * 证书替换异常
 *
 * 抛出异常，提示对等方可以替换证书，一般直接替换为当前请求中的域名证书。
 * 此种状况通常发生在证书的public key与客户端的key不匹配，但是证书的CN在已知的CN列表中。
 * 出现这种状况，可能是服务端证书更新了，但是客户端没有最新的证书的public key信息
 */
class CertReplacedException : CertVerifyException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}

/**
 * 证书过期异常
 */
class CertOutDateException : CertVerifyException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}

/**
 * 未知证书异常
 *
 * X509 证书未知,
 * 证书 Public Key 未知
 * 证书 CN 未知
 */
class CertUnknownException : CertVerifyException {
    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
}