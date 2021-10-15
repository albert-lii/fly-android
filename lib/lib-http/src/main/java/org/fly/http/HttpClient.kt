package org.fly.http

import android.content.Context
import android.net.ParseException
import android.webkit.MimeTypeMap
import com.google.gson.JsonParseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.fly.http.request.RequestService
import org.fly.http.response.HttpError
import org.fly.http.response.InfoResponse
import org.fly.http.response.ResponseHolder
import org.json.JSONException
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.io.InterruptedIOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/8 4:59 下午
 * @description: Http客户端
 * @since: 1.0.0
 */
open class HttpClient : HttpClientBase() {

    fun init(context: Context, httpClientConfig: HttpClientConfig) {
        initialize(context, httpClientConfig)
    }

    /**
     * Http Get
     */
    @JvmOverloads
    suspend fun <T> get(
        url: String,
        headers: Map<String, String>? = null,
        params: Map<String, String>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> =
        request(type, isInfoResponse) { it.get(url, headers ?: mapOf(), params ?: mapOf()) }


    /**
     * Http Post
     * Data post in form
     */
    @JvmOverloads
    suspend fun <T> postForm(
        url: String,
        headers: Map<String, String>? = null,
        params: Map<String, String>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> =
        request(type, isInfoResponse) { it.postForm(url, headers ?: mapOf(), params ?: mapOf()) }

    /**
     * Http Post
     * Data post in json
     */
    @JvmOverloads
    suspend fun <T> postJson(
        url: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> {
        var ct = ""
        if (!params.isNullOrEmpty()) {
            ct = getGson().toJson(params)
        }
        return postJsonString(url, headers, ct, type, isInfoResponse)
    }

    @JvmOverloads
    suspend fun <T> postJsonString(
        url: String,
        headers: Map<String, String>? = null,
        content: String? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> =
        request(type, isInfoResponse) { it.postJson(url, headers ?: mapOf(), content ?: "") }

    /**
     * Support Multipart body for POST
     */
    @JvmOverloads
    suspend fun <T> postMultipart(
        url: String,
        headers: Map<String, String>? = null,
        params: Map<String, Any>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> = request(type, isInfoResponse) {
        val mb = MultipartBody.Builder().setType(MultipartBody.FORM)
        params?.forEach {
            if (it.value is File) {
                val file = it.value as File
                var mimeType = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.absolutePath))
                if (mimeType.isNullOrBlank()) {
                    mimeType = "file/*"
                }
                mb.addFormDataPart(
                    it.key,
                    file.name,
                    file.asRequestBody(mimeType.toMediaTypeOrNull())
                )
            } else {
                mb.addFormDataPart(it.key, it.value.toString())
            }
        }
        it.postOrigin(url, headers ?: mapOf(), mb.build())
    }


    @JvmOverloads
    suspend fun <T> put(
        url: String,
        headers: Map<String, String>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> = request(type, isInfoResponse) { it.put(url, headers ?: mapOf()) }

    @JvmOverloads
    suspend fun <T> delete(
        url: String,
        headers: Map<String, String>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> = request(type, isInfoResponse) { it.delete(url, headers ?: mapOf()) }

    @JvmOverloads
    suspend fun <T> head(
        url: String,
        headers: Map<String, String>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> = request(type, isInfoResponse) { it.head(url, headers ?: mapOf()) }

    @JvmOverloads
    suspend fun <T> options(
        url: String,
        headers: Map<String, String>? = null,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> = request(type, isInfoResponse) { it.options(url, headers ?: mapOf()) }

    suspend fun downloadFile(url: String): Response<ResponseBody>? {
        try {
            return getRequestService().downloadFile(url)
        } catch (cause: Throwable) {
            return null
        }
    }

    /**
     * 建议使用此方法发起网络请求
     * 因为协程中出现异常时，会直接抛出异常，所以使用try...catch方法捕获异常
     */
    open suspend fun <T> request(
        type: Type,
        isInfoResponse: Boolean = true,
        call: suspend (service: RequestService) -> Response<String>
    ): ResponseHolder<T> {
        try {
            val response = call.invoke(getRequestService())
            return parseResponse(response, type, isInfoResponse)
        } catch (cause: Throwable) {
            val httpError = catchException(cause)
            return ResponseHolder.Error(httpError)
        }
    }

    /**
     * 发起网络请求，返回Flow
     * 使用普通的协程访问已经足够满足大部分请求，此处使用Flow请求仅仅作为一个扩展
     * 如需大规模使用Flow，可以按照上述request方式进行扩充
     */
    @ExperimentalCoroutinesApi
    open fun <T> requestFlow(
        type: Type,
        isInfoResponse: Boolean = true,
        call: suspend (service: RequestService) -> Response<String>
    ): Flow<ResponseHolder<T>> {
        try {
            return flow {
                val response = call.invoke(getRequestService())
                emit(parseResponse(response, type, isInfoResponse))
            }
        } catch (cause: Throwable) {
            return flow {
                val httpError = catchException(cause)
                emit(ResponseHolder.Error(httpError))
            }
        }
    }

    /**
     * 解析请求返回的Response
     */
    open fun <T> parseResponse(
        response: Response<String>,
        type: Type,
        isInfoResponse: Boolean = true
    ): ResponseHolder<T> {
        try {
            if (response.isSuccessful && response.body() != null) {
                // 请求成功
                if (isInfoResponse) {
                    return resolveInfoResponse(response, type)
                } else {
                    return resolveUnInfoResponse(response, type)
                }
            } else {
                // 请求失败
                return resolveFailedResponse(response)
            }
        } catch (cause: Throwable) {
            val httpError = catchException(cause)
            httpError.httpCode = response.code()
            return ResponseHolder.Error(httpError)
        }
    }

    /**
     * 解析成功的网络请求返回的响应，InfoResponse形式
     */
    open fun <T> resolveInfoResponse(
        response: Response<String>,
        type: Type
    ): ResponseHolder<T> {
        val resp = getGson().fromJson<InfoResponse<T>>(response.body(), type)
        if (resp.isSuccessful()) {
            // 请求成功，返回成功响应
            return ResponseHolder.Success(resp.data)
        } else {
            // 请求成功，返回失败响应
            return ResponseHolder.Failure(resp.code, resp.message)
        }
    }

    /**
     * 解析成功的网络请求返回的响应，非InfoResponse形式
     */
    open fun <T> resolveUnInfoResponse(
        response: Response<String>,
        type: Type
    ): ResponseHolder<T> {
        val resp = getGson().fromJson<T>(response.body(), type)
        return ResponseHolder.Success(resp)
    }

    /**
     * 解析失败的网络请求返回的响应
     */
    open fun resolveFailedResponse(response: Response<String>): ResponseHolder<Nothing> {
        val errorCode = response.raw().code ?: HttpError.UNKNOW_ERROR
        val errorMsg = response.raw().message ?: ""
        val httpError = HttpError(
            httpCode = response.code(),
            errorCode = errorCode,
            errorMsg = errorMsg
        )
        return ResponseHolder.Error(httpError)
    }

    /**
     * 捕获异常
     */
    open fun catchException(cause: Throwable): HttpError {
        return when (cause) {
            is ConnectException,
            is UnknownHostException -> HttpError(
                errorCode = HttpError.CONNECT_ERROR,
                errorMsg = "服务器连接失败",
                cause = cause
            )
            is InterruptedIOException -> HttpError(
                errorCode = HttpError.CONNECT_TIMEOUT,
                errorMsg = "网络请求超时",
                cause = cause
            )
            is HttpException -> HttpError(
                errorCode = HttpError.BAD_NETWORK,
                errorMsg = "网络请求出错",
                cause = cause
            )
            is JsonParseException,
            is JSONException,
            is ParseException,
            is ClassCastException -> HttpError(
                errorCode = HttpError.PARSE_ERROR,
                errorMsg = "数据解析失败",
                cause = cause
            )
            is CancellationException -> HttpError(
                errorCode = HttpError.CANCEL_REQUEST,
                errorMsg = "",
                cause = cause
            )
            else -> HttpError(errorCode = HttpError.UNKNOW_ERROR, errorMsg = "未知错误", cause = cause)
        }
    }
}