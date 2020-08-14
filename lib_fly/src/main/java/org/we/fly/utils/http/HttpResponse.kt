package org.we.fly.utils.http

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/14 7:06 PM
 * @description: 网络请求返回基础模型
 * @since: 1.0.0
 */
data class HttpResponse<T>(
    val code: Int,
    val msg: String,
    val data: T?
)
