package org.fly.uikit

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import java.lang.ref.WeakReference

/**
 * 自定义Toast
 */
class EasyToast(
    var context: Context,
    mGravity: Int = Gravity.CENTER_VERTICAL,
    mXOffset: Int = 0,
    mYOffset: Int = 0
) : Toast(context) {

    enum class WidthType(var maxWidth: Int) {
        Max_Width_130(130),
        Max_Width_246(246),
        Max_Width_275(275);
    }

    var MIN_WIDTH = 90
    var toastView: View? = null

    init {
        toastView = LayoutInflater.from(context).inflate(R.layout.fuikit_toast_layout, null)
        view = toastView
        setGravity(mGravity, mXOffset, mYOffset)
        duration = Toast.LENGTH_LONG
    }

    /**
     * 设置显示的文字，WidthType 有两种类型，Max_Width_130对应的是是文字居中，Max_Width_246对应的是文字居左
     * Set the displayed text. There are two types of WidthType. Max_Width_130 corresponds to the text centered, and Max_Width_246 corresponds to the text left
     */
    fun setShowText(toastText: String, widthType: WidthType = WidthType.Max_Width_246) {
        toastView?.let {
            val textView = toastView?.findViewById<TextView>(R.id.fuikit_toast_show_text)
            val contentView = toastView?.findViewById<ConstraintLayout>(R.id.uikit_toast_root)
            textView?.let { tv ->
                when (widthType) {
                    WidthType.Max_Width_130 -> {
                        tv.maxWidth =
                            (WidthType.Max_Width_130.maxWidth * context.resources.displayMetrics.density).toInt()
                        tv.minWidth = (MIN_WIDTH * context.resources.displayMetrics.density).toInt()
                        tv.gravity = Gravity.CENTER
                    }
                    WidthType.Max_Width_246 -> tv.maxWidth =
                        (WidthType.Max_Width_246.maxWidth * context.resources.displayMetrics.density).toInt()
                    WidthType.Max_Width_275 -> {
                        val density = context.resources.displayMetrics.density
                        tv.setLineSpacing(6 * density, 1.1f)
                        tv.maxWidth = (WidthType.Max_Width_275.maxWidth * density).toInt()
                        val tvMargin = tv.layoutParams as ViewGroup.MarginLayoutParams
                        tvMargin.marginEnd = 0
                        tvMargin.marginStart = 0
                        tvMargin.topMargin = 0
                        tvMargin.bottomMargin = 0
                        tv.layoutParams = tvMargin
                        tv.includeFontPadding = false
                        val paddingStart = (15f * density).toInt()
                        val paddingTop = (10f * density).toInt()
                        contentView?.setPadding(paddingStart, paddingTop, paddingStart, paddingTop)
                    }
                }
                tv.text = toastText
            }
        }
    }

    /**
     * 设置左边的图标
     * set left icon
     */
    fun setLeftIcon(leftIconResId: Int?) {
        toastView?.let {
            var imageView = it.findViewById<ImageView>(R.id.fuikit_toast_left_icon)
            leftIconResId?.let { imgRes ->
                imageView?.visibility = View.VISIBLE
                imageView?.setImageResource(imgRes)
            }
        }
    }

    /**
     * 设置顶部的图标
     * set top icon
     */
    fun setTopIcon(topIconResId: Int? = null) {
        toastView?.let {
            var imageView = it.findViewById<ImageView>(R.id.fuikit_toast_top_icon)
            topIconResId?.let { imgRes ->
                imageView?.visibility = View.VISIBLE
                imageView?.setImageResource(imgRes)
            }
        }
    }

    /**
     * 是否显示底部的空间，因为有两种样式，其中一种下边距比较长，需要显示
     * Whether to display the bottom space, because there are two styles, one of which has a longer bottom margin and needs to be displayed
     */
    fun setSpaceVisible(visible: Boolean) {
        if (visible) {
            toastView?.findViewById<Space>(R.id.fuikit_toast_space)?.visibility = View.VISIBLE
        } else {
            toastView?.findViewById<Space>(R.id.fuikit_toast_space)?.visibility = View.GONE
        }

    }

    companion object {
        private var weakPreToast: WeakReference<EasyToast>? = null

        /**
         * 显示提示信息
         * Display prompt information
         */
        fun createAndShowText(
            context: Context,
            toastText: String,
            mGravity: Int = Gravity.CENTER_VERTICAL,
            mXOffset: Int = 0,
            mYOffset: Int = 0,
            duration: Int = Toast.LENGTH_LONG
        ): EasyToast {
            val preToast: EasyToast? = weakPreToast?.get()
            preToast?.cancel()
            val toast = EasyToast(context, mGravity, mXOffset, mYOffset)
            toast.setSpaceVisible(false)
            toast.setShowText(toastText, WidthType.Max_Width_275)
            toast.duration = duration
            toast.show()
            weakPreToast = WeakReference(toast)
            return toast
        }
    }

    override fun cancel() {
        try {
            super.cancel()
        } catch (e: Exception) {

        }
    }

    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
