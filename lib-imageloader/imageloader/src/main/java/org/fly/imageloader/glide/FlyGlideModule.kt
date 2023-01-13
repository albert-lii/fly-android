package org.fly.imageloader.glide

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.LibraryGlideModule
import com.caverock.androidsvg.SVG
import okhttp3.OkHttpClient
import org.fly.imageloader.glide.interceptor.ProgressInterceptor
import org.fly.imageloader.glide.svg.SvgDecoder
import org.fly.imageloader.glide.svg.SvgDrawableTranscoder
import java.io.InputStream

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/31 9:40 下午
 * @description: -
 * @since: 1.0.0
 */
@GlideModule
class GlideModule : LibraryGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(ProgressInterceptor)
        val okHttpClient = builder.build()
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(okHttpClient)
        )
        registry.register(
            SVG::class.java,
            PictureDrawable::class.java,
            SvgDrawableTranscoder()
        ).append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }
}