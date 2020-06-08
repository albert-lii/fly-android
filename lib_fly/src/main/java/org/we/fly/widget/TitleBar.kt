package org.we.fly.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.we.fly.R
import org.we.fly.extensions.dpToPx

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/5 7:06 PM
 * @description: 标题栏控件
 * @since: 1.0.0
 */
class TitleBar : LinearLayout {
    private lateinit var leftFuncView: LeftFuncView
    private lateinit var rightFuncView: RightFuncView
    private lateinit var titleView: TextView
    private lateinit var dividerView: View

    private val DEF_TITLE_COLOR = Color.BLACK
    private val DEF_TEXT_COLOR = Color.BLACK
    private val DEF_DEVIDER_COLOR = Color.LTGRAY
    private val DEF_LEFT_PADDING = 14.dpToPx
    private val DEF_RIGHT_PADDING = 14.dpToPx
    private val DEF_TITLE_SIZE = 18.dpToPx
    private val DEF_TEXT_SIZE = 15.dpToPx
    private val DEF_TEXT_LEFT_MARGIN = 15.dpToPx
    private val DEF_ICON_HEIGHT = 24.dpToPx
    private val DEF_DIVIDER_HEIGHT = 1.dpToPx


    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        initView(context)
        initAttr(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initView(context)
        initAttr(attrs)
    }

    private fun initView(context: Context) {
        orientation = VERTICAL

        val container = FrameLayout(context)
        titleView = TextView(context)
        leftFuncView = LeftFuncView(context)
        rightFuncView = RightFuncView(context)
        dividerView = View(context)
        val titleLp = FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        titleLp.gravity = Gravity.CENTER
        container.addView(titleView, titleLp)
        val lfvLp = FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        lfvLp.gravity = Gravity.LEFT
        container.addView(leftFuncView, lfvLp)
        val rfvLp = FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        rfvLp.gravity = Gravity.RIGHT
        container.addView(rightFuncView, rfvLp)
        rightFuncView!!.visibility = View.GONE
        val lp =
            LayoutParams(LayoutParams.MATCH_PARENT, 0)
        lp.weight = 1f
        addView(container, lp)
        val dividerLp = LayoutParams(
            LayoutParams.MATCH_PARENT, DEF_DIVIDER_HEIGHT.toInt()
        )
        addView(dividerView, dividerLp)
    }

    private fun initAttr(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)

            val title = ta.getString(R.styleable.TitleBar_title)
            titleView.setText(title)

            val titleSize =
                ta.getDimension(R.styleable.TitleBar_titleSize, DEF_TITLE_SIZE.toFloat())
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)

            val titleColor = ta.getColor(R.styleable.TitleBar_titleColor, DEF_TITLE_COLOR)
            titleView.setTextColor(titleColor)

            val leftPadding =
                ta.getDimension(R.styleable.TitleBar_leftPadding, DEF_LEFT_PADDING.toFloat())
            leftFuncView.setPadding(leftPadding.toInt(), 0, 0, 0)

            val leftIcon = ta.getDrawable(R.styleable.TitleBar_leftIcon)
            leftFuncView.setIcon(leftIcon)

            val leftIconHeight =
                ta.getDimension(R.styleable.TitleBar_leftIconHight, DEF_ICON_HEIGHT.toFloat())
            leftFuncView.setIconHeight(leftIconHeight)

            val showLeftIcon = ta.getBoolean(R.styleable.TitleBar_showLeftIcon, true)
            leftFuncView.showIcon(showLeftIcon)

            val leftText = ta.getString(R.styleable.TitleBar_leftText)
            leftFuncView.setText(leftText)

            val leftTextSize =
                ta.getDimension(R.styleable.TitleBar_leftTextSize, DEF_TEXT_SIZE.toFloat())
            leftFuncView.setTextSize(leftTextSize)

            val leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor, DEF_TEXT_COLOR)
            leftFuncView.setTextColor(leftTextColor)

            val leftTextLeftMargin =
                ta.getDimension(
                    R.styleable.TitleBar_leftTextLeftMargin,
                    DEF_TEXT_LEFT_MARGIN.toFloat()
                )
            leftFuncView.setTextLeftMargin(leftTextLeftMargin)

            val showLeftText = ta.getBoolean(R.styleable.TitleBar_showLeftText, false)
            leftFuncView.showText(showLeftText)

            val rightPadding =
                ta.getDimension(R.styleable.TitleBar_rightPadding, DEF_RIGHT_PADDING.toFloat())
            rightFuncView.setPadding(0, 0, rightPadding.toInt(), 0)

            val rightIcon = ta.getDrawable(R.styleable.TitleBar_rightIcon)
            rightFuncView.setIcon(rightIcon)

            val rightIconHeight =
                ta.getDimension(R.styleable.TitleBar_rightIconHight, DEF_ICON_HEIGHT.toFloat())
            rightFuncView.setIconHeight(rightIconHeight)

            val showRightIcon = ta.getBoolean(R.styleable.TitleBar_showRightIcon, false)
            rightFuncView.showIcon(showRightIcon)

            val rightText = ta.getString(R.styleable.TitleBar_rightText)
            rightFuncView.setText(rightText)

            val rightTextSize =
                ta.getDimension(R.styleable.TitleBar_rightTextSize, DEF_TEXT_SIZE.toFloat())
            rightFuncView.setTextSize(rightTextSize)

            val rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor, DEF_TEXT_COLOR)
            rightFuncView.setTextColor(rightTextColor)

            val rightTextLeftMargin =
                ta.getDimension(
                    R.styleable.TitleBar_rightTextLeftMargin,
                    DEF_TEXT_LEFT_MARGIN.toFloat()
                )
            rightFuncView.setTextLeftMargin(rightTextLeftMargin)

            val showRightText = ta.getBoolean(R.styleable.TitleBar_showRightText, false)
            rightFuncView.showText(showRightText)

            val showRightFunc = ta.getBoolean(R.styleable.TitleBar_showRightFunc, true)
            showRightFunc(showRightFunc)

            val dividerHeight =
                ta.getDimension(R.styleable.TitleBar_dividerHeight, DEF_DIVIDER_HEIGHT.toFloat())
            val dividerLp = dividerView.layoutParams as LayoutParams
            dividerLp.height = dividerHeight.toInt()
            dividerView.layoutParams = dividerLp

            val dividerColor = ta.getColor(R.styleable.TitleBar_dividerColor, DEF_DEVIDER_COLOR)
            dividerView.setBackgroundColor(dividerColor)

            val showDivider = ta.getBoolean(R.styleable.TitleBar_showDivider, false)
            showDivider(showDivider)
            ta.recycle()
        }
    }

    fun leftClick(listener: OnClickListener?) {
        leftFuncView.setOnClickListener(listener)
    }

    fun rightClick(listener: OnClickListener?) {
        rightFuncView.setOnClickListener(listener)
    }

    fun showRightFunc(show: Boolean) {
        rightFuncView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showDivider(show: Boolean) {
        dividerView.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * TitleBar左边的功能控件
     */
    private open inner class LeftFuncView(context: Context?) :
        LinearLayout(context) {
        protected var iconView: ImageView
        protected var textView: TextView

        init {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            iconView = ImageView(context)
            textView = TextView(context)
            addView(iconView)
            addView(textView)
            iconView.adjustViewBounds = true
            iconView.scaleType = ImageView.ScaleType.CENTER_CROP
            textView.visibility = View.GONE
        }

        fun setIcon(icon: Drawable?) {
            iconView.setImageDrawable(icon)
        }

        fun setIconHeight(height: Float) {
            val lp = iconView.layoutParams as LayoutParams
            lp.width = LayoutParams.WRAP_CONTENT
            lp.height = height.toInt()
            iconView.layoutParams = lp
        }

        fun setTextLeftMargin(leftMargin: Float) {
            val lp = textView.layoutParams as LayoutParams
            lp.leftMargin = leftMargin.toInt()
            textView.layoutParams = lp
        }

        fun setText(text: String?) {
            textView.text = text
        }

        fun setTextSize(size: Float) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
        }

        fun setTextColor(color: Int) {
            textView.setTextColor(color)
        }

        fun showIcon(show: Boolean) {
            iconView.visibility = if (show) View.VISIBLE else View.GONE
        }

        fun showText(show: Boolean) {
            textView.visibility = if (show) View.VISIBLE else View.GONE
        }
    }

    /**
     * TitleBar右边的功能控件，使用继承方便以后扩展
     */
    private inner class RightFuncView(context: Context?) :
        LeftFuncView(context)
}