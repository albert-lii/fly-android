package org.we.fly.widget.chart

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import org.we.fly.R


/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/1/12 7:34 PM
 * @description: 曲线图
 * @since: 1.0.0
 */
class CurveChart : View {
    /**
     * X轴相关
     */
    var xAxisWidth = 5f // X轴的线宽
    var xAxisColor = Color.BLACK // X轴坐标颜色
    var xAxisTextColor = Color.BLUE // X轴刻度文字的颜色
    var xAxisTextSize = 35f // X轴刻度文字的大小
    var xAxisTextRowTopMargin = 10f // X轴刻度文字整行文字距离原点的上间距
    var xAxisTextRowLeftMargin = 0f // X轴刻度文字整行文字距离原点的左间距
    var xAxisTextRowRightMargin = 0f // X轴刻度文字整行文字距离X轴尾端的右间距
    var isXFirstPointAtOrigin = false // X轴的第一个刻度点是否在原点上
    var isXLastPointAtXEnd = false // X轴的最后一个刻度点是否在X轴的尾端

    // X轴刻度文字的第一个刻度的文字是否相对刻度居中（仅当isXFirstPointAtOrigin为true时有效）
    var isXAxisFirstTextCenter = false

    // X轴刻度文字的最后一个刻度的文字是否相对刻度居中（仅当isXLastPointAtXEnd为true时有效）
    var isXAxisLastTextCenter = false

    /**
     * Y轴相关
     */
    var yAxisWidth = 5f // Y轴的线宽
    var yAxisColor = Color.BLACK // Y轴坐标颜色
    var yAxisTextColor = Color.BLUE // Y轴刻度文字的颜色
    var yAxisTextSize = 35f // Y轴刻度文字的大小
    var yAxisTextColTopMargin = 0f // Y轴刻度文字整列文字距离View顶部的上间距
    var yAxisTextColRightMargin = 10f // Y轴刻度文字整列文字距离原点的右间距
    var isYLastPointAtEnd = false // Y轴的最后一个刻度点是否在Y轴的末端
    var isYLastTextCenter = false // Y轴最后一个刻度点的刻度文字是否相对刻度点居中（仅当isYLastPointAtEnd为true时有效）
    var showYAxisLine = true // 是否显示Y轴线

    /**
     * 曲线相关
     */
    var curveColor = Color.RED // 曲线颜色
    var curveWidth = 5f // 曲线的线宽

    /**
     * 刻度线相关
     */
    var xMarkLineColor = Color.GREEN // X轴刻度线颜色
    var xMarkLineWidth = 3f // X轴刻度线粗细
    var showXMarkLine = false // 显示X轴刻度线
    var yMarkLineColor = Color.MAGENTA // Y轴刻度线颜色
    var yMarkLineWidth = 3f // Y轴刻度线粗细
    var showYMarkLine = false // 显示Y轴刻度线

    /**
     * 外部传入数据
     */
    var xAxisData = ArrayList<String>() // X轴上的刻度文字
    var yAxisData = ArrayList<String>() // Y轴上的刻度文字
    var dataPoints = ArrayList<PointF>() // 原始数据点

    private lateinit var curvePaint: Paint // 曲线画笔
    private lateinit var axisPaint: Paint // 坐标轴画笔
    private lateinit var axisTextPaint: Paint // 坐标轴文字画笔
    private lateinit var markLinePaint: Paint // 刻度线画笔
    private var originPoint = PointF(0f, 0f) // 原点坐标
    private var xAxisLineLength = 0f // X轴的长度
    private var yAxisLineLength = 0f // Y轴的长度
    private var xAxisPoints = ArrayList<Float>() // X轴上的刻度点
    private var yAxisPoints = ArrayList<Float>() // Y轴上的刻度点
    private var realPoints = ArrayList<PointF>() // 处理后的数据点

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
        initAttr(context, attrs)
        initPaint()
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_CurveChart)
            xAxisColor = ta.getColor(R.styleable.fly_CurveChart_cc_xAxisColor, xAxisColor)
            xAxisWidth = ta.getDimension(R.styleable.fly_CurveChart_cc_xAxisWidth, xAxisWidth)
            xAxisTextColor =
                ta.getColor(R.styleable.fly_CurveChart_cc_xAxisTextColor, xAxisTextColor)
            xAxisTextSize =
                ta.getDimension(R.styleable.fly_CurveChart_cc_xAxisTextSize, xAxisTextSize)
            xAxisTextRowLeftMargin = ta.getDimension(
                R.styleable.fly_CurveChart_cc_xAxisTextRowLeftMargin,
                xAxisTextRowLeftMargin
            )
            xAxisTextRowRightMargin = ta.getDimension(
                R.styleable.fly_CurveChart_cc_xAxisTextRowRightMargin,
                xAxisTextRowRightMargin
            )
            xAxisTextRowTopMargin = ta.getDimension(
                R.styleable.fly_CurveChart_cc_xAxisTextRowTopMargin,
                xAxisTextRowTopMargin
            )
            yAxisColor = ta.getColor(R.styleable.fly_CurveChart_cc_yAxisColor, yAxisColor)
            yAxisWidth = ta.getDimension(R.styleable.fly_CurveChart_cc_yAxisWidth, yAxisWidth)
            yAxisTextColor =
                ta.getColor(R.styleable.fly_CurveChart_cc_yAxisTextColor, yAxisTextColor)
            yAxisTextSize =
                ta.getDimension(R.styleable.fly_CurveChart_cc_yAxisTextSize, yAxisTextSize)
            yAxisTextColRightMargin = ta.getDimension(
                R.styleable.fly_CurveChart_cc_yAxisTextColRightMargin,
                yAxisTextColRightMargin
            )
            yAxisTextColTopMargin = ta.getDimension(
                R.styleable.fly_CurveChart_cc_yAxisTextColTopMargin,
                yAxisTextColTopMargin
            )
            showYAxisLine = ta.getBoolean(R.styleable.fly_CurveChart_cc_showYLine, showYAxisLine)
            xMarkLineColor =
                ta.getColor(R.styleable.fly_CurveChart_cc_xMarkLineColor, xMarkLineColor)
            xMarkLineWidth =
                ta.getDimension(R.styleable.fly_CurveChart_cc_xMarkLineWidth, xMarkLineWidth)
            yMarkLineColor =
                ta.getColor(R.styleable.fly_CurveChart_cc_yMarkLineColor, yMarkLineColor)
            yMarkLineWidth =
                ta.getDimension(R.styleable.fly_CurveChart_cc_yMarkLineWidth, yMarkLineWidth)
            showXMarkLine =
                ta.getBoolean(R.styleable.fly_CurveChart_cc_showXMarkLine, showXMarkLine)
            showYMarkLine =
                ta.getBoolean(R.styleable.fly_CurveChart_cc_showYMarkLine, showYMarkLine)
            curveColor = ta.getColor(R.styleable.fly_CurveChart_cc_curveColor, curveColor)
            curveWidth = ta.getDimension(R.styleable.fly_CurveChart_cc_curveWidth, curveWidth)
            ta.recycle()
        }
    }

    private fun initPaint() {
        // 坐标轴画笔
        axisPaint = Paint().also {
            it.style = Paint.Style.STROKE
            it.color = xAxisColor
            it.strokeWidth = xAxisWidth
            it.isAntiAlias = true
        }
        // 坐标轴文字画笔
        axisTextPaint = Paint().also {
            it.style = Paint.Style.FILL
            it.color = xAxisColor
            it.isAntiAlias = true
            it.textSize = xAxisTextSize
        }
        // 曲线画笔
        curvePaint = Paint().also {
            it.style = Paint.Style.STROKE
            it.color = curveColor
            it.strokeWidth = curveWidth
            it.isAntiAlias = true
        }
        // 刻度线画笔
        markLinePaint = Paint().also {
            it.style = Paint.Style.STROKE
            it.color = xMarkLineColor
            it.strokeWidth = xMarkLineWidth
            it.isAntiAlias = true
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (xAxisData.isNotEmpty() && yAxisData.isNotEmpty()) {
            calcChartSize()
        }
    }

    /**
     * 计算坐标轴的长度、原点的位置、X轴的刻度点等数据
     */
    private fun calcChartSize() {
        axisTextPaint.textSize = xAxisTextSize
        val xFirstTextRect = Rect()
        axisTextPaint.getTextBounds(xAxisData[0], 0, xAxisData[0].length, xFirstTextRect)
        val xLastTextRect = Rect()
        axisTextPaint.getTextBounds(
            xAxisData[xAxisData.size - 1],
            0,
            xAxisData[xAxisData.size - 1].length,
            xLastTextRect
        )
        var maxYtw = 0f
        for (yText in yAxisData) {
            val rect = Rect()
            axisTextPaint.getTextBounds(yText, 0, yText.length, rect)
            if (rect.width() > maxYtw) {
                maxYtw = rect.width().toFloat()
            }
        }
        val ySpace = maxYtw + yAxisTextColRightMargin + yAxisWidth
        originPoint.y =
            height.toFloat() - xFirstTextRect.height() - xAxisTextRowTopMargin - xAxisWidth
        if (isXFirstPointAtOrigin) {
            if (isXAxisFirstTextCenter) {
                originPoint.x = Math.max(xFirstTextRect.width() / 2f, ySpace)
            } else {
                originPoint.x = ySpace
            }
        } else {
            originPoint.x = ySpace
        }
        if (isXLastPointAtXEnd) {
            if (isXAxisLastTextCenter) {
                xAxisLineLength = width - originPoint.x - xLastTextRect.width() / 2
            } else {
                xAxisLineLength = width - originPoint.x
            }
        } else {
            xAxisLineLength = width - originPoint.x
        }
        /**
         *  计算X轴的刻度点
         */
        // X轴的第一个刻度点所在位置
        val xFirstPoint = if (isXFirstPointAtOrigin) {
            originPoint.x
        } else {
            originPoint.x + xFirstTextRect.width() / 2 + xAxisTextRowLeftMargin
        }
        // X轴的最后一个刻度点所在位置
        val xLastPoint = if (isXLastPointAtXEnd) {
            originPoint.x + xAxisLineLength - xAxisTextRowRightMargin
        } else {
            originPoint.x + xAxisLineLength - xLastTextRect.width() / 2 - xAxisTextRowRightMargin
        }
        val totalXDis = xLastPoint - xFirstPoint
        // 相邻刻度点之间的间距
        val avgXDis = totalXDis / (xAxisData.size - 1)
        xAxisPoints.clear()
        for (i in 0 until xAxisData.size) {
            if (i == 0) {
                xAxisPoints.add(xFirstPoint)
            } else if (i == (xAxisData.size - 1)) {
                xAxisPoints.add(xLastPoint)
            } else {
                xAxisPoints.add(xAxisPoints[i - 1] + avgXDis)
            }
        }
        /**
         * 计算Y轴上的刻度点所在位置
         */
        axisTextPaint.textSize = yAxisTextSize
        val yLastTextRect = Rect()
        axisTextPaint.getTextBounds(
            yAxisData[yAxisData.size - 1],
            0,
            yAxisData[yAxisData.size - 1].length,
            yLastTextRect
        )
        if (isYLastPointAtEnd) {
            if (isYLastTextCenter) {
                yAxisLineLength = originPoint.y + xAxisWidth - yLastTextRect.height() / 2f
            } else {
                yAxisLineLength = originPoint.y + xAxisWidth
            }
        } else {
            yAxisLineLength = originPoint.y + xAxisWidth
        }
        val yLastPoint = if (isYLastPointAtEnd) {
            originPoint.y + xAxisWidth - yAxisLineLength + yAxisTextColTopMargin
        } else {
            yLastTextRect.height() / 2f + yAxisTextColTopMargin
        }
        val totalYDis = originPoint.y - yLastPoint
        val avgYDis = totalYDis / (yAxisData.size - 1)
        yAxisPoints.clear()
        for (i in 0 until yAxisData.size) {
            if (i == 0) {
                yAxisPoints.add(originPoint.y)
            } else if (i == (yAxisData.size - 1)) {
                yAxisPoints.add(yLastPoint)
            } else {
                yAxisPoints.add(yAxisPoints[i - 1] - avgYDis)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null || xAxisData.isNullOrEmpty() || xAxisData.isNullOrEmpty()) {
            return
        }
        drawAxisX(canvas)
        drawAxisY(canvas)
        drawMarkLine(canvas)
        drawCurve(canvas)
    }

    /**
     * 绘制X轴
     */
    private fun drawAxisX(canvas: Canvas) {
        /**
         * 绘制X轴的线
         */
        val path = Path()
        // 移动画笔到指定位置，即设置Path的起始点
        path.moveTo(originPoint.x, originPoint.y + xAxisWidth / 2)
        path.lineTo(originPoint.x + xAxisLineLength, originPoint.y + xAxisWidth / 2)
        axisPaint.color = xAxisColor
        axisPaint.strokeWidth = xAxisWidth
        canvas.drawPath(path, axisPaint)
        /**
         * 绘制X轴的刻度文字
         */
        axisTextPaint.color = xAxisTextColor
        axisTextPaint.textSize = xAxisTextSize
        for (i in 0 until xAxisPoints.size) {
            // 获取文字所在矩形的宽高
            val text = xAxisData[i]
            val rect = Rect()
            axisTextPaint.getTextBounds(text, 0, text.length, rect)
            val w = rect.width()
            val h = rect.height()
            val textX = if (i == 0) {
                if (isXFirstPointAtOrigin) {
                    if (isXAxisFirstTextCenter) {
                        xAxisPoints[i] - w / 2
                    } else {
                        xAxisPoints[i]
                    }
                } else {
                    xAxisPoints[i] - w / 2
                }
            } else if (i == (xAxisPoints.size - 1)) {
                if (isXLastPointAtXEnd) {
                    if (isXAxisLastTextCenter) {
                        xAxisPoints[i] - w / 2
                    } else {
                        xAxisPoints[i] - w
                    }
                } else {
                    xAxisPoints[i] - w / 2
                }
            } else {
                xAxisPoints[i] - w / 2
            }
            val textY = originPoint.y + xAxisWidth + h + xAxisTextRowTopMargin
            canvas.drawText(text, textX, textY, axisTextPaint)
        }
    }

    /**
     * 绘制Y轴
     */
    private fun drawAxisY(canvas: Canvas) {
        /**
         * 绘制Y轴的线
         */
        if (showYAxisLine) {
            val path = Path()
            path.moveTo(originPoint.x - yAxisWidth / 2, originPoint.y + xAxisWidth)
            path.lineTo(
                originPoint.x - yAxisWidth / 2,
                originPoint.y + xAxisWidth - yAxisLineLength
            )
            axisPaint.color = yAxisColor
            axisPaint.strokeWidth = yAxisWidth
            canvas.drawPath(path, axisPaint)
        }
        /**
         * 绘制Y轴上的刻度文字
         */
        axisTextPaint.color = yAxisTextColor
        axisTextPaint.textSize = yAxisTextSize
        for (i in 0 until yAxisPoints.size) {
            // 获取文字所在矩形的宽高
            val text = yAxisData[i]
            val rect = Rect()
            axisTextPaint.getTextBounds(text, 0, text.length, rect)
            val w = rect.width()
            val h = rect.height()
            val textX = originPoint.x - yAxisWidth - yAxisTextColRightMargin - w
            val textY = if (i == (yAxisPoints.size - 1)) {
                if (isYLastPointAtEnd) {
                    if (isYLastTextCenter) {
                        yAxisPoints[i] + h / 2
                    } else {
                        yAxisPoints[i] + h
                    }
                } else {
                    yAxisPoints[i] + h / 2
                }
            } else {
                yAxisPoints[i] + h / 2
            }
            canvas.drawText(text, textX, textY, axisTextPaint)
        }
    }

    /**
     * 绘制刻度线
     */
    private fun drawMarkLine(canvas: Canvas) {
        // 绘制Y轴的刻度线
        if (showYMarkLine) {
            val yMarkLineX = originPoint.x + xAxisLineLength
            markLinePaint.color = yMarkLineColor
            markLinePaint.strokeWidth = yMarkLineWidth
            for (i in 0 until yAxisPoints.size) {
                if (i != 0) {
                    canvas.drawLine(
                        originPoint.x,
                        yAxisPoints[i],
                        yMarkLineX,
                        yAxisPoints[i],
                        markLinePaint
                    )
                }
            }
        }
        if (showXMarkLine) {
            // 绘制X轴的刻度线
            val xMarkLineY = originPoint.y + xAxisWidth - yAxisLineLength
            markLinePaint.color = xMarkLineColor
            markLinePaint.strokeWidth = xMarkLineWidth
            for (i in 0 until xAxisPoints.size) {
                if (i == 0) {
                    if (isXFirstPointAtOrigin) {
                        continue
                    } else {
                        canvas.drawLine(
                            xAxisPoints[i],
                            originPoint.y,
                            xAxisPoints[i],
                            xMarkLineY,
                            markLinePaint
                        )
                    }
                } else {
                    canvas.drawLine(
                        xAxisPoints[i],
                        originPoint.y,
                        xAxisPoints[i],
                        xMarkLineY,
                        markLinePaint
                    )
                }
            }
        }
    }

    /**
     * 绘制曲线
     */
    private fun drawCurve(canvas: Canvas) {
        realPoints.clear()
        for (item in dataPoints) {
            realPoints.add(PointF(xAxisPoints[0] + item.x, yAxisPoints[0] - item.y))
        }
        realPoints.let {
            var startP: PointF
            var endP: PointF
            val path = Path()
            for (i in 0 until it.size - 1) {
                startP = it[i]
                endP = it[i + 1]
                path.moveTo(startP.x, startP.y)
                // 贝塞尔曲线的控制点
                val bx = (startP.x + endP.x) / 2f
                val cp1 = PointF(bx, startP.y)
                val cp2 = PointF(bx, endP.y)
                // 绘制三阶贝塞尔曲线
                path.cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, endP.x, endP.y)
                canvas.drawPath(path, curvePaint)
            }
        }
    }

    /**
     * 构建曲线图
     */
    fun build() {
        invalidate()
    }
}