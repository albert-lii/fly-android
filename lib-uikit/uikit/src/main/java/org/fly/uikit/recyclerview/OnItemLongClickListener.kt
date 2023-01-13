package org.fly.uikit.recyclerview

import android.view.View

/**
 * item长按事件
 */
interface OnItemLongClickListener {
    fun onItemLongClick(v: View, position: Int): Boolean
}