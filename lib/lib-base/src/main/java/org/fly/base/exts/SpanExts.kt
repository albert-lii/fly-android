package org.fly.base.exts

import android.graphics.Typeface
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/7/20 7:10 下午
 * @description: Span相关
 * @since: 1.0.0
 */

/**
 * 设置文字大小
 */
fun TextView.setSpanTextSize(fullStr: String, target: String, textSize: Int) {
    text = SpannableString(fullStr).apply {
        val start = fullStr.indexOf(target)
        if (start < 0) {
            return@apply
        }
        setSpan(
            AbsoluteSizeSpan(textSize, true),
            start,
            start + target.length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance()
}

/**
 * 设置文字颜色
 */
fun TextView.setSpanTextColor(fullStr: String, target: String, color: Int) {
    text = SpannableString(fullStr).apply {
        val start = fullStr.indexOf(target)
        if (start < 0) {
            return@apply
        }
        setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, color)),
            start,
            start + target.length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance()
}


/**
 * 设置文字样式
 */
fun TextView.setSpanTextStyle(fullStr: String, target: String, style: Int = Typeface.BOLD) {
    text = SpannableString(fullStr).apply {
        val start = fullStr.indexOf(target)
        if (start < 0) {
            return@apply
        }
        setSpan(
            StyleSpan(style),
            start,
            start + target.length,
            SpannableString.SPAN_INCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod = LinkMovementMethod.getInstance()
}