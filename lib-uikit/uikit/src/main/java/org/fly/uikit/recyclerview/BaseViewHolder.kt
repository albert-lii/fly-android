package org.fly.uikit.recyclerview

import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val views: SparseArray<View> = SparseArray()

    fun <T : View> getView(@IdRes resId: Int): T {
        var v: View? = views.get(resId)
        if (v == null) {
            v = itemView.findViewById(resId)
            views.put(resId, v)
        }
        return v as T
    }

    fun setText(@IdRes resId: Int, text: CharSequence) {
        val textView = getView<TextView>(resId)
        textView.text = text
    }

    fun setImage(@IdRes resId: Int, @DrawableRes src: Int) {
        val imageView = getView<ImageView>(resId)
        imageView.setImageResource(src)
    }

    fun setImage(@IdRes resId: Int, src: Drawable) {
        val imageView = getView<ImageView>(resId)
        imageView.setImageDrawable(src)
    }
}