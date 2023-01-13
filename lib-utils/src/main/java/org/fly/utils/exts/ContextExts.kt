package org.fly.utils.exts

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat


/**=================================================================================================
 * 资源获取
 *================================================================================================*/
/**
 * 根据资源id获取Color
 */
fun Context.getColorRes(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}

/**
 * 根据资源id获取String
 */
fun Context.getStringRes(@StringRes resId: Int): String {
    return this.getString(resId)
}

/**
 * 根据资源id获取Dimen
 */
fun Context.getDimenRes(@DimenRes resId: Int): Float {
    return this.resources.getDimension(resId)
}

/**
 * 根据资源id获取Px单位的Dimen
 */
fun Context.getDimenPxRes(@DimenRes resId: Int): Int {
    return this.resources.getDimensionPixelSize(resId)
}

/**
 * 根据资源id获取Drawable
 */
fun Context.getDrawableRes(@DrawableRes resId: Int): Drawable? {
    return ContextCompat.getDrawable(this, resId)
}
