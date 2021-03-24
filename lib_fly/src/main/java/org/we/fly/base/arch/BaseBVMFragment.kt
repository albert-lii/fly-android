package org.we.fly.base.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import org.we.fly.extensions.observeNonNull

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/8 4:17 PM
 * @description: 基于MVVM模式的Fragment的基类
 * @since: 1.0.0
 */
abstract class BaseBVMFragment<B : ViewDataBinding, VM : BaseViewModel> : BaseBindingFragment<B>(),
    ViewBehavior {

    protected lateinit var viewModel: VM
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
        injectViewModel()
        initialize(savedInstanceState)
        initInternalObserver()
        doLazyLoad(false)
        return getRootView()
    }

    protected fun injectViewModel() {
        val vm = createViewModel()
        viewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm))
            .get(vm::class.java)
        viewModel.application = activity!!.application
        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
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
        viewModel._backPressEvent.observeNonNull(this) {
            backPress(it)
        }
        viewModel._finishPageEvent.observeNonNull(this) {
            finishPage(it)
        }
    }

    protected abstract fun createViewModel(): VM;
}