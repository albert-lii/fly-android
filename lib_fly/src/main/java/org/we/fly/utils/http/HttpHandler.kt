package org.we.fly.utils.http

import android.net.ParseException
import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/18 5:55 PM
 * @description: 网络请求处理类
 * @since: 1.0.0
 */
interface HttpHandler {

    suspend fun request(
        doRequest: suspend () -> Unit,
        doCatch: suspend (e: Throwable) -> Unit = {},
        doEnd: suspend () -> Unit = {}
    ) {
        try {
            doRequest()
        } catch (e: Throwable) {
            parseException(e)
            doCatch(e)
        } finally {
            doEnd()
        }
    }

    /**
     * 解析网络请求异常
     */
    fun parseException(e: Throwable) {
        when (e) {
            is HttpException -> HttpError.BAD_NETWORK
            is ConnectException, is UnknownHostException -> HttpError.CONNECT_ERROR
            is InterruptedIOException -> HttpError.CONNECT_TIMEOUT
            is JsonParseException, is JSONException, is ParseException, is ClassCastException -> HttpError.PARSE_ERROR
            else -> HttpError.UNKNOWN_ERROR
        }
    }
}