package org.we.fly.base.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/28 7:45 PM
 * @description: 带有DataBinding的Dialog基类
 * @since: 1.0.0
 */
abstract class BaseBindingDialog<B : ViewDataBinding> : AppCompatDialog {
    private val INVAILD_VALUE = -1
    protected var binding: B? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            getLayoutId(), null, false
        )
        setContentView(binding!!.root)
        initialize(savedInstanceState)
    }

    fun setWidth(width: Int) {
        setSize(width, INVAILD_VALUE)
    }

    fun setHeight(height: Int) {
        setSize(INVAILD_VALUE, height)
    }

    /**
     * 设置弹框大小
     */
    fun setSize(width: Int, height: Int) {
        window?.let {
            val params: WindowManager.LayoutParams = it.getAttributes()
            if (width > 0) {
                params.width = width
            }
            if (height > 0) {
                params.height = height
            }
            it.setAttributes(params)
        }
    }

    /**
     * 设置位置
     */
    fun setGravity(gravity: Int) {
        window?.let {
            val params: WindowManager.LayoutParams = it.getAttributes()
            params.gravity = gravity
            it.setAttributes(params)
        }
    }

    /**
     * 设置背景透明度
     */
    fun setBackgroundAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        window?.let {
            val params: WindowManager.LayoutParams = it.getAttributes()
            params.alpha = alpha
            it.setAttributes(params)
        }
    }

    /**
     * 设置显示和隐藏动画
     */
    fun setAnimation(animation: Int) {
        window?.let {
            val params: WindowManager.LayoutParams = it.getAttributes()
            params.windowAnimations = animation
            it.setAttributes(params)
        }
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    protected abstract fun initialize(savedInstanceState: Bundle?)
}