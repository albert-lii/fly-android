package org.we.fly.base.ui.popupwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/29 10:21 AM
 * @description: 带有DataBinding的PopupWindow基类
 * @since: 1.0.0
 */
abstract class BaseBindingPopupWindow<B : ViewDataBinding>(protected val context: Context) :
    PopupWindow(context) {
    protected lateinit var binding: B

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getLayoutId(), null, false
        )
        contentView = binding.root
        initialize()
    }

    /**
     * 设置背景色，默认为半透明
     */
    fun addBackgroundDrawable(@ColorInt color: Int = 0x10000000) {
        // 实例化一个ColorDrawable
        val dw = ColorDrawable(color)
        // 设置弹出窗体的背景
        setBackgroundDrawable(dw)
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    protected abstract fun initialize()
}