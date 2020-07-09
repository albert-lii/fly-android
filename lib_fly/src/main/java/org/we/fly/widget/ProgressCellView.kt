package org.we.fly.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import org.we.fly.R

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/8 10:04 AM
 * @description: 进度格控件
 * @since: 1.0.0
 */
class ProgressCellView : View {
    private var primaryColor = Color.GRAY // 进度格的基础颜色
    private var accentColor = Color.RED // 进度格点亮后的颜色
    private var accentCount = 0  // 被点亮的进度格数
    private var totalCount = 4 // 进度格总数
    private var cellSpace = 8 // 进度格之间的间隔距离
    private var cellWidth = 10 // 进度格的宽度
    private var reverse = false // 是否反向绘制
    private var oritenation: Int = 0 // 0-横向 1-纵向

    private var paint = Paint()

    constructor(context: Context?) : super(context) {
        init(context!!, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressCellView)
            primaryColor = ta.getColor(R.styleable.ProgressCellView_pc_primaryColor, primaryColor)
            accentColor = ta.getInteger(R.styleable.ProgressCellView_pc_accentColor, accentColor)
            totalCount = ta.getColor(R.styleable.ProgressCellView_pc_totalCount, totalCount)
            accentCount = ta.getInteger(R.styleable.ProgressCellView_pc_accentCount, accentCount)
            cellWidth =
                ta.getDimensionPixelSize(R.styleable.ProgressCellView_pc_cellWidth, cellWidth)
            cellSpace =
                ta.getDimensionPixelSize(R.styleable.ProgressCellView_pc_cellSpace, cellSpace)
            reverse = ta.getBoolean(R.styleable.ProgressCellView_pc_reverse, reverse)
            oritenation = ta.getInteger(R.styleable.ProgressCellView_pc_oritenation, oritenation)
            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var w = MeasureSpec.getSize(widthMeasureSpec)
        var h = MeasureSpec.getSize(heightMeasureSpec)
        if (oritenation == 0) {
            w = paddingLeft + paddingRight
            for (i in 0 until totalCount) {
                w += cellWidth
                if (i < (totalCount - 1)) {
                    w += cellSpace
                }
            }
        } else {
            h = paddingTop + paddingBottom
            for (i in 0 until totalCount) {
                h += cellWidth
                if (i < (totalCount - 1)) {
                    h += cellSpace
                }
            }
        }
        setMeasuredDimension(w, h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (oritenation == 0) {
            horizontalDraw(canvas!!)
        } else {
            verticalDraw(canvas!!)
        }
    }

    /**
     * 横向绘制
     */
    private fun horizontalDraw(canvas: Canvas) {
        val bottom = (height - paddingBottom).toFloat()
        var boundary = accentCount
        if (reverse) {
            boundary = totalCount - accentCount
        }
        for (i in 0 until totalCount) {
            val left = paddingLeft + i * (cellWidth + cellSpace)
            val right = left + cellWidth
            if (i < boundary) {
                paint.setColor(accentColor)
            } else {
                paint.setColor(primaryColor)
            }
            canvas.drawRect(left.toFloat(), paddingTop.toFloat(), right.toFloat(), bottom, paint)
        }
    }

    /**
     * 纵向绘制
     */
    private fun verticalDraw(canvas: Canvas) {
        val right = (width - paddingRight).toFloat()
        var boundary = totalCount - accentCount
        if (reverse) {
            boundary = accentCount
        }
        for (i in 0 until totalCount) {
            val top = paddingTop + i * (cellWidth + cellSpace)
            val bottom = top + cellWidth
            if (i < boundary) {
                paint.setColor(primaryColor)
            } else {
                paint.setColor(accentColor)
            }
            canvas.drawRect(paddingLeft.toFloat(), top.toFloat(), right, bottom.toFloat(), paint)
        }
    }

    fun setTotalCount(count: Int) {
        this.totalCount = count
        invalidate()
    }

    fun setAccentCount(count: Int) {
        this.accentCount = count
        invalidate()
    }

    fun setPrimaryColor(color: Int) {
        this.primaryColor = color
        invalidate()
    }

    fun setAccentColor(color: Int) {
        this.accentCount = color
        invalidate()
    }

    fun setCellWidth(width: Int) {
        this.cellWidth = width
        invalidate()
    }

    fun setCellSpace(space: Int) {
        this.cellSpace = space
        invalidate()
    }

    fun setReverse(reverse: Boolean) {
        this.reverse = reverse
        invalidate()
    }
}