package org.we.fly.base.ui.popupwindow

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/29 10:21 AM
 * @description: 带有DataBinding的PopupWindow基类
 * @since: 1.0.0
 */
abstract class BaseBindingPopupWindow<B : ViewDataBinding>(context: Context) :
    BasePopupWindow(context) {

    protected lateinit var binding: B

    override fun initContentView() {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getLayoutId(), null, false
        )
        contentView = binding.root
    }
}