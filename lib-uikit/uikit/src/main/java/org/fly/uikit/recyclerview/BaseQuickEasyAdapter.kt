package org.fly.uikit.recyclerview

import com.cloud.lib.common.base.recyclerview.BaseViewHolder

/**
 * 快捷使用的Adapter
 */
abstract class BaseQuickEasyAdapter<T> : BaseEasyAdapter<T, BaseViewHolder> {
    constructor(layoutResId: Int) : super(layoutResId)
    constructor(layoutResId: Int, data: MutableList<T>?) : super(layoutResId, data)
}