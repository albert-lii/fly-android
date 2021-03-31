package org.we.fly.base.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/8 4:17 PM
 * @description: 基于MVVM模式的Fragment的基类
 * @since: 1.0.0
 */
abstract class BaseBindingFragment<B : ViewDataBinding> : BaseFragment() {
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
        doLazyLoad(false)
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


abstract class BaseBinding2Fragment<B : ViewDataBinding> : Fragment() {
    /**
     * 缓存视图，如果视图已经创建，则不再初始化视图
     */
    protected var rootView: View? = null

    protected lateinit var binding: B
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (rootView != null) {
            return rootView
        }
        rootView = inflater.inflate(getLayoutId(), container, false)
        injectDataBinding(inflater, container)
        initialize(savedInstanceState)
        return rootView
    }

    protected open fun injectDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        rootView = binding.root
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    /**
     *  初始化操作
     */
    protected abstract fun initialize(savedInstanceState: Bundle?)

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }
}