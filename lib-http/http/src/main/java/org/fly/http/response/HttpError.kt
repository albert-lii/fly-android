package org.fly.http.response

/**
 * 解析后的网络请求异常信息类
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 5:31 PM
 * @since: 1.0.0
 */
open class HttpError(
    var httpCode: Int? = null,
    var errorCode: Int,
    var errorMsg: String,
    var cause: Throwable? = null
) {

    companion object {
        /**
         * 异常错误码
         */
        const val UNKNOW_ERROR = -1 // 未知错误
        const val CANCEL_REQUEST = -2 // 取消请求
        const val UNKNOW_HOST = -3 // 未知域名，连接失败
        const val TIMEOUT = -4 // 请求超时
        const val BAD_NETWORK = -5 // 网络异常
        const val PARSE_ERROR = -6 // 数据解析错误
        const val CERTIFICATE_UNVERIFIED = -7 // SSL证书校验失败
    }
}
