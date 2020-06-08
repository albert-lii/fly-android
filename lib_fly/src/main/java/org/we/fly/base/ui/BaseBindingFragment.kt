package org.we.fly.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/8 4:17 PM
 * @description: 基于MVVM模式的Fragment的基类
 * @since: 1.0.0
 */
abstract class BaseBindingFragment<VM : BaseViewModel, B : ViewDataBinding> : Fragment(),
    DataBindingBehavior {
    protected lateinit var viewModel: VM
    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectViewModel()
        injectDataBinding(inflater, container)
        initView()
        addListener()
        addObserver()
        loadData()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getViewModelVariableId(): Int {
        return DataBindingBehavior.NO_VIEW_MODEL
    }

    protected fun injectViewModel() {
        val vm = createViewModel()
        viewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm))
            .get(vm::class.java)
        lifecycle.addObserver(viewModel)
    }

    private fun injectDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        if (getViewModelVariableId() != DataBindingBehavior.NO_VIEW_MODEL) {
            binding.setVariable(getViewModelVariableId(), viewModel)
        }
    }

    protected abstract fun createViewModel(): VM;

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    /**
     *  初始化UI
     */
    protected abstract fun initView()

    /**
     * 添加事件监听
     */
    protected abstract fun addListener()

    /**
     * 添加观察者
     */
    protected abstract fun addObserver()

    /**
     * 加载数据
     */
    protected abstract fun loadData()
}