package org.we.fly.utils.http

import androidx.annotation.StringRes
import org.we.fly.R

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 5:31 PM
 * @description: 网络请求错误类
 * @since: 1.0.0
 */
enum class HttpError(val code: Int, @StringRes val message: Int) {
    UNKNOWN_ERROR(-1, R.string.fly_http_error_unknow),
    CONNECT_ERROR(-2, R.string.fly_http_error_connect),
    CONNECT_TIMEOUT(-3, R.string.fly_http_error_connect_timeout),
    BAD_NETWORK(-4, R.string.fly_http_error_bad_network),
    PARSE_ERROR(-5, R.string.fly_http_error_parse),
}