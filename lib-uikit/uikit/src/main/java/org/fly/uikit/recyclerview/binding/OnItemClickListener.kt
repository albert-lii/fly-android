package org.fly.uikit.recyclerview.binding

import androidx.viewbinding.ViewBinding

interface OnItemClickListener<VB:ViewBinding> {
    fun onItemClick(binding: VB, position: Int)
}