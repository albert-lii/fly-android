package org.fly.uikit.recyclerview

import android.view.View

/**
 * item点击事件
 */
interface OnItemClickListener {
    fun onItemClick(v: View, position: Int)
}