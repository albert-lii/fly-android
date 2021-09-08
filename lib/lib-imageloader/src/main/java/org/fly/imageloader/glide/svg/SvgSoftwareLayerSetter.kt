package org.fly.imageloader.glide.svg

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target


/**
 * Listener which updates the {@link ImageView} to be software rendered, because {@link
 * com.caverock.androidsvg.SVG SVG}/{@link android.graphics.Picture Picture} can't render on a
 * hardware backed {@link android.graphics.Canvas Canvas}.
 *
 * Created by ak on 2021/1/14.
 */
class SvgSoftwareLayerSetter : RequestListener<PictureDrawable?> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<PictureDrawable?>?,
        isFirstResource: Boolean
    ): Boolean {
        val view: ImageView = (target as ImageViewTarget<*>).view
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null)
        return false
    }

    override fun onResourceReady(
        resource: PictureDrawable?,
        model: Any?,
        target: Target<PictureDrawable?>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        val view: ImageView = (target as ImageViewTarget<*>).view
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        return false
    }
}
