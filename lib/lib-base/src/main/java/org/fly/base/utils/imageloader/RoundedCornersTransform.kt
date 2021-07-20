package org.fly.base.utils.imageloader

import android.graphics.Bitmap

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

import java.security.MessageDigest


class RoundedCornersTransform(
    val radius: Float,
    var isLeftTop: Boolean = true,
    var isRightTop: Boolean = true,
    var isLeftBottom: Boolean = true,
    var isRightBotoom: Boolean = true
) : BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return TransformationUtils.roundedCorners(
            pool,
            toTransform,
            if (isLeftTop) radius else 0f,
            if (isRightTop) radius else 0f,
            if (isLeftBottom) radius else 0f,
            if (isRightBotoom) radius else 0f
        )
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}