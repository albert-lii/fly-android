package org.we.fly.widget.eilstextview

import android.graphics.Paint.FontMetricsInt
import android.text.style.LineHeightSpan

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/26 11:28 PM
 * @description: 去除TextView的自带行间距的类
 * @since: 1.0.0
 */
class ExInnerLineSpaceSpan(private val height: Int) : LineHeightSpan {

    override fun chooseHeight(
        text: CharSequence,
        start: Int,
        end: Int,
        spanstartv: Int,
        lineHeight: Int,
        fm: FontMetricsInt
    ) {
        // 原始行高
        val originHeight = fm.descent - fm.ascent
        if (originHeight <= 0) {
            return
        }
        // 计算比例值
        val ratio = height * 1.0f / originHeight
        // 根据最新行高，修改descent
        fm.descent = Math.round(fm.descent * ratio)
        // 根据最新行高，修改ascent
        fm.ascent = fm.descent - height
    }
}