package org.fly.uikit.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.fly.uikit.R


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/14 5:09 PM
 * @description: DialogFragment的基类
 * @since: 1.0.0
 */
abstract class BaseDialogFragment : AppCompatDialogFragment(), CoroutineScope by MainScope() {
    protected lateinit var rootView: View

    private var width: Int = ViewGroup.LayoutParams.MATCH_PARENT
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var gravity: Int = Gravity.CENTER
    private var animRes: Int = -1
    private var dimAmount: Float = 0.5f
    private var alpha: Float = 1f

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            if (getDialogStyle() != null) getDialogStyle()!! else STYLE_NO_TITLE,
            if (getDialogTheme() != null) getDialogTheme()!! else R.style.fly_uikit_CommonDialog
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize(view, savedInstanceState)
    }

    override fun onStart() {
        try {
            // 在super之前执行，因为super.onStart()中dialog会执行自己的show方法
            updateAttributes()
            super.onStart()
        } catch (e: Exception) {
            dismissAllowingStateLoss()
        }
    }

    /**
     * 设置宽度
     */
    fun setWidth(width: Int) {
        this.width = width
    }

    /**
     * 设置高度
     */
    fun setHeight(height: Int) {
        this.height = height
    }

    /**
     * 设置位置
     */
    fun setGravity(gravity: Int) {
        this.gravity = gravity
    }

    /**
     * 设置窗口透明度
     */
    fun setDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float) {
        this.dimAmount = dimAmount
    }

    /**
     * 设置背景透明度
     */
    fun setAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        this.alpha = alpha
    }

    /**
     * 设置显示和隐藏动画
     */
    fun setAnimationRes(animation: Int) {
        this.animRes = animation
    }

    /**
     * 更新属性
     */
    fun updateAttributes() {
        if (dialog != null) {
            dialog!!.window!!.let {
                val params: WindowManager.LayoutParams = it.getAttributes()
                params.width = width
                params.height = height
                params.gravity = gravity
                params.windowAnimations = animRes
                params.dimAmount = dimAmount
                params.alpha = alpha
                it.setAttributes(params)
            }
        }
    }

    protected abstract fun getDialogStyle(): Int?

    protected abstract @StyleRes
    fun getDialogTheme(): Int?

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    protected abstract fun initialize(view: View, savedInstanceState: Bundle?)

    open fun isShowing(): Boolean {
        return isAdded && dialog != null && dialog!!.isShowing
    }

    open fun show(activity: FragmentActivity, tag: String? = null) {
        showAllowStateLoss(activity.supportFragmentManager, tag ?: this::javaClass.name)
    }

    open fun show(fragment: Fragment, tag: String? = null) {
        showAllowStateLoss(fragment.parentFragmentManager, tag ?: this::javaClass.name)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}