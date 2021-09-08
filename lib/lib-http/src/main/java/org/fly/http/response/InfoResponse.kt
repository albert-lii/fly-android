package org.fly.http.response

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/14 7:06 PM
 * @description: 成功的网络请求返回的数据模型
 * @since: 1.0.0
 */
data class InfoResponse<T>(
    val code: Int,
    val message: String,
    val data: T?
) {
    /**
     * 判断本次请求返回的响应是否为成功响应，此方法一定要实现
     */
    fun isSuccessful(): Boolean {
        return code == 0
    }
}

