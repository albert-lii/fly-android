package org.fly.uikit.recyclerview

/**
 * 快捷使用的Adapter
 */
abstract class BaseQuickEasyAdapter<T> : BaseEasyAdapter<T, BaseViewHolder> {
    constructor(layoutResId: Int) : super(layoutResId)
    constructor(layoutResId: Int, data: MutableList<T>?) : super(layoutResId, data)
}