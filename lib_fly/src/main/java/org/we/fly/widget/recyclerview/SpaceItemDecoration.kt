package org.we.fly.widget.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/11/4 10:12 PM
 * @description: RecyclerView分割线
 * @since: 1.0.0
 */
class SpaceItemDecoration(
    private val space: Int? = null, // Linear形态时，item之间的间距
    private val gridH: Int? = null, // Grid形态时，列与列之间的间距
    private val gridV: Int? = null // Grid形态时，行与行之间的间距
) : RecyclerView.ItemDecoration() {
    private var fullPosition = -1

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val childCount = parent.adapter?.itemCount ?: 0
        val position = parent.getChildAdapterPosition(view)
        val layoutMgr = parent.layoutManager
        if (layoutMgr != null) {
            if (layoutMgr is GridLayoutManager) {
                // 网格状态
                setItemOffsetsByGrid(outRect, layoutMgr, position, childCount)
            } else if (layoutMgr is LinearLayoutManager) {
                // 线性状态
                setItemOffsetsByLinear(outRect, layoutMgr, position, childCount)
            } else if (layoutMgr is StaggeredGridLayoutManager) {
                // 瀑布流状态
                setItemOffsetsByStaggerGrid(view, outRect, layoutMgr, position, childCount)
            }
        }
    }

    private fun setItemOffsetsByGrid(
        outRect: Rect,
        layoutMgr: GridLayoutManager,
        position: Int,
        childCount: Int
    ) {
        val sc = layoutMgr.spanCount
        val h = gridH ?: (space ?: 0) // 列与列之间的间距
        val v = gridV ?: (space ?: 0) // 行与行之间的间距
        if (layoutMgr.orientation == GridLayoutManager.VERTICAL) {
            // 纵向网格
            val totalRow = childCount / sc + 1 // 总行数
            val itemAvgH = h * (sc - 1) / sc // 平均每个item应该持有的行与行的间距
            val itemAvgV = v * (totalRow - 1) / totalRow // 平均每个item应该持有的列与列的间距
            val curRow = position / sc // item处于第几行（从0开始）
            val curCol = position % sc // item处于第几列（从0开始）
            val left = curCol * (h - itemAvgH)
            val right = itemAvgH - left
            val top = curRow * (v - itemAvgV)
            val bottom = itemAvgV - top
            outRect.set(left, top, right, bottom)
        } else {
            // 横向网格
            val totalCol = childCount / sc + 1 // 总列数
            val itemAvgH = h * (totalCol - 1) / totalCol // 平均每个item应该持有的行与行的间距
            val itemAvgV = v * (sc - 1) / sc // 平均每个item应该持有的列与列的间距
            val curRow = position % sc // item处于第几行（从0开始）
            val curCol = position / sc // item处于第几列（从0开始）
            val left = curCol * (h - itemAvgH)
            val right = itemAvgH - left
            val top = curRow * (v - itemAvgV)
            val bottom = itemAvgV - top
            outRect.set(left, top, right, bottom)
        }
    }

    private fun setItemOffsetsByLinear(
        outRect: Rect,
        layoutMgr: LinearLayoutManager,
        position: Int,
        childCount: Int
    ) {
        val s = space ?: 0
        val itemAvgSpace = s * (childCount - 1) / childCount // 平均每个item应该持有的间距
        if (layoutMgr.orientation == LinearLayoutManager.VERTICAL) {
            // 线性纵向列表
            val top = position * (s - itemAvgSpace)
            val bottom = itemAvgSpace - top
            outRect.top = top
            outRect.bottom = bottom
        } else {
            // 线性横向列表
            val left = position * (s - itemAvgSpace)
            val right = itemAvgSpace - left
            outRect.left = left
            outRect.right = right
        }
    }

    private fun setItemOffsetsByStaggerGrid(
        view: View,
        outRect: Rect,
        layoutMgr: StaggeredGridLayoutManager,
        position: Int,
        childCount: Int
    ) {
        val sc = layoutMgr.spanCount
        val h = gridH ?: (space ?: 0) // 列与列之间的间距
        val v = gridV ?: (space ?: 0) // 行与行之间的间距
        val params = view.getLayoutParams() as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = params.spanIndex // 当前item在第几列（从0计算）
        if (layoutMgr.orientation == StaggeredGridLayoutManager.VERTICAL) {
            // 纵向瀑布流
            val itemAvgH = h * (sc - 1) / sc // 平均每个item应该持有的行与行的间距
            if (params.isFullSpan) {
                // item是否占满一行
                outRect.left = 0
                outRect.right = 0
            } else {
                outRect.left = spanIndex * (h - itemAvgH)
                outRect.right = itemAvgH - outRect.left
            }
            if (position < sc && params.isFullSpan) {
                // 找到头部第一个整行的position
                fullPosition = position
            }
            if (position < sc && (fullPosition == -1 || position < fullPosition)) {
                outRect.top = 0
            } else {
                outRect.top = v
            }
        } else {
            // 横向瀑布流
            val itemAvgV = v * (sc - 1) / sc // 平均每个item应该持有的列与列的间距
            if (params.isFullSpan) {
                // item是否占满一列
                outRect.top = 0
                outRect.bottom = 0
            } else {
                outRect.top = spanIndex * (v - itemAvgV)
                outRect.bottom = itemAvgV - outRect.top
            }
            if (position < sc && params.isFullSpan) {
                // 找到头部第一个整列的position
                fullPosition = position
            }
            if (position < sc && (fullPosition == -1 || position < fullPosition)) {
                outRect.left = 0
            } else {
                outRect.left = h
            }
        }
    }
}