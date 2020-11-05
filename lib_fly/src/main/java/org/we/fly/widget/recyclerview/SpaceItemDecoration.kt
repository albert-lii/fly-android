package org.we.fly.widget.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/4 10:12 PM
 * @description: -
 * @since: 1.0.0
 */
class SpaceItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutMgr = parent.layoutManager
        if (layoutMgr != null) {
            if (layoutMgr is LinearLayoutManager) {

            } else if (layoutMgr is GridLayoutManager) {

            }
        }
    }
}