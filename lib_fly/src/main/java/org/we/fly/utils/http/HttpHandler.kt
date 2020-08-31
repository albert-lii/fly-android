package org.we.fly.utils.http

import android.net.ParseException
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.JsonParseException
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    /**
     * 将对象类转换为RequestBody
     */
    fun convertRequesBody(obj: Any): RequestBody {
        return GsonUtils.toJson(obj).toRequestBody("application/json".toMediaTypeOrNull())
    }

    suspend fun <T : Any> request(call: suspend () -> HttpResponse<T>): HttpResponse<T> {
        return call()
    }

    /**
     * 建议调用此方法发送网络请求，因为协程中出现异常时，会直接抛出，所以使用try...catch方法捕获异常
     */
    suspend fun <T : Any> safeRequest(call: suspend () -> Result<T>): Result<T> {
        return try {
            call()
        } catch (ex: Throwable) {
            Result.ERROR(ex, parseException(ex))
        }
    }

    /**
     * 在请求完成后，处理网络请求返回的Response，并返回Result
     */
    suspend fun <T : Any> handleResponse(response: HttpResponse<T>): Result<T> {
        return if (response.isSuccess()) {
            Result.Success(response.data)
        } else {
            Result.Failure(response.code, response.msg)
        }
    }

    /**
     * 解析网络请求异常
     */
    fun parseException(e: Throwable): HttpError {
        return when (e) {
            is HttpException -> HttpError.BAD_NETWORK
            is ConnectException, is UnknownHostException -> HttpError.CONNECT_ERROR
            is InterruptedIOException -> HttpError.CONNECT_TIMEOUT
            is JsonParseException, is JSONException, is ParseException, is ClassCastException -> HttpError.PARSE_ERROR
            else -> HttpError.UNKNOWN_ERROR
        }
    }
}