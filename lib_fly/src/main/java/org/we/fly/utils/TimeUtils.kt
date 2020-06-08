package org.we.fly.utils

import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/2 10:55 AM
 * @description: --
 * @since: 1.0.0
 */
object TimeUtils {
    /**
     * 时间戳转字符串
     */
    fun timestampToString(time: Long, pattern: String = "YYYY-MM-dd HH:mm"): String {
        return SimpleDateFormat(pattern).format(time)
    }

    /**
     * 字符串转时间戳
     */
    fun stringToTimestamp(date: String, pattern: String = "YYYY-MM-dd HH:mm"): Long {
        return SimpleDateFormat(pattern).parse(date, ParsePosition(0)).time
    }
}