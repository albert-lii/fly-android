package org.we.fly.base.mvvm

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewHolder == null) {
            injectDataBinding(inflater, container)
            injectViewModel()
            initialize()
            initInternalObserver()
        }
        return binding.root
    }

    protected fun injectViewModel() {
        val vm = createViewModel()
        viewModel = ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm))
            .get(vm::class.java)
        lifecycle.addObserver(viewModel)
    }

    protected fun initInternalObserver() {
        viewModel._loadingEvent.observeNonNull(this, {
            showLoadingUI(it)
        })
        viewModel._emptyPageEvent.observeNonNull(this, {
            showEmptyUI(it)
        })
        viewModel._toastStrEvent.observeNonNull(this, {
            showToast(it[0] as String,it[1] as Int)
        })
        viewModel._toastStrIdEvent.observeNonNull(this, {
            showToast(it[0],it[1])
        })
        viewModel._pageNavigationEvent.observeNonNull(this, {
            navigateTo(it)
        })
        viewModel._backPressEvent.observeNonNull(this, {
            backPress(it)
        })
        viewModel._finishPageEvent.observeNonNull(this, {
            finishPage(it)
        })
    }

    protected abstract fun createViewModel(): VM;
}