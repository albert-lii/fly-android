package org.fly.base.arch.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/8 4:17 PM
 * @description: 基于DataBinding的Fragment的基类
 * @since: 1.0.0
 */
abstract class BaseBindFragment<B : ViewDataBinding> : BaseFragment() {
    protected lateinit var binding: B
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setCurrentState(ILazyLoad.ON_CREATE_VIEW)
        if (getRootView() != null) {
            return getRootView()
        }
        injectDataBinding(inflater, container)
        initialize(savedInstanceState)
        excuteLazyInit(false)
        return getRootView()
    }

    protected open fun injectDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        setRootView(binding.root)
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}