package org.fly.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import okhttp3.HttpUrl
import org.fly.imageloader.ImageLoaderConfig
import org.fly.imageloader.ImageLoaderOptions
import org.fly.imageloader.ImageLoaderStrategy
import org.fly.imageloader.glide.interceptor.ProgressInterceptor
import org.fly.imageloader.glide.interceptor.ProgressResponseBody
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/31 4:06 下午
 * @description: 使用Glide作为图片加载器
 * @since: 1.0.0
 */
class GlideImageLoaderStrategy : ImageLoaderStrategy {

    override fun init(context: Context, config: ImageLoaderConfig) {
        // 预先初始化Glide，用于提升第一次加载图片时的速度
        config.executor.execute {
            Glide.with(context.applicationContext)
                .asDrawable()
                .load("")
                .apply(
                    RequestOptions()
                        .skipMemoryCache(true)
                        .centerCrop()
                )
                .thumbnail()
                .preload()
        }
    }

    override fun load(imageView: ImageView, url: String, options: ImageLoaderOptions) {
        val builder = createRequestBuilder(imageView, url, options)
        registerProgressListener(url, options)
        builder?.load(url)
            ?.apply(mapToGlideOptions(options))
            ?.into(imageView)
    }

    override fun load(imageView: ImageView, resId: Int, options: ImageLoaderOptions) {
        val builder = createRequestBuilder(imageView, resId, options)
        builder?.load(resId)
            ?.apply(mapToGlideOptions(options))
            ?.into(imageView)
    }

    override fun load(imageView: ImageView, uri: Uri, options: ImageLoaderOptions) {
        val builder = createRequestBuilder(imageView, uri, options)
        builder?.load(uri)
            ?.apply(mapToGlideOptions(options))
            ?.into(imageView)
    }

    override fun load(imageView: ImageView, file: File, options: ImageLoaderOptions) {
        val builder = createRequestBuilder(imageView, file, options)
        builder?.load(file)
            ?.apply(mapToGlideOptions(options))
            ?.into(imageView)
    }

    override fun download(
        context: Context,
        url: String,
        imageSize: ImageLoaderOptions.ImageSize
    ): Future<File> {
        return Glide.with(context)
            .download(url)
            .let {
                if (imageSize.width == ImageLoaderOptions.ImageSize.ORIGIN_SIZE
                    || imageSize.height == ImageLoaderOptions.ImageSize.ORIGIN_SIZE
                ) {
                    it.submit()
                } else {
                    it.submit(imageSize.width, imageSize.height)
                }
            }
    }

    override fun clearMemoryCache(context: Context) {
        Glide.get(context).clearMemory()
    }

    override fun clearDiskCache(context: Context) {
        Glide.get(context).clearDiskCache()
    }

    private fun registerProgressListener(url: String, options: ImageLoaderOptions) {
        if (options.listener != null) {
            ProgressInterceptor.registerListener(
                url,
                object : ProgressResponseBody.ProgressResponseListener {
                    override fun update(url: HttpUrl, bytesRead: Long, contentLength: Long) {
                        options.listener?.onUpdateProgress(bytesRead, contentLength)
                    }
                })
        }
    }

    /**
     * 创建Glide的RequestBuilder
     */
    private fun createRequestBuilder(
        imageView: ImageView,
        resource: Any,
        options: ImageLoaderOptions
    ): RequestBuilder<out Any>? {
        var requestManager: RequestManager? = null
        try {
            requestManager = Glide.with(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (requestManager == null) {
            return null
        }
        return when (options.resourceType) {
            ImageLoaderOptions.ResourceType.DRAWABLE -> {
                requestManager.asDrawable().apply {
                    options.listener?.let {
                        addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                options.listener?.onError(e?.message ?: "")
                                if (resource is String) {
                                    ProgressInterceptor.unregisterListener(resource)
                                }
                                return false
                            }

                            override fun onResourceReady(
                                resourceDrawable: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                options.listener?.onSuccess()
                                if (resource is String) {
                                    ProgressInterceptor.unregisterListener(resource)
                                }
                                return false
                            }
                        })
                    }
                    options.placeHolder?.let { holderInfo ->
                        createHolderRequestBuilder(imageView, resource, holderInfo)?.let {
                            thumbnail(it)
                        }
                    }
                    options.errorHolder?.let { holderInfo ->
                        createHolderRequestBuilder(imageView, resource, holderInfo)?.let {
                            thumbnail(it)
                        }
                    }
                }
            }
            ImageLoaderOptions.ResourceType.BITMAP -> {
                requestManager.asBitmap().apply {
                    if (options.listener != null) {
                        addListener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                options.listener?.onError(e?.message ?: "")
                                if (resource is String) {
                                    ProgressInterceptor.unregisterListener(resource)
                                }
                                return false
                            }

                            override fun onResourceReady(
                                resourceBitmap: Bitmap?,
                                model: Any?,
                                target: Target<Bitmap>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                options.listener?.onSuccess()
                                if (resource is String) {
                                    ProgressInterceptor.unregisterListener(resource)
                                }
                                return false
                            }
                        })
                    }
                }
            }
            ImageLoaderOptions.ResourceType.GIF -> {
                requestManager.asGif().apply {
                    addListener(object : RequestListener<GifDrawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            options.listener?.onError(e?.message ?: "")
                            if (resource is String) {
                                ProgressInterceptor.unregisterListener(resource)
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resourceGif: GifDrawable?,
                            model: Any?,
                            target: Target<GifDrawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            options.listener?.onSuccess()
                            options.gifExtOption?.apply {
                                resourceGif?.setLoopCount(gifLoopCount)
                            }
                            if (resource is String) {
                                ProgressInterceptor.unregisterListener(resource)
                            }
                            return false
                        }
                    })
                }
            }
            ImageLoaderOptions.ResourceType.FILE -> {
                requestManager.asFile().apply {
                    if (options.listener != null) {
                        addListener(object : RequestListener<File> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<File>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                options.listener?.onError(e?.message ?: "")
                                if (resource is String) {
                                    ProgressInterceptor.unregisterListener(resource)
                                }
                                return false
                            }

                            override fun onResourceReady(
                                resourceFile: File?,
                                model: Any?,
                                target: Target<File>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                options.listener?.onSuccess()
                                if (resource is String) {
                                    ProgressInterceptor.unregisterListener(resource)
                                }
                                return false
                            }
                        })
                    }
                }
            }
        }
    }

    /**
     * 根据HolderInfo创建RequestBuilder
     */
    private fun createHolderRequestBuilder(
        imageView: ImageView,
        resource: Any,
        holderInfo: ImageLoaderOptions.HolderInfo
    ): RequestBuilder<Drawable>? {
        if (holderInfo.corners == null && holderInfo.scaleType == null) {
            return null
        }
        val options =
            ImageLoaderOptions(corners = holderInfo.corners, scaleType = holderInfo.scaleType)
        val builder = createRequestBuilder(imageView, resource, options)
        return builder?.load(holderInfo.resId)
            ?.apply(mapToGlideOptions(options)) as? RequestBuilder<Drawable>
    }

    /**
     * 将ImageLoaderOptions转变为Glide的RequestOptions
     */
    private fun mapToGlideOptions(options: ImageLoaderOptions): RequestOptions {
        var glideOptions = RequestOptions()

        options.placeHolder?.apply {
            if (resId != -1) {
                glideOptions = glideOptions.placeholder(resId)
            }
        }

        options.errorHolder?.apply {
            if (resId != -1) {
                glideOptions = glideOptions.error(resId)
            }
        }

        options.resize?.apply {
            val realResizeWidth =
                if (width == ImageLoaderOptions.ImageSize.ORIGIN_SIZE) Target.SIZE_ORIGINAL else width
            val realResizeHeight =
                if (height == ImageLoaderOptions.ImageSize.ORIGIN_SIZE) Target.SIZE_ORIGINAL else height
            glideOptions = glideOptions.override(realResizeWidth, realResizeHeight)
        }

        val listTransformation = arrayListOf<Transformation<Bitmap>>()

        options.corners?.apply {
            listTransformation.add(
                GranularRoundedCorners(
                    topLeftRadius,
                    topRightRadius,
                    bottomRightRadius,
                    bottomLeftRadius
                )
            )
        }

        options.scaleType?.apply {
            when (this) {
                ImageLoaderOptions.ScaleType.CENTER_CROP -> listTransformation.add(CenterCrop())
                ImageLoaderOptions.ScaleType.CENTER_INSIDE -> listTransformation.add(CenterInside())
                ImageLoaderOptions.ScaleType.FIT_CENTER -> listTransformation.add(FitCenter())
                ImageLoaderOptions.ScaleType.CIRCLE_CROP -> listTransformation.add(CircleCrop())
            }
        }

        if (listTransformation.isNotEmpty()) {
            glideOptions = glideOptions.transform(MultiTransformation(listTransformation))
        }

        glideOptions = glideOptions.skipMemoryCache(options.isSkipMemoryCache)

        glideOptions = options.diskCacheStrategy?.let {
            when (it) {
                ImageLoaderOptions.DiskCacheStrategy.ALL -> glideOptions.diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
                ImageLoaderOptions.DiskCacheStrategy.NONE -> glideOptions.diskCacheStrategy(
                    DiskCacheStrategy.NONE
                )
                ImageLoaderOptions.DiskCacheStrategy.RESOURCE -> glideOptions.diskCacheStrategy(
                    DiskCacheStrategy.RESOURCE
                )
                ImageLoaderOptions.DiskCacheStrategy.DATA -> glideOptions.diskCacheStrategy(
                    DiskCacheStrategy.DATA
                )
                ImageLoaderOptions.DiskCacheStrategy.AUTOMATIC -> glideOptions.diskCacheStrategy(
                    DiskCacheStrategy.AUTOMATIC
                )
            }
        } ?: glideOptions
        return glideOptions
    }
}