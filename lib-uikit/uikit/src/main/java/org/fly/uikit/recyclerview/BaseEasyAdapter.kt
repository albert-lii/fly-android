package org.fly.uikit.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * RecyclerView适配器基类
 */
abstract class BaseEasyAdapter<T, VH : BaseViewHolder> : BaseDataAdapter<T, VH> {
    private val layoutResId: Int
    private var itemClickListener: OnItemClickListener? = null
    private var itemLongClickListener: OnItemLongClickListener? = null

    constructor(@LayoutRes layoutResId: Int) : super() {
        this.layoutResId = layoutResId
    }

    constructor(@LayoutRes layoutResId: Int, data: MutableList<T>?) : super(data) {
        this.layoutResId = layoutResId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createBaseViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        convert(holder, position, data[position])
    }

    /**
     * 创建BaseViewHolder
     */
    protected open fun createBaseViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = BaseViewHolder(getItemView(parent, layoutResId, viewType)) as VH
        itemClickListener?.run {
            holder.itemView.setOnClickListener { v ->
                val position = holder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                this.onItemClick(v, position)
            }
        }
        itemLongClickListener?.run {
            holder.itemView.setOnLongClickListener { v ->
                val position = holder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    false
                } else {
                    this.onItemLongClick(v, position) ?: false
                }
            }
        }
        return holder
    }

    /**
     * holder与item绑定处理
     */
    abstract fun convert(holder: VH, position: Int, item: T)

    /**
     * 获取item的布局
     */
    protected open fun getItemView(
        parent: ViewGroup,
        @LayoutRes layoutResId: Int,
        viewType: Int
    ): View {
        return LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
    }

    open fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    open fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.itemLongClickListener = listener
    }
}