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
 * @time: 2020/6/12 7:19 PM
 * @description: 基于MVVM模式的懒加载的Fragment
 * @since: 1.0.0
 */
abstract class BaseBVMLazyFragmet<B : ViewDataBinding, VM : BaseViewModel> : BaseBindingLazyFragment<B>(),
    DataBindingBehavior, ViewBehavior {

    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewHolder == null) {
            injectViewModel()
            injectDataBinding(inflater, container)
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

    override fun injectDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        super.injectDataBinding(inflater, container)
        if (getViewModelVariableId() != DataBindingBehavior.NO_VIEW_MODEL) {
            binding.setVariable(getViewModelVariableId(), viewModel)
        }
    }

    override fun getViewModelVariableId(): Int {
        return DataBindingBehavior.NO_VIEW_MODEL
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
        viewModel._backPressEvent.observeNonNull(this, {
            backPress(it)
        })
        viewModel._finishPageEvent.observeNonNull(this, {
            finishPage(it)
        })
    }

    protected abstract fun createViewModel(): VM;
}
