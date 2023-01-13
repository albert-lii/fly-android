package org.fly.uikit.recyclerview.binding

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseVBViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root) {

}