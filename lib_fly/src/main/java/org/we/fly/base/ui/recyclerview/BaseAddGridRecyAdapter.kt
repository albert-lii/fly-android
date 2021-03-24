package org.we.fly.base.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/7 10:27 PM
 * @description: 动态添加item的网格适配器
 * @since: 1.0.0
 */
abstract class BaseAddGridRecyAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {
    val VIEW_TYPE_ADD = 1
    val VIEW_TYPE_ITEM = 2

    // 数据源
    private var items: MutableList<T>? = null

    // 最多显示的item
    private var maxSize: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        var itemView: View? = null
        when (viewType) {
            VIEW_TYPE_ADD -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(getAddViewLayoutId(), parent, false)
            }
            VIEW_TYPE_ITEM -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(getItemViewLayoutId(), parent, false)
            }
            else -> {
                itemView = LayoutInflater.from(parent.context)
                    .inflate(getItemViewLayoutId(), parent, false)
            }
        }
        return BaseViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        val dataCount = if (items.isNullOrEmpty()) {
            0
        } else {
            items!!.size
        }
        if (maxSize == null) {
            return dataCount + 1
        } else {
            return if (dataCount < maxSize!!) {
                dataCount + 1
            } else {
                maxSize!!
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val dataCount = if (items.isNullOrEmpty()) {
            0
        } else {
            items!!.size
        }
        if (maxSize == null) {
            if (position < (itemCount - 1)) {
                return VIEW_TYPE_ADD
            } else {
                return VIEW_TYPE_ITEM
            }
        } else {
            if (dataCount < maxSize!!) {
                if (position < (itemCount - 1)) {
                    return VIEW_TYPE_ADD
                } else {
                    return VIEW_TYPE_ITEM
                }
            } else {
                return VIEW_TYPE_ITEM
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == VIEW_TYPE_ADD) {
            onBindAddItem(holder, position)
        } else if (viewType == VIEW_TYPE_ITEM) {
            onBindItem(holder, position, items!![position])
        }
    }

    fun getItems(): MutableList<T>? {
        return items
    }

    fun setItems(items: MutableList<T>?) {
        this.items = items
    }

    fun addItem(item: T) {
        if (!items.isNullOrEmpty()) {
            items!!.add(item)
        } else {
            items = ArrayList<T>()
            items!!.add(item)
        }
        notifyDataSetChanged()
    }

    fun refreshItems(items: MutableList<T>?) {
        this.items = items
        notifyDataSetChanged()
    }

    fun refreshItem(position: Int, item: T) {
        if (!items.isNullOrEmpty() && itemCount > position) {
            items!!.set(position, item)
            notifyItemChanged(position)
        }
    }

    fun removeItem(position: Int) {
        if (!items.isNullOrEmpty()) {
            items!!.removeAt(position)
            notifyItemRemoved(position)
            // 使用notifyItemRemoved()方法时此句一定要加，用于通知item的变化，否则item的position会错乱
            notifyItemRangeChanged(0, items!!.size)
        }
    }

    fun clear() {
        if (items != null) {
            items!!.clear()
            items = null
        }
    }

    fun setMaxSize(size: Int?) {
        this.maxSize = size
    }

    fun getMaxSize(): Int? {
        return this.maxSize
    }

    abstract fun getAddViewLayoutId(): Int

    abstract fun getItemViewLayoutId(): Int

    abstract fun onBindAddItem(holder: BaseViewHolder, position: Int)

    abstract fun onBindItem(holder: BaseViewHolder, position: Int, item: T)
}