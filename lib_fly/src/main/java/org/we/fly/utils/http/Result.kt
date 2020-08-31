package org.we.fly.utils.http

import java.lang.Error

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/8/20 11:06 AM
 * @description: 网络请求最终返回的结果（成功或者异常都会返回）
 * @since: 1.0.0
 */
sealed class Result<out T : Any> {
    /* 请求成功，返回成功响应  */
    data class Success<out T : Any>(val data: T?) : Result<T>()

    /* 请求成功，返回失败响应 */
    data class Failure(val code: Int, val msg: String) : Result<Nothing>()

    /* 请求失败，抛出异常 */
    data class ERROR(val ex: Throwable, val error: HttpError) : Result<Nothing>()

    fun then(
        onSuccess: (data: T?) -> Unit,
        onFailure: ((code: Int, msg: String) -> Unit)? = null,
        onError: ((ex: Throwable, error: HttpError) -> Unit)? = null
    ) {
        when (this) {
            is Success<T> -> onSuccess(data)
            is Failure -> onFailure?.let { it(code, msg) }
            is ERROR -> onError?.let { it(ex, error) }
        }
    }
}