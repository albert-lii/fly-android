package org.we.fly.widget.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/3 7:38 PM
 * @description: RecyclerView的 Grid 形态的 item 之间的间距
 * @since: 1.0.0
 */
class GridItemDecoration(
    private val spanCount: Int, // 一行的 item 的个数
    private val horizontalSpace: Int, // item 之间的横向间距
    private val verticalSpace: Int // item 之间的纵向间距
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childCount = parent.adapter?.itemCount ?: 0
        val position = parent.getChildAdapterPosition(view)

        /**
         * 处理列与列之间的间距
         */
        val avgHorizontalSpace: Int = (spanCount - 1) * horizontalSpace / spanCount // 每列应该持有的间距
        val column = position % spanCount // 当前 child 处于第几列
        if (column == 0) {
            outRect.left = 0
            outRect.right = avgHorizontalSpace
        } else if (column == (spanCount - 1)) {
            outRect.left = avgHorizontalSpace
            outRect.right = 0
        } else {
            val half = avgHorizontalSpace / 2
            outRect.left = half
            outRect.right = half
        }

        /**
         * 处理行与行之间的间距
         */
        val totalRow = childCount / spanCount + 1
        val avgVerticalSpace = (totalRow - 1) * verticalSpace / totalRow // 每行应该持有的间距
        val row = position / spanCount // 当前 child 处于第几行
        if (row == 0) {
            outRect.top = 0
            outRect.bottom = avgVerticalSpace
        } else if (row == totalRow) {
            outRect.top = avgVerticalSpace
            outRect.bottom = 0
        } else {
            val half = avgVerticalSpace / 2
            outRect.top = half
            outRect.bottom = half
        }
    }
}