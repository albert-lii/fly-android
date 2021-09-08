package org.fly.imageloader.glide.svg

import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.caverock.androidsvg.SVG


/**
 * Convert the {@link SVG}'s internal representation to an Android-compatible one ({@link Picture}).
 * Created by ak on 2021/1/14.
 */
class SvgDrawableTranscoder : ResourceTranscoder<SVG?, PictureDrawable?> {

    override fun transcode(
        toTranscode: Resource<SVG?>,
        options: Options
    ): Resource<PictureDrawable?>? {
        val svg: SVG = toTranscode.get()
        val picture = svg.renderToPicture()
        val drawable = PictureDrawable(picture)
        return SimpleResource(drawable)
    }
}