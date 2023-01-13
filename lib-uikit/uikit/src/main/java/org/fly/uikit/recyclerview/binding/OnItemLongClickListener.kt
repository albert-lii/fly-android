package org.fly.uikit.recyclerview.binding

import androidx.viewbinding.ViewBinding

interface OnItemLongClickListener<VB:ViewBinding> {
    fun onItemLongClick(binding: VB, position: Int): Boolean
}