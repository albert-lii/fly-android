package org.fly.uikit.recyclerview.binding

import androidx.viewbinding.ViewBinding

/**
 * 快捷使用的集成ViewBinding的适配器
 */
abstract class BaseQuickVBAdapter<T, VB : ViewBinding> :
    BaseVBAdapter<T, VB, BaseVBViewHolder<VB>> {
    constructor() : super()
    constructor(data: MutableList<T>?) : super(data)
}