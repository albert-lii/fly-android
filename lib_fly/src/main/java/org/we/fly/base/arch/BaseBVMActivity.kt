package org.we.fly.base.arch

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
abstract class BaseBVMActivity<B : ViewDataBinding, VM : BaseViewModel> : BaseBindingActivity<B>(),
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
            showLoadingUI(it)
        }
        viewModel._emptyPageEvent.observeNonNull(this) {
            showEmptyUI(it)
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