package org.fly.imageloader.glide.interceptor

import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/1 11:19 上午
 * @description: 下载进度响应
 * @since: 1.0.0
 */
class ProgressResponseBody(
    val url: HttpUrl,
    val responseBody: ResponseBody,
    val progressResponseListener: ProgressResponseListener
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead: Long = super.read(sink, byteCount)
                val fullLength = responseBody.contentLength()
                if (bytesRead == -1L) { // this source is exhausted
                    totalBytesRead = fullLength
                } else {
                    totalBytesRead += bytesRead
                }
                progressResponseListener.update(url, totalBytesRead, fullLength)
                return bytesRead
            }
        }
    }

    interface ProgressResponseListener {
        fun update(url: HttpUrl, bytesRead: Long, contentLength: Long)
    }
}