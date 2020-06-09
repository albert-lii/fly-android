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
abstract class BaseBindingFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment(),
    DataBindingBehavior {
    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    // 缓存视图，如果视图已经创建，则不再初始化视图
    protected var viewHolder: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewHolder == null) {
            injectViewModel()
            injectDataBinding(inflater, container)
            initView()
            addListener()
            addObserver()
            loadData()
        }
        return binding.root
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
        viewHolder = binding.root
    }

    override fun getViewModelVariableId(): Int {
        return DataBindingBehavior.NO_VIEW_MODEL
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