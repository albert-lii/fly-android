package org.fly.base.utils.imageloader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/14 10:07 PM
 * @description: 图片加载工具类
 * @since: 1.0.0
 */
object ImageLoader {

    /**
     * 加载图片
     */
    @JvmStatic
    fun load(
        context: Context,
        imageView: ImageView,
        url: String,
        loadingDrawable: Int,
        errorDrawable: Int
    ) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(loadingDrawable).error(errorDrawable))
            .into(imageView)
    }

    /**
     * 加载圆形图
     */
    @JvmStatic
    fun loadCircle(
        context: Context,
        imageView: ImageView,
        url: String,
        loadingDrawable: Int,
        errorDrawable: Int
    ) {
        val roundOptions = RequestOptions()
            .transform(CenterCrop(), CircleCrop()).placeholder(loadingDrawable).error(errorDrawable)
        Glide.with(context)
            .load(url)
            .apply(roundOptions)
            .into(imageView)
    }

    /**
     * 加载圆角图
     */
    @JvmStatic
    fun loadCorners(
        context: Context, imageView: ImageView, url: String,
        radius: Int,
        loadingDrawable: Int,
        errorDrawable: Int
    ) {
        val roundOptions = RequestOptions()
            .transform(CenterCrop(), RoundedCorners(radius)).placeholder(loadingDrawable)
            .error(errorDrawable)
        Glide.with(context)
            .load(url)
            .apply(roundOptions)
            .into(imageView)
    }

    /**
     * 加载圆角图
     */
    @JvmStatic
    fun loadCorner(
        context: Context, imageView: ImageView, url: String,
        radius: Float,
        isLeftTop: Boolean = true,
        isRightTop: Boolean = true,
        isLeftBottom: Boolean = true,
        isRightBotoom: Boolean = true,
        loadingDrawable: Int,
        errorDrawable: Int
    ) {
        val roundOptions = RequestOptions()
            .transform(
                RoundedCornersTransform(
                    radius,
                    isLeftTop,
                    isRightTop,
                    isLeftBottom,
                    isRightBotoom
                )
            ).placeholder(loadingDrawable).error(errorDrawable)
        Glide.with(context)
            .load(url)
            .apply(roundOptions)
            .into(imageView)
    }
}