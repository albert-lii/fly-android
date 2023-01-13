package org.fly.uikit.loopview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi
import org.fly.uikit.R

/**
 * Banner的指示器
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/13 3:48 下午
 * @since: 1.0.0
 */
open class Indicator : View {
    // 活动指示器的颜色
    var activeColor: Int = Color.BLACK
        set(value) {
            field = value
            requestLayout()
        }

    // 非活动指示器的颜色
    var inactiveColor: Int = Color.LTGRAY
        set(value) {
            field = value
            requestLayout()
        }

    // 非活动指示器的半径缩放因子，相对于activeRadius
    @FloatRange(from = 0.0, to = 1.0)
    var inactiveScale: Float = 0.8f
        set(value) {
            field = value
            requestLayout()
        }

    // 指示器之间的间距
    var gap: Float = 10f
        set(value) {
            field = value
            requestLayout()
        }

    // 活动指示器的半径
    private var activeRadius: Float = 0f

    // 指示器的数量
    var totalCount = 6
        set(value) {
            field = value
            requestLayout()
        }

    // 当前被选中的指示器位置
    var selectedPosition = 0
        private set

    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.fly_uikit_Indicator)
            activeColor =
                typedArray.getColor(R.styleable.fly_uikit_Indicator_fu_activeColor, activeColor)
            inactiveColor = typedArray.getColor(
                R.styleable.fly_uikit_Indicator_fu_inactiveColor,
                inactiveColor
            )
            inactiveScale = typedArray.getFloat(
                R.styleable.fly_uikit_Indicator_fu_inactiveScale,
                inactiveScale
            )
            gap = typedArray.getDimension(R.styleable.fly_uikit_Indicator_fu_gap, gap)
            typedArray.recycle()
        }
        paint.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        activeRadius = height * 1f / 2
        val inactiveRadius = inactiveScale * activeRadius
        var totalWidth = (paddingLeft + paddingRight) * 1f
        if (totalCount == 1) {
            totalWidth += activeRadius * 2
        } else if (totalCount > 1) {
            totalWidth += activeRadius * 2 + (totalCount - 1) * (inactiveRadius * 2 + gap)
        }
        // 此处totalWidth向上取整，防止最后绘制时长度不够，显示不全
        val newWidthMeasureSpec =
            measureWidth(widthMeasureSpec, Math.ceil(totalWidth.toDouble()).toInt())
        super.onMeasure(newWidthMeasureSpec, heightMeasureSpec)
    }

    private fun measureWidth(measureSpace: Int, total: Int): Int {
        val size = MeasureSpec.getSize(measureSpace)
        val mode = MeasureSpec.getMode(measureSpace)
        return when (mode) {
            MeasureSpec.AT_MOST -> MeasureSpec.makeMeasureSpec(total, MeasureSpec.EXACTLY)
            MeasureSpec.EXACTLY -> MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
            else -> MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0 until totalCount) {
            drawIndicator(canvas, paint, i, selectedPosition)
        }
    }

    /**
     * 绘制指示器
     */
    open fun drawIndicator(canvas: Canvas?, paint: Paint, position: Int, selectedPosition: Int) {
        canvas?.let {
            paint.color = if (position == selectedPosition) {
                activeColor
            } else {
                inactiveColor
            }
            val inactiveWidth = activeRadius * 2 * inactiveScale
            var centerX = 0f
            if (position < selectedPosition) {
                centerX = position * inactiveWidth + inactiveWidth / 2 + position * gap
            } else if (position > selectedPosition) {
                centerX =
                    selectedPosition * inactiveWidth + activeRadius * 2 + (position - selectedPosition - 1) * inactiveWidth + inactiveWidth / 2 + position * gap
            } else {
                centerX = position * inactiveWidth + activeRadius + position * gap
            }
            val radius = if (position == selectedPosition) {
                activeRadius
            } else {
                inactiveWidth / 2
            }
            it.drawCircle(centerX + paddingLeft, activeRadius + paddingTop, radius, paint)
        }
    }

    /**
     * 指示器切换
     */
    fun transfer(position: Int) {
        if (selectedPosition != position) {
            selectedPosition = position
            postInvalidate()
        }
    }
}