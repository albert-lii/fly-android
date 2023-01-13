package org.fly.utils

import android.content.res.Resources

/**
 * 尺寸单位转换工具类
 */
object SizeUtils {

    /**
     * dp 转 px
     */
    @JvmStatic
    fun dpToPx(value: Float): Int {
        return android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * sp 转 px
     */
    @JvmStatic
    fun spToPx(value: Float): Int {
        return android.util.TypedValue.applyDimension(
            android.util.TypedValue.COMPLEX_UNIT_SP, value, Resources.getSystem().displayMetrics
        ).toInt()
    }

    /**
     * px 转 dp
     */
    @JvmStatic
    fun pxToDp(value: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return (value / scale + 0.5f)
    }

    /**
     * px 转 sp
     */
    @JvmStatic
    fun pxToSp(value: Float): Float {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        return (value / scale + 0.5f)
    }
}