package org.fly.http.response

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/20 11:06 AM
 * @description: 网络请求响应解析后返回的对象（成功或者异常都会返回）
 * @since: 1.0.0
 */
sealed class ResponseHolder<out T> {

    /* 请求成功，返回成功响应  */
    data class Success<T>(val data: T?) : ResponseHolder<T>()

    /* 请求成功，返回失败响应 */
    data class Failure(val code: Int, var message: String? = null) : ResponseHolder<Nothing>()

    /* 请求失败，抛出异常 */
    data class Error(val error: HttpError) : ResponseHolder<Nothing>()

    private var onCompletion: (suspend () -> Unit)? = null

    fun isSuccess(): Boolean {
        return this is Success
    }

    fun isFailure(): Boolean {
        return this is Failure
    }

    fun isError(): Boolean {
        return this is Error && this.error.errorCode != HttpError.CANCEL_REQUEST
    }

    fun getData(){

    }

    /**
     * 网络请求完成处理，必须在onSuccess、onFailure与onCatch之前调用才有效
     */
    fun onCompletion(action: (suspend () -> Unit)?): ResponseHolder<T> {
        this.onCompletion = action
        return this
    }

    /**
     * 处理网络请求成功
     */
    suspend fun onSuccess(action: (suspend (data: T?) -> Unit)): ResponseHolder<T> {
        if (isSuccess()) {
            onCompletion?.invoke()
            action.invoke((this as Success).data)
        }
        return this
    }

    /**
     * 处理网络请求失败
     */
    suspend fun onFailure(action: (suspend (code: Int, message: String?) -> Unit)): ResponseHolder<T> {
        if (isFailure()) {
            onCompletion?.invoke()
            action.invoke((this as Failure).code, (this as Failure).message)
        }
        return this
    }

    /**
     * 捕获网络请求异常
     */
    suspend fun onCatch(action: (suspend (error: HttpError) -> Unit)): ResponseHolder<T> {
        if (isError()) {
            onCompletion?.invoke()
            action.invoke((this as Error).error)
        }
        return this
    }

    /**
     * 处理网络请求取消
     */
    suspend fun onCancel(action: (suspend () -> Unit)): ResponseHolder<T> {
        if (this is Error && error.errorCode == HttpError.CANCEL_REQUEST) {
            action.invoke()
        }
        return this
    }
}