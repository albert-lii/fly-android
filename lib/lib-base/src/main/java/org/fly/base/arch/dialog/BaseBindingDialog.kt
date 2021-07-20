package org.fly.base.arch.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/28 7:45 PM
 * @description: 带有DataBinding的Dialog基类
 * @since: 1.0.0
 */
abstract class BaseBindingDialog<B : ViewDataBinding> : BaseDialog {
    protected lateinit var binding: B

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(savedInstanceState)
        refreshAttributes()
    }

    override fun initContentView() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(getContext()),
            getLayoutId(), null, false
        )
        setContentView(binding.root)
    }
}