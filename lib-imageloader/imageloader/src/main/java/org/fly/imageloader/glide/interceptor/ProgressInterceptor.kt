package org.fly.imageloader.glide.interceptor

import android.net.Uri
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.ConcurrentHashMap

/**
 * 图片下载进度拦截器
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/1 11:18 上午
 * @since: 1.0.0
 */
object ProgressInterceptor : Interceptor {

    private val listenerMap = ConcurrentHashMap<String, ProgressResponseBody.ProgressResponseListener>()

    private val progressMap = ConcurrentHashMap<String, Long>()

    private val listener by lazy {
        object : ProgressResponseBody.ProgressResponseListener {

            override fun update(url: HttpUrl, bytesRead: Long, contentLength: Long) {
                val key = Uri.decode(url.toString())
                val listener = listenerMap[key] ?: return
                if (contentLength <= bytesRead) {
                    unregisterListener(key)
                }
                val lastProgress = progressMap[key]
                if (lastProgress == null || lastProgress != bytesRead) {
                    progressMap[key] = bytesRead
                    listener.update(url, bytesRead, contentLength)
                }
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // fix NullPointerException: bio == null,
        // when okhttp 5.0 release version published,then we upgrade it to solve it (https://square.github.io/okhttp/changelog/)
        return try {
            val request: Request = chain.request()
            val response = chain.proceed(request)
            val originBody = response.body
            response.newBuilder()
                .body(originBody?.let { ProgressResponseBody(request.url, it, listener) })
                .build()
        } catch (e: Exception) {
            throw  IOException(e.message)
        }
    }

    fun registerListener(url: String, listener: ProgressResponseBody.ProgressResponseListener) {
        listenerMap[url] = listener
    }

    fun unregisterListener(url: String) {
        listenerMap.remove(url)
        progressMap.remove(url)
    }
}