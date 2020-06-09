package org.we.fly.base.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import org.we.fly.extensions.observeNonNullSimply

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 10:28 PM
 * @description: 基于MVVM模式的Activity的基类
 * @since: 1.0.0
 */
abstract class BaseBindingActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    DataBindingBehavior, ViewBehavior {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModel()
        injectDataBinding()
        initView()
        addListener()
        addDefaultObserver()
        addObserver()
        loadData()
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

    override fun getViewModelVariableId(): Int {
        return DataBindingBehavior.NO_VIEW_MODEL
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
        lifecycle.removeObserver(viewModel)
    }

    protected fun addDefaultObserver() {
        viewModel._loadingEvent.observeNonNullSimply(this, {
            showLoadingUI(it)
        })
        viewModel._emptyPageEvent.observeNonNullSimply(this, {
            showEmptyUI(it)
        })
    }

    override fun showLoadingUI(isShow: Boolean) {

    }

    override fun showEmptyUI(isShow: Boolean) {

    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun createViewModel(): VM;

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