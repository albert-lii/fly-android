package org.fly.base.arch.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/9 6:14 PM
 * @description: 使用databinding的动态添加item的网格适配器
 * @since: 1.0.0
 */
abstract class BaseBindingAddGridRecyAdapter<VB_ADD : ViewDataBinding, VB_ITEM : ViewDataBinding, T> :
    BaseAddGridRecyAdapter<T>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView: View
        when (viewType) {
            VIEW_TYPE_ADD -> {
                itemView = bindingInflate<VB_ADD>(getAddViewLayoutId(), parent).root
            }
            VIEW_TYPE_ITEM -> {
                itemView = bindingInflate<VB_ITEM>(getItemViewLayoutId(), parent).root
            }
            else -> {
                itemView = bindingInflate<VB_ITEM>(getItemViewLayoutId(), parent).root
            }
        }
        return BaseViewHolder(itemView)
    }

    override fun onBindAddItem(holder: BaseViewHolder, position: Int) {
        val binding = getBinding<VB_ADD>(holder)
        onBindAddItem(binding, position)
        binding.executePendingBindings()
    }

    override fun onBindItem(holder: BaseViewHolder, position: Int, item: T) {
        val binding = getBinding<VB_ITEM>(holder)
        onBindItem(binding, position, item)
        binding.executePendingBindings()
    }

    protected fun <VB : ViewDataBinding> getBinding(holder: BaseViewHolder): VB {
        return DataBindingUtil.getBinding(holder.itemView)!!
    }

    protected fun <VB : ViewDataBinding> bindingInflate(
        layoutId: Int,
        parent: ViewGroup,
        attachToParent: Boolean = false
    ): VB {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            attachToParent
        )
    }

    abstract fun onBindAddItem(binding: VB_ADD, position: Int)

    abstract fun onBindItem(binding: VB_ITEM, position: Int, item: T)
}