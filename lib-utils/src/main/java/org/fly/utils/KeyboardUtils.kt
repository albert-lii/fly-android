package org.fly.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import kotlin.math.abs

/**
 * 键盘工具类
 */
object KeyboardUtils {

    /**
     * 弹出软键盘
     */
    @JvmStatic
    fun showKeyboard(editText: EditText) {
        if (!editText.requestFocus()) {
            return
        }
        val imm =
            editText.context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 收起软键盘
     */
    @JvmStatic
    fun hideKeyboard(view: View) {
        val inputManager =
            view.context.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 软键盘是否弹出
     */
    fun isKeyboardShown(activity: Activity): Boolean {
        val softKeyboardHeight = 100
        // 获取Activity的根布局
        (activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup).getChildAt(0)?.let {
            val rect = Rect()
            it.getWindowVisibleDisplayFrame(rect)
            val dm = it.resources.displayMetrics
            val heightDiff = it.bottom - rect.bottom
            return heightDiff > softKeyboardHeight * dm.density
        } ?: kotlin.run {
            return false
        }
    }

    /**=============================================================================================
     * 监听键盘高度
     *============================================================================================*/
    private var decorViewInVisibleHeightPre = 0
    private var decorViewDelta = 0
    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    private fun getDecorViewInVisibleHeight(activity: Activity): Int {
        val decorView = activity.window.decorView
        val outRect = Rect()
        decorView.getWindowVisibleDisplayFrame(outRect) // 获取当前窗口实际的可见区域
        val delta = abs(decorView.bottom - outRect.bottom)
        if (delta <= ScreenUtils.getNavBarHeight()) {
            decorViewDelta = delta
            return 0
        }
        return delta - decorViewDelta
    }

    /**
     * 注册监听
     */
    fun registerKeyboardHeightListener(activity: Activity, onChanged: (height: Int) -> Unit) {
        val flags = activity.window.attributes.flags
        if ((flags and WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        val contentView = activity.findViewById<FrameLayout>(android.R.id.content)
        decorViewInVisibleHeightPre = getDecorViewInVisibleHeight(activity)
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val invisibleHeight = getDecorViewInVisibleHeight(activity)
            if (decorViewInVisibleHeightPre != invisibleHeight) {
                onChanged.invoke(invisibleHeight)
                decorViewInVisibleHeightPre = invisibleHeight
            }
        }
        contentView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    /**
     * 取消监听
     */
    fun unregisterKeyboardHeightListener(activity: Activity) {
        globalLayoutListener = null
        val contentView = activity.findViewById<FrameLayout>(android.R.id.content) ?: return
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
    }
}