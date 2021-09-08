package org.fly.uikit.popupwindow

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/16 7:16 下午
 * @description: 感知Activity生命周期变化的PopupWindow，所有的popupWindow都应继承它，
 *               可以解决activity finish 但是popupWindow还在引发的问题
 * @since: 1.0.0
 */
open class LifecyclePopupWindow(private val context: Context) : PopupWindow(),
    LifecycleObserver {

    protected val lifecycleOwner by lazy {
        if (context is LifecycleOwner) {
            context.lifecycle
        } else null
    }

    init {
        lifecycleOwner?.addObserver(this)
    }

    override fun dismiss() {
        lifecycleOwner?.removeObserver(this)
        super.dismiss()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onOwnerDestroy(owner: LifecycleOwner) {
        if (isShowing) {
            dismiss()
        }
    }

    override fun showAtLocation(parent: View?, gravity: Int, x: Int, y: Int) {
        try {
            super.showAtLocation(parent, gravity, x, y)
        } catch (e: WindowManager.BadTokenException) {
            if (isShowing) {
                dismiss()
            }
        }
    }

    fun showAndResetWidthHeight(
        anchor: View?,
        xoff: Int,
        yoff: Int,
        gravity: Int = Gravity.END,
        reset: Boolean = true
    ) {
        if (reset) {
            width = contentView.measuredWidth
            height = contentView.measuredHeight
        }
        showAsDropDown(anchor, xoff, yoff, gravity)
    }
}