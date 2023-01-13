package org.fly.uikit.recyclerview

import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView适配器基类，仅做数据处理
 */
abstract class BaseDataAdapter<T, VH : RecyclerView.ViewHolder>
@JvmOverloads constructor(data: MutableList<T>? = null) : RecyclerView.Adapter<VH>() {

    var data: MutableList<T> = data ?: mutableListOf()
        internal set

    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * 获取item
     */
    open fun getItem(@IntRange(from = 0) position: Int): T {
        return data[position]
    }

    /**
     * 获取item，返回item或null
     */
    open fun getItemOrNull(@IntRange(from = 0) position: Int): T? {
        return data.getOrNull(position)
    }

    /**
     * 获取item的位置
     */
    open fun getItemPosition(item: T?): Int {
        return if (item != null && this.data.isNotEmpty()) this.data.indexOf(item) else -1
    }

    /**
     * 设置新的数据引用，替换原有的内存引用。
     * 如非必要，使用 setList
     */
    open fun setNewList(list: MutableList<T>?) {
        if (list === this.data) {
            return
        }
        this.data = list ?: mutableListOf()
        notifyDataSetChanged()
    }

    /**
     * 设置数据集合内容，但是不会改变内存引用
     */
    open fun setList(list: Collection<T>?) {
        this.data.clear()
        if (!list.isNullOrEmpty()) {
            this.data.addAll(list)
        }
        notifyDataSetChanged()
    }

    /**
     * 更新指定位置的item
     */
    open fun setItem(@IntRange(from = 0) position: Int, item: T) {
        if (position >= this.data.size) {
            return
        }
        this.data[position] = item
        notifyItemChanged(position)
    }

    /**
     * 在list末尾插入数据集合
     */
    open fun addList(newList: Collection<T>?) {
        newList?.let {
            this.data.addAll(it)
            notifyItemRangeInserted(this.data.size - it.size, it.size)
        }
    }

    /**
     * 在list的指定位置插入数据集合
     */
    open fun addList(@IntRange(from = 0) position: Int, newList: Collection<T>?) {
        newList?.let {
            this.data.addAll(position, it)
            notifyItemRangeInserted(position, it.size)
        }
    }

    /**
     * 在list末尾插入item
     */
    open fun addItem(@NonNull item: T) {
        this.data.add(item)
        notifyItemInserted(this.data.size - 1)
    }

    /**
     * 在list的指定位置末尾插入item
     */
    open fun addItem(@IntRange(from = 0) position: Int, @NonNull item: T) {
        this.data.add(position, item)
        notifyItemInserted(position)
    }

    /**
     * 根据position移除item
     */
    open fun removeAt(@IntRange(from = 0) position: Int) {
        if (position >= this.data.size) {
            return
        }
        this.data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.data.size - position)
    }

    /**
     * 移除item
     */
    open fun remove(item: T) {
        val index = this.data.indexOf(item)
        if (index == -1) {
            return
        }
        removeAt(index)
    }
}