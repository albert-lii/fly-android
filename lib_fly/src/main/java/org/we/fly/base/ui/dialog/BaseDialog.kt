package org.we.fly.base.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/28 2:02 PM
 * @description: dialog的基类
 * @since: 1.0.0
 */
abstract class BaseDialog : AppCompatDialog {
    private var width: Int = ViewGroup.LayoutParams.MATCH_PARENT
    private var height: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var gravity: Int = Gravity.CENTER
    private var animRes: Int = -1
    private var dimAmount: Float = 0.5f
    private var alpha: Float = 1f

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initialize(savedInstanceState)
        refreshAttributes()
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
     * 刷新属性
     */
    fun refreshAttributes() {
        window!!.let {
            val params: WindowManager.LayoutParams = it.getAttributes()
            params.width = width
            params.height = height
            params.gravity = gravity
            params.windowAnimations = animRes
            params.dimAmount = dimAmount
            params.alpha = alpha
            params.windowAnimations = animRes
            it.setAttributes(params)
        }
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    protected abstract fun initialize(savedInstanceState: Bundle?)
}