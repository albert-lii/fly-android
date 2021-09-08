package org.fly.uikit.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/4 7:48 下午
 * @description: 多种Type的RecyclerView适配器
 * @since: 1.0.0
 */
abstract class BaseMultiRecyclerAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        onBindItem(holder, getItemViewType(position), position)
    }

    abstract @LayoutRes
    fun getLayoutId(viewType: Int): Int

    abstract fun onBindItem(holder: BaseViewHolder, viewType: Int, position: Int)
}