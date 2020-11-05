package org.we.fly.widget.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/3 7:41 PM
 * @description: RecyclerView的 Linear 形态的 item 之间的间距
 * @since: 1.0.0
 */
class LinearItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childCount = parent.adapter?.itemCount ?: 0
        val position = parent.getChildAdapterPosition(view)
        val orientation = (parent.layoutManager as LinearLayoutManager).orientation
        val avgSpace = space * (childCount - 1) / childCount // 每个item应该持有的间距
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            // 横向列表
            if (position == 0) {
                outRect.left = 0
                outRect.right = avgSpace
            } else if (position < (childCount - 1)) {
                val half = avgSpace / 2
                outRect.left = half
                outRect.right = half
            } else {
                outRect.left = avgSpace
                outRect.right = 0
            }
        } else if (orientation == LinearLayoutManager.VERTICAL) {
            // 纵向列表
            if (position == 0) {
                outRect.top = 0
                outRect.bottom = avgSpace
            } else if (position < (childCount - 1)) {
                val half = avgSpace / 2
                outRect.top = half
                outRect.bottom = half
            } else {
                outRect.top = avgSpace
                outRect.bottom = 0
            }
        }
    }
}