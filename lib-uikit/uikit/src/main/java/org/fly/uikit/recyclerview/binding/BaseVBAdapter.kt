package org.fly.uikit.recyclerview.binding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.cloud.lib.common.base.recyclerview.BaseDataAdapter
import java.lang.reflect.ParameterizedType

/**
 * 集成ViewBinding的RecyclerView适配器基类
 */
abstract class BaseVBAdapter<T, VB : ViewBinding, VH : BaseVBViewHolder<VB>> :
    BaseDataAdapter<T, VH> {
    private var itemClickListener: OnItemClickListener<VB>? = null
    private var itemLongClickListener: OnItemLongClickListener<VB>? = null

    constructor() : super()

    constructor(data: MutableList<T>?) : super(data)

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
        val binding = getViewBinding(parent, viewType)
            ?: throw NullPointerException("ViewHolder获取ViewBinding对象失败")
        val holder = BaseVBViewHolder(binding) as VH
        itemClickListener?.run {
            holder.itemView.setOnClickListener { v ->
                val position = holder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                this.onItemClick(holder.binding, position)
            }
        }
        itemLongClickListener?.run {
            holder.itemView.setOnLongClickListener { v ->
                val position = holder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) {
                    false
                } else {
                    this.onItemLongClick(holder.binding, position) ?: false
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
     * 获取ViewBinding对象
     */
    protected open fun getViewBinding(parent: ViewGroup, viewType: Int): VB? {
        try {
            val type = javaClass.genericSuperclass
            if (type is ParameterizedType) {
                val bindingClass = type.actualTypeArguments[1] as Class<VB>
                val method = bindingClass.getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
                return method.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    open fun setOnItemClickListener(listener: OnItemClickListener<VB>) {
        this.itemClickListener = listener
    }

    open fun setOnItemLongClickListener(listener: OnItemLongClickListener<VB>) {
        this.itemLongClickListener = listener
    }
}