package org.fly.base.arch.mvvm

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/12 10:06 AM
 * @description: 集成DataBinding的Activity的基类
 * @since: 1.0.0
 */
abstract class BaseBindActivity<B : ViewDataBinding> : BaseActivity() {

    protected lateinit var binding: B
        private set

    override fun initContentView() {
        injectDataBinding()
    }

    protected fun injectDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        binding.unbind()
        super.onDestroy()
    }
}