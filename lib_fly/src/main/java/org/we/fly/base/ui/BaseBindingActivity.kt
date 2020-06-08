package org.we.fly.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 10:28 PM
 * @description: 基于MVVM模式的Activity的基类
 * @since: 1.0.0
 */
abstract class BaseBindingActivity<VM : BaseViewModel, B : ViewDataBinding> : AppCompatActivity(),
    DataBindingBehavior {
    protected lateinit var viewModel: VM
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModel()
        injectDataBinding()
        initView()
        addListener()
        addObserver()
        loadData()
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

    protected fun injectDataBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        if (getViewModelVariableId() != DataBindingBehavior.NO_VIEW_MODEL) {
            binding.setVariable(getViewModelVariableId(), viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
        lifecycle.removeObserver(viewModel)
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