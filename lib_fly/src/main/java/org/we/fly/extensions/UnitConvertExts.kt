package org.we.fly.extensions

import android.content.res.Resources

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 5:13 PM
 * @description: 单位转换
 * @since: 1.0.0
 */

/**
 * dp 转 px
 */
val Float.dpToPx: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

/**
 * dp 转 px
 */
val Int.dpToPx: Float
    get() = this.toFloat().dpToPx

/**
 * sp 转 px
 */
val Float.spToPx: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )

/**
 * sp 转 px
 */
val Int.spToPx: Float
    get() = this.toFloat().spToPx

/**
 * px 转 dp
 */
val Float.pxToDp: Float
    get() = this.let {
        val scale = Resources.getSystem().displayMetrics.density
        (it / scale + 0.5f)
    }

/**
 * px 转 dp
 */
val Int.pxToDp: Float
    get() = this.toFloat().pxToDp

/**
 * px 转 sp
 */
val Float.pxToSp: Float
    get() = this.let {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        (it / scale + 0.5f)
    }

/**
 * px 转 sp
 */
val Int.pxToSp: Float
    get() = this.toFloat().pxToSp