package org.fly.imageloader

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import org.fly.imageloader.glide.GlideImageLoaderStrategy
import java.io.File
import java.util.concurrent.Future

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/31 4:03 下午
 * @description: 图片加载工具类
 * @since: 1.0.0
 */
@SuppressLint("StaticFieldLeak")
object ImageLoader {

    private lateinit var context: Context

    lateinit var config: ImageLoaderConfig
        private set

    fun init(context: Context, config: ImageLoaderConfig? = null) {
        var cfg = config
        if (cfg == null) {
            cfg = ImageLoaderConfig(imageLoaderStrategy = GlideImageLoaderStrategy())
        }
        this.context = context.applicationContext
        this.config = cfg
        cfg.imageLoaderStrategy.init(context, cfg)
    }

    fun load(url: String): ImageLoaderBuilder<String> {
        return ImageLoaderBuilder<String>().load(url)
    }

    fun load(@RawRes @DrawableRes resId: Int): ImageLoaderBuilder<Int> {
        return ImageLoaderBuilder<Int>().load(resId)
    }

    fun load(file: File): ImageLoaderBuilder<File> {
        return ImageLoaderBuilder<File>().load(file)
    }

    fun load(uri: Uri): ImageLoaderBuilder<Uri> {
        return ImageLoaderBuilder<Uri>().load(uri)
    }

    fun download(
        context: Context,
        url: String,
        imageSize: ImageLoaderOptions.ImageSize = ImageLoaderOptions.ImageSize()
    ): Future<File> {
        return config.imageLoaderStrategy.download(context, url, imageSize)
    }

    @UiThread
    fun clearMemoryCache() {
        config.imageLoaderStrategy.clearMemoryCache(context)
    }

    @WorkerThread
    fun clearDiskCache() {
        config.imageLoaderStrategy.clearDiskCache(context)
    }

    class ImageLoaderBuilder<T> {
        private var resource: T? = null
        private var options = config.defaultOptions
        private lateinit var imageView: ImageView

        fun load(resource: T): ImageLoaderBuilder<T> {
            this.resource = resource
            return this
        }

        fun applyOptions(options: ImageLoaderOptions): ImageLoaderBuilder<T> {
            this.options = options
            return this
        }

        fun into(imageView: ImageView) {
            this.imageView = imageView
            when (resource) {
                is String -> {
                    config.imageLoaderStrategy.load(imageView, resource as String, options)
                }
                is Int -> {
                    config.imageLoaderStrategy.load(imageView, resource as Int, options)
                }
                is Uri -> {
                    config.imageLoaderStrategy.load(imageView, resource as Uri, options)
                }
                is File -> {
                    config.imageLoaderStrategy.load(imageView, resource as File, options)
                }
            }
        }
    }
}

fun ImageView?.load(
    url: String,
    options: ImageLoaderOptions = ImageLoader.config.defaultOptions
) {
    if (this == null) {
        return
    }
    ImageLoader.load(url).applyOptions(options).into(this)
}

fun ImageView?.load(
    @RawRes @DrawableRes resId: Int,
    options: ImageLoaderOptions = ImageLoader.config.defaultOptions
) {
    if (this == null) {
        return
    }
    ImageLoader.load(resId).applyOptions(options).into(this)
}

fun ImageView?.load(
    uri: Uri,
    options: ImageLoaderOptions = ImageLoader.config.defaultOptions
) {
    if (this == null) {
        return
    }
    ImageLoader.load(uri).applyOptions(options).into(this)
}

fun ImageView?.load(
    file: File,
    options: ImageLoaderOptions = ImageLoader.config.defaultOptions
) {
    if (this == null) {
        return
    }
    ImageLoader.load(file).applyOptions(options).into(this)
}