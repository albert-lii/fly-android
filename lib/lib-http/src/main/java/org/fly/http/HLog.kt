package org.fly.http

import android.util.Log

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/8 1:16 下午
 * @description: Log工具类
 * @since: 1.0.0
 */
object HLog {
    @JvmField
    var tag = "fly-android"

    @JvmField
    var isDebug = true

    @JvmStatic
    fun i(message: String) {
        i(tag, message)
    }

    @JvmStatic
    fun d(message: String) {
        d(tag, message)
    }

    @JvmStatic
    fun v(message: String) {
        v(tag, message)
    }

    @JvmStatic
    fun w(message: String) {
        w(tag, message)
    }

    @JvmStatic
    fun e(message: String) {
        e(tag, message)
    }

    @JvmStatic
    fun i(tag: String, message: String) {
        if (isDebug) Log.i(tag, message)
    }

    @JvmStatic
    fun d(tag: String, message: String) {
        if (isDebug) Log.d(tag, message)
    }

    @JvmStatic
    fun v(tag: String, message: String) {
        if (isDebug) Log.v(tag, message)
    }

    @JvmStatic
    fun w(tag: String, message: String) {
        if (isDebug) Log.w(tag, message)
    }

    @JvmStatic
    fun e(tag: String, message: String) {
        if (isDebug) Log.e(tag, message)
    }

    @JvmStatic
    fun e(className: String, methodName: String, message: String?) {
        if (isDebug) Log.e("$className#$methodName()", message ?: "")
    }

    @JvmStatic
    fun printStackTrace(throwable: Throwable) {
        if (isDebug) throwable.printStackTrace()
    }

    @JvmStatic
    fun printStackTrace(tag: String) {
        if (!isDebug) {
            return
        }
        for (stackTraceElement in Thread.currentThread().stackTrace) {
            Log.d(tag, stackTraceElement.toString())
        }
    }
}