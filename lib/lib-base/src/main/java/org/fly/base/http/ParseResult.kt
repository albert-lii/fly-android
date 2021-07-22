package org.fly.base.http

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/20 11:06 AM
 * @description: 网络响应解析后返回的结果（成功或者异常都会返回）
 * @since: 1.0.0
 */
sealed class ParseResult<out T : Any> {
    /* 请求成功，返回成功响应  */
    data class Success<out T : Any>(val data: T?) : ParseResult<T>()

    /* 请求成功，返回失败响应 */
    data class Failure(val code: Int, var msg: String? = null) :
        ParseResult<Nothing>()

    /* 请求失败，抛出异常 */
    data class ERROR(val ex: Throwable, val error: HttpError) : ParseResult<Nothing>()

    private var successBlock: (suspend (data: T?) -> Unit)? = null
    private var failureBlock: (suspend (code: Int, msg: String?) -> Unit)? = null
    private var errorBlock: (suspend (ex: Throwable, error: HttpError) -> Unit)? = null
    private var cancelBlock: (suspend () -> Unit)? = null

    /**
     * 设置网络请求成功处理
     */
    fun doSuccess(successBlock: (suspend (data: T?) -> Unit)?): ParseResult<T> {
        this.successBlock = successBlock
        return this
    }

    /**
     * 设置网络请求失败处理
     */
    fun doFailure(failureBlock: (suspend (code: Int, msg: String?) -> Unit)?): ParseResult<T> {
        this.failureBlock = failureBlock
        return this
    }

    /**
     * 设置网络请求异常处理
     */
    fun doError(errorBlock: (suspend (ex: Throwable, error: HttpError) -> Unit)?): ParseResult<T> {
        this.errorBlock = errorBlock
        return this
    }

    /**
     * 设置网络请求取消处理
     */
    fun doCancel(cancelBlock: (suspend () -> Unit)?): ParseResult<T> {
        this.cancelBlock = cancelBlock
        return this
    }

    suspend fun procceed() {
        when (this) {
            is Success<T> -> successBlock?.invoke(data)
            is Failure -> failureBlock?.invoke(code, msg)
            is ERROR -> {
                if (this.error == HttpError.CANCEL_REQUEST) {
                    cancelBlock?.invoke()
                } else {
                    errorBlock?.invoke(ex, error)
                }
            }
        }
    }
}