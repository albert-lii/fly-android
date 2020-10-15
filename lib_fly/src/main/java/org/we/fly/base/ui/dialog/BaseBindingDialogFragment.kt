package org.we.fly.base.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/23 5:06 PM
 * @description: 带有DataBinding的DialogFragment基类
 * @since: 1.0.0
 */
abstract class BaseBindingDialogFragment<B : ViewDataBinding> : BaseDialogFragment() {
    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        rootView = binding.root
        return rootView
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getDialogStyle(): Int? {
        return null
    }

    override fun getDialogTheme(): Int? {
        return null
    }
}