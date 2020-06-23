package org.we.fly.utils.http

import androidx.annotation.StringRes
import org.we.fly.R

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 5:20 PM
 * @description: 服务器返回异常数据模型
 * @since: 1.0.0
 */
class HttpServerException() {
    var errorCode: Int = -1
    var message: Int = R.string.fly_http_error_unknow

    constructor(errorCode: Int, @StringRes message: Int) : this() {
        this.errorCode = errorCode
        this.message = message
    }
}