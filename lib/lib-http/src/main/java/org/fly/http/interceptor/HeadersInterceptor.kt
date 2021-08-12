package org.fly.http.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/8 10:57 下午
 * @description: 请求头部拦截器
 * @since: 1.0.0
 */
class HeadersInterceptor(private val headers: Map<String, String>): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder  = chain.request().newBuilder()

        // Request customization: add request headers
        headers.forEach {
            requestBuilder.addHeader(it.key, it.value)
        }
        return chain.proceed(requestBuilder.build())
    }
}