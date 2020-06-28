package org.we.fly.utils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/28 6:42 PM
 * @description: 防止快速点击的工具类
 * @since: 1.0.0
 */
class NoFastClickUtils private constructor() {
    private var lastClickTime: Long

    companion object {
        @JvmStatic
        fun getInstance(): NoFastClickUtils {
            return SingletonHolder.holder
        }

        private const val INTERVAL = 500
    }

    private object SingletonHolder {
        val holder = NoFastClickUtils()
    }

    init {
        lastClickTime = System.currentTimeMillis() - (1 + INTERVAL)
    }

    fun isNoFastClick(): Boolean {
        val t = System.currentTimeMillis()
        if (Math.abs(t - lastClickTime) > INTERVAL) {
            lastClickTime = t
            return true
        }
        return false
    }
}