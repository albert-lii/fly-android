package org.fly.widget

import android.app.Activity
import android.content.Context
import android.content.res.Resources
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
import org.fly.base.extensions.dpToPx
import org.fly.base.extensions.singleClick

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/5 7:06 PM
 * @description: 标题栏控件
 * @since: 1.0.0
 */
class TitleBar : LinearLayout {
    private lateinit var containerGroup: FrameLayout
    private lateinit var leftFuncView: FuncView
    private lateinit var rightFuncView: FuncView
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

    private var addStatusBarHeight = false

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

        containerGroup = FrameLayout(context)
        titleView = TextView(context)
        leftFuncView = FuncView(context)
        rightFuncView = FuncView(context)
        dividerView = View(context)
        val titleLp = FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        titleLp.gravity = Gravity.CENTER
        containerGroup.addView(titleView, titleLp)
        val lfvLp = FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        lfvLp.gravity = Gravity.LEFT
        containerGroup.addView(leftFuncView, lfvLp)
        val rfvLp = FrameLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT
        )
        rfvLp.gravity = Gravity.RIGHT
        containerGroup.addView(rightFuncView, rfvLp)
        rightFuncView.visibility = View.GONE
        val clp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(containerGroup, clp)
        val dividerLp = LayoutParams(
            LayoutParams.MATCH_PARENT, DEF_DIVIDER_HEIGHT.toInt()
        )
        addView(dividerView, dividerLp)
        leftFuncView.singleClick {
            if (context is Activity) {
                context.finish()
            }
        }
    }

    private fun initAttr(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_TitleBar)

            val title = ta.getString(R.styleable.fly_TitleBar_tb_title)
            titleView.setText(title)

            val titleSize =
                ta.getDimension(R.styleable.fly_TitleBar_tb_titleSize, DEF_TITLE_SIZE.toFloat())
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)

            val titleColor = ta.getColor(R.styleable.fly_TitleBar_tb_titleColor, DEF_TITLE_COLOR)
            titleView.setTextColor(titleColor)

            val titleBold = ta.getBoolean(R.styleable.fly_TitleBar_tb_titleBold, false)
            if (titleBold) {
                titleView.paint.isFakeBoldText = true
            }

            val leftPadding =
                ta.getDimension(R.styleable.fly_TitleBar_tb_leftPadding, DEF_LEFT_PADDING.toFloat())
            leftFuncView.setPadding(leftPadding.toInt(), 0, 0, 0)

            val leftIcon = ta.getDrawable(R.styleable.fly_TitleBar_tb_leftIcon)
            leftFuncView.setIcon(leftIcon)

            val leftIconHeight =
                ta.getDimension(R.styleable.fly_TitleBar_tb_leftIconHight, DEF_ICON_HEIGHT.toFloat())
            leftFuncView.setIconHeight(leftIconHeight)

            val showLeftIcon = ta.getBoolean(R.styleable.fly_TitleBar_tb_showLeftIcon, true)
            leftFuncView.showIcon(showLeftIcon)

            val leftText = ta.getString(R.styleable.fly_TitleBar_tb_leftText)
            leftFuncView.setText(leftText)

            val leftTextSize =
                ta.getDimension(R.styleable.fly_TitleBar_tb_leftTextSize, DEF_TEXT_SIZE.toFloat())
            leftFuncView.setTextSize(leftTextSize)

            val leftTextColor = ta.getColor(R.styleable.fly_TitleBar_tb_leftTextColor, DEF_TEXT_COLOR)
            leftFuncView.setTextColor(leftTextColor)

            val leftTextLeftMargin =
                ta.getDimension(
                    R.styleable.fly_TitleBar_tb_leftTextLeftMargin,
                    DEF_TEXT_LEFT_MARGIN.toFloat()
                )
            leftFuncView.setTextLeftMargin(leftTextLeftMargin)

            val showLeftText = ta.getBoolean(R.styleable.fly_TitleBar_tb_showLeftText, false)
            leftFuncView.showText(showLeftText)

            val showLeftFunc = ta.getBoolean(R.styleable.fly_TitleBar_tb_showLeftFunc, true)
            showLeftFunc(showLeftFunc)

            val rightPadding =
                ta.getDimension(R.styleable.fly_TitleBar_tb_rightPadding, DEF_RIGHT_PADDING.toFloat())
            rightFuncView.setPadding(0, 0, rightPadding.toInt(), 0)

            val rightIcon = ta.getDrawable(R.styleable.fly_TitleBar_tb_rightIcon)
            rightFuncView.setIcon(rightIcon)

            val rightIconHeight =
                ta.getDimension(R.styleable.fly_TitleBar_tb_rightIconHight, DEF_ICON_HEIGHT.toFloat())
            rightFuncView.setIconHeight(rightIconHeight)

            val showRightIcon = ta.getBoolean(R.styleable.fly_TitleBar_tb_showRightIcon, false)
            rightFuncView.showIcon(showRightIcon)

            val rightText = ta.getString(R.styleable.fly_TitleBar_tb_rightText)
            rightFuncView.setText(rightText)

            val rightTextSize =
                ta.getDimension(R.styleable.fly_TitleBar_tb_rightTextSize, DEF_TEXT_SIZE.toFloat())
            rightFuncView.setTextSize(rightTextSize)

            val rightTextColor = ta.getColor(R.styleable.fly_TitleBar_tb_rightTextColor, DEF_TEXT_COLOR)
            rightFuncView.setTextColor(rightTextColor)

            val rightTextLeftMargin =
                ta.getDimension(
                    R.styleable.fly_TitleBar_tb_rightTextLeftMargin,
                    DEF_TEXT_LEFT_MARGIN.toFloat()
                )
            rightFuncView.setTextLeftMargin(rightTextLeftMargin)

            val showRightText = ta.getBoolean(R.styleable.fly_TitleBar_tb_showRightText, false)
            rightFuncView.showText(showRightText)

            val showRightFunc = ta.getBoolean(R.styleable.fly_TitleBar_tb_showRightFunc, true)
            showRightFunc(showRightFunc)

            val dividerHeight =
                ta.getDimension(R.styleable.fly_TitleBar_tb_dividerHeight, DEF_DIVIDER_HEIGHT.toFloat())
            val dividerLp = dividerView.layoutParams as LayoutParams
            dividerLp.height = dividerHeight.toInt()
            dividerView.layoutParams = dividerLp

            val dividerColor = ta.getColor(R.styleable.fly_TitleBar_tb_dividerColor, DEF_DEVIDER_COLOR)
            dividerView.setBackgroundColor(dividerColor)

            val showDivider = ta.getBoolean(R.styleable.fly_TitleBar_tb_showDivider, false)
            showDivider(showDivider)

            val barHeight = ta.getDimension(R.styleable.fly_TitleBar_tb_barHeight, -1f)
            if (barHeight >= 0) {
                containerGroup.layoutParams.height = barHeight.toInt()
            }

            addStatusBarHeight = ta.getBoolean(R.styleable.fly_TitleBar_tb_addStatusBarHeight, false)
            if (addStatusBarHeight) {
                setPadding(paddingLeft, getStatusBarHeight(), paddingRight, paddingBottom)
            }
            ta.recycle()
        }
    }

    fun setTitle(title: String?) {
        titleView.setText(title)
    }

    fun getTitle(): String {
        return titleView.text.toString()
    }

    fun setTitleSize(size: Float) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun setTitleColor(color: Int) {
        titleView.setTextColor(color)
    }

    fun setLeftClick(listener: OnClickListener?) {
        leftFuncView.setOnClickListener(listener)
    }

    fun setRightClick(listener: OnClickListener?) {
        rightFuncView.setOnClickListener(listener)
    }

    fun setLeftIcon(icon: Drawable?) {
        leftFuncView.setIcon(icon)
    }

    fun setRightIcon(icon: Drawable?) {
        rightFuncView.setIcon(icon)
    }

    fun setLeftText(text: String?) {
        leftFuncView.setText(text)
    }

    fun setRightText(text: String?) {
        rightFuncView.setText(text)
    }

    fun setLeftTextColor(color: Int) {
        leftFuncView.setTextColor(color)
    }

    fun setRightTextColor(color: Int) {
        rightFuncView.setTextColor(color)
    }

    fun setLeftTextSize(size: Float) {
        leftFuncView.setTextSize(size)
    }

    fun setRightTextSize(size: Float) {
        rightFuncView.setTextSize(size)
    }

    fun showLeftFunc(show: Boolean) {
        leftFuncView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showRightFunc(show: Boolean) {
        rightFuncView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showDivider(show: Boolean) {
        dividerView.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * TitleBar的内部功能控件
     */
    private inner class FuncView(context: Context?) :
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

    private fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}