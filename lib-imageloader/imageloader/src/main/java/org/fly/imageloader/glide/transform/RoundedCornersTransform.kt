package org.fly.imageloader.glide.transform

import android.graphics.Bitmap

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

import java.security.MessageDigest


class RoundedCornersTransform(
    var topLeftRadius: Float = 0f,
    var topRightRadius: Float = 0f,
    var bottomLeftRadius: Float = 0f,
    var bottomRightRadius: Float = 0f,
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
            topLeftRadius,
            topRightRadius,
            bottomLeftRadius,
            bottomRightRadius
        )
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}