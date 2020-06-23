package org.we.fly.utils.http

import android.net.ParseException
import com.google.gson.JsonParseException
import io.reactivex.observers.ResourceObserver
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/23 5:17 PM
 * @description: 网络请求观察者基类
 * @since: 1.0.0
 */
abstract class BaseHttpObserver<T> : ResourceObserver<T>() {

    override fun onComplete() {

    }

    override fun onNext(t: T) {
        try {
            onBaseNext(t)
        } catch (ex: Exception) {
            if (ex is ClassCastException || ex is JsonParseException || ex is JSONException || ex is ParseException) {
                handleError(
                    HttpServerException(
                        HttpError.PARSE_ERROR.code,
                        HttpError.PARSE_ERROR.message
                    )
                )
            } else {
                handleError(
                    HttpServerException(
                        HttpError.UNKNOWN_ERROR.code,
                        HttpError.UNKNOWN_ERROR.message
                    )
                )
            }
        }
    }

    override fun onError(e: Throwable) {
        val httpError = when (e) {
            is HttpException -> HttpError.BAD_NETWORK
            is ConnectException, is UnknownHostException -> HttpError.CONNECT_ERROR
            is InterruptedIOException -> HttpError.CONNECT_TIMEOUT
            is JsonParseException, is JSONException, is ParseException -> HttpError.PARSE_ERROR
            else -> HttpError.UNKNOWN_ERROR
        }
        handleError(HttpServerException(httpError.code, httpError.message))
    }

    abstract fun onBaseNext(t: T)

    abstract fun handleError(serverException: HttpServerException)
}