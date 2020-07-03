package org.we.fly.extensions

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/3 5:11 PM
 * @description: 资源相关
 * @since: 1.0.0
 */


/**
 * 将资源id转换为颜色
 */
@ColorInt
fun @receiver:ColorRes Int.toColor(context: Context): Int {
    return ContextCompat.getColor(context, this)
}

/**
 * 将资源id转换为字符串
 */
fun @receiver:StringRes Int.toString(context: Context): String {
    return context.getString(this)
}