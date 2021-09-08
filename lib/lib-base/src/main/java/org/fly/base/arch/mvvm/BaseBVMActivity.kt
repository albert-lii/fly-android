package org.fly.base.arch.mvvm

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import org.fly.base.exts.observeNonNull
import org.fly.base.exts.observeNullable

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 10:28 PM
 * @description: 基于MVVM模式的Activity的基类
 * @since: 1.0.0
 */
abstract class BaseBVMActivity<B : ViewDataBinding, VM : BaseViewModel> : BaseBindActivity<B>(),
    ViewBehavior {

    protected lateinit var viewModel: VM
        private set

    override fun initContentView() {
        super.initContentView()
        injectViewModel()
        initInternalObserver()
    }

    protected fun injectViewModel() {
        val vm = createViewModel()
        viewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm))
            .get(vm::class.java)
        viewModel.application = application
        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {
        binding.unbind()
        lifecycle.removeObserver(viewModel)
        super.onDestroy()
    }

    protected fun initInternalObserver() {
        viewModel._loadingEvent.observeNonNull(this) {
            showLoadingView(it)
        }
        viewModel._emptyPageEvent.observeNonNull(this) {
            showEmptyView(it)
        }
        viewModel._toastEvent.observeNonNull(this) {
            showToast(it)
        }
        viewModel._pageNavigationEvent.observeNonNull(this) {
            navigate(it)
        }
        viewModel._backPressEvent.observeNullable(this) {
            backPress(it)
        }
        viewModel._finishPageEvent.observeNullable(this) {
            finishPage(it)
        }
    }

    protected abstract fun createViewModel(): VM
}