package org.we.fly.base.ui.popupwindow

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.PopupWindow
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/28 2:03 PM
 * @description: PopupWindow的基类
 * @since: 1.0.0
 */
abstract class BasePopupWindow(protected val context: Context) : PopupWindow(context) {
    protected var uiScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        initContentView()
    }

    protected open fun initContentView(){
        val rootView = LayoutInflater.from(context).inflate(getLayoutId(), null)
        contentView = rootView
    }

    /**
     * 设置背景色，默认为半透明
     */
    fun addBackgroundDrawable(@ColorInt color: Int = 0x10000000) {
        // 实例化一个ColorDrawable
        val dw = ColorDrawable(0x10000000)
        // 设置弹出窗体的背景
        setBackgroundDrawable(dw)
    }

    override fun dismiss() {
        uiScope.cancel()
        super.dismiss()
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int
}