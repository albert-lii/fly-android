package org.fly.uikit

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/16 7:28 下午
 * @description: kit相关扩展
 * @since: 1.0.0
 */

/**
 * dp 转 px
 */
val Float.dpToPxF: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dpToPxF: Float
    get() = this.toFloat().dpToPxF

val Float.dpToPx: Int
    get() = this.dpToPxF.toInt()

val Int.dpToPx: Int
    get() = this.dpToPxF.toInt()


/**
 * sp 转 px
 */
val Float.spToPxF: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )

val Int.spToPxF: Float
    get() = this.toFloat().spToPxF

val Float.spToPx: Int
    get() = this.spToPxF.toInt()

val Int.spToPx: Int
    get() = this.spToPxF.toInt()

/**
 * px 转 dp
 */
val Float.pxToDpF: Float
    get() = this.let {
        val scale = Resources.getSystem().displayMetrics.density
        (it / scale + 0.5f)
    }

val Int.pxToDpF: Float
    get() = this.toFloat().pxToDpF

val Float.pxToDp: Int
    get() = this.pxToDpF.toInt()

val Int.pxToDp: Int
    get() = this.pxToDpF.toInt()


/**
 * px 转 sp
 */
val Float.pxToSpF: Float
    get() = this.let {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        (it / scale + 0.5f)
    }

val Int.pxToSpF: Float
    get() = this.toFloat().pxToSpF

val Float.pxToSp: Int
    get() = this.pxToSpF.toInt()

val Int.pxToSp: Int
    get() = this.pxToSpF.toInt()


/**
 * 根据资源id获取drawable
 */
fun @receiver:DrawableRes Int.toDrawable(context: Context): Drawable? {
    return ContextCompat.getDrawable(context, this)
}