package org.we.fly.base.mvvm

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import org.we.fly.extensions.observeNonNull
import org.we.fly.extensions.observeNullable

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 10:28 PM
 * @description: 基于MVVM模式的Activity的基类
 * @since: 1.0.0
 */
abstract class BaseBVMActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity(),
    DataBindingBehavior, ViewBehavior {

    protected lateinit var binding: B
    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectViewModel()
        injectDataBinding()
        initInternalObserver()
        initialize(savedInstanceState)
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
            binding.executePendingBindings()
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

    protected fun initInternalObserver() {
        viewModel._loadingEvent.observeNonNull(this, {
            showLoadingUI(it)
        })
        viewModel._emptyPageEvent.observeNonNull(this, {
            showEmptyUI(it)
        })
        viewModel._toastStrEvent.observeNonNull(this, {
            showToast(it[0] as String, it[1] as Int)
        })
        viewModel._toastStrIdEvent.observeNonNull(this, {
            showToast(it[0], it[1])
        })
        viewModel._pageNavigationEvent.observeNonNull(this, {
            navigateTo(it)
        })
        viewModel._backPressEvent.observeNullable(this, {
            backPress(it)
        })
        viewModel._finishPageEvent.observeNullable(this, {
            finishPage(it)
        })
    }

    protected abstract @LayoutRes
    fun getLayoutId(): Int

    protected abstract fun createViewModel(): VM

    /**
     *  初始化操作
     */
    protected abstract fun initialize(savedInstanceState: Bundle?)
}