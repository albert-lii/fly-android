package org.fly.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间相关工具类
 */
object TimeUtils {

    /**
     * 日期格式字符串转时间戳
     */
    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun strToMilli(time: String?, format: String = "yyyy-MM-dd HH:mm:ss"): Long {
        if (time.isNullOrEmpty()) {
            return 0L
        }
        try {
            val sdf = SimpleDateFormat(format)
            return sdf.parse(time).getTime()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0L
    }

    /**
     * 时间戳转日期格式字符串
     */
    @JvmStatic
    @SuppressLint("SimpleDateFormat")
    fun milliToStr(millis: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
        val date = Date(millis)
        return SimpleDateFormat(format).format(date)
    }
}