package org.fly.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import java.io.File
import java.util.concurrent.Future

/**
 * 图片加载策略
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/31 4:01 下午
 * @since: 1.0.0
 */
interface ImageLoaderStrategy {

    fun init(context: Context, config: ImageLoaderConfig)

    fun load(imageView: ImageView, url: String, options: ImageLoaderOptions)

    fun load(
        imageView: ImageView,
        @RawRes @DrawableRes resId: Int,
        options: ImageLoaderOptions
    )

    fun load(imageView: ImageView, uri: Uri, options: ImageLoaderOptions)

    fun load(imageView: ImageView, file: File, options: ImageLoaderOptions)

    fun download(
        context: Context,
        url: String,
        imageSize: ImageLoaderOptions.ImageSize
    ): Future<File>

    fun clearMemoryCache(context: Context)

    fun clearDiskCache(context: Context)
}