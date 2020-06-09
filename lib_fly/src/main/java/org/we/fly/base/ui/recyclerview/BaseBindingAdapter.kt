package org.we.fly.base.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/5/30 10:25 PM
 * @description: 基于DataBinding的RecyclerView Adapter封装
 * @since: 1.0.0
 */
abstract class BaseBindingAdapter<B : ViewDataBinding, T> :
    RecyclerView.Adapter<BaseViewHolder>() {
    private var items: MutableList<T>? = null
    private var itemClickListener: OnItemClickListener<T, B>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding: B = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(viewType),
            parent,
            false
        )
        return BaseViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val binding: B = DataBindingUtil.getBinding(holder.itemView)!!
        onBindItem(binding, items!![position], position)
        binding!!.executePendingBindings()
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener {
                itemClickListener!!.onItemClick(
                    binding,
                    items!![position],
                    position
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    @LayoutRes
    abstract fun getLayoutId(viewType: Int): Int

    abstract fun onBindItem(binding: B?, item: T, position: Int)

    fun getItems(): MutableList<T>? {
        return items
    }

    fun setItems(items: MutableList<T>?) {
        this.items = items
    }

    fun refreshItems(items: MutableList<T>?) {
        this.items = items
        notifyDataSetChanged()
    }

    fun refreshItem(position: Int, item: T) {
        if (itemCount > position) {
            items!!.set(position, item)
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (itemCount > position) {
            items!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        if (items != null) {
            items!!.clear()
            items = null
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T, B>?) {
        this.itemClickListener = listener
    }

    interface OnItemClickListener<T, B> {
        fun onItemClick(binding: B, item: T, position: Int)
    }
}