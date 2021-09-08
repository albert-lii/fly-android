package org.fly.uikit

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/9/2 4:16 下午
 * @description: 自定义Toolbar
 * @since: 1.0.0
 */
open class TitleToolbar : Toolbar {
    /**
     * 标题
     */
    private var title: String = ""
    private var titleColor: Int = Color.BLACK
    private var titleSize: Float = 18.dpToPxF
    private var titleBold: Boolean = false

    /**
     * 左侧功能区
     */
    private var leftPaddingL: Int = 14.dpToPx
    private var leftPaddingR: Int = 0
    private var leftIcon: Drawable? = null
    private var leftIconWidth: Int = LinearLayout.LayoutParams.WRAP_CONTENT
    private var leftIconHeight: Int = LinearLayout.LayoutParams.WRAP_CONTENT
    private var leftText: String = ""
    private var leftTextColor = Color.BLACK
    private var leftTextSize = 15.dpToPxF
    private var leftTextLeftMargin: Int = 0

    /**
     * 右侧功能区
     */
    private var rightPaddingL: Int = 0
    private var rightPaddingR: Int = 14.dpToPx
    private var rightIcon: Drawable? = null
    private var rightIconWidth: Int = LinearLayout.LayoutParams.WRAP_CONTENT
    private var rightIconHeight: Int = LinearLayout.LayoutParams.WRAP_CONTENT
    private var rightText: String = ""
    private var rightTextColor = Color.BLACK
    private var rightTextSize = 15.dpToPxF
    private var rightTextLeftMargin: Int = 0

    /**
     * 分割线
     */
    private var dividerColor: Int = Color.TRANSPARENT
    private var dividerHeight: Int = 0

    /**
     * 其他属性
     */
    private var barHeight: Int = LinearLayout.LayoutParams.WRAP_CONTENT // 高度，不包括StatusBar与分割线的高度
    private var addStatusBarHeight = false // 是否添加状态栏高度


    /**
     * 子控件
     */
    private lateinit var childContainer: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var leftFuncView: FuncView
    private lateinit var rightFuncView: FuncView
    private var dividerView: View? = null


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // 消除Toolbar自带的左右间距
        setContentInsetsRelative(0, 0)
        initAttr(attrs)
        initView(context)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttr(attrs: AttributeSet?) {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_uikit_TitleBar)
            // 标题
            title = ta.getString(R.styleable.fly_uikit_TitleBar_fu_title) ?: ""
            titleColor = ta.getColor(R.styleable.fly_uikit_TitleBar_fu_titleColor, titleColor)
            titleSize = ta.getDimension(R.styleable.fly_uikit_TitleBar_fu_titleSize, titleSize)
            titleBold = ta.getBoolean(R.styleable.fly_uikit_TitleBar_fu_titleBold, titleBold)

            // 左侧功能区
            leftPaddingL = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_leftPaddingL,
                leftPaddingL.toFloat()
            ).toInt()
            leftPaddingR = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_leftPaddingR,
                leftPaddingR.toFloat()
            ).toInt()
            leftIcon = ta.getDrawable(R.styleable.fly_uikit_TitleBar_fu_leftIcon)
            leftIconWidth = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_leftIconWidth,
                leftIconHeight.toFloat()
            ).toInt()
            leftIconHeight = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_leftIconHeight,
                leftIconHeight.toFloat()
            ).toInt()
            leftText = ta.getString(R.styleable.fly_uikit_TitleBar_fu_leftText) ?: ""
            leftTextSize =
                ta.getDimension(R.styleable.fly_uikit_TitleBar_fu_leftTextSize, leftTextSize)
            leftTextColor =
                ta.getColor(R.styleable.fly_uikit_TitleBar_fu_leftTextColor, leftTextColor)
            leftTextLeftMargin = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_leftTextLeftMargin,
                leftTextLeftMargin.toFloat()
            ).toInt()

            // 右侧功能区
            rightPaddingL = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_rightPaddingL,
                rightPaddingL.toFloat()
            ).toInt()
            rightPaddingR = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_rightPaddingR,
                rightPaddingR.toFloat()
            ).toInt()
            rightIcon = ta.getDrawable(R.styleable.fly_uikit_TitleBar_fu_rightIcon)
            rightIconWidth = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_rightIconWidth,
                rightIconWidth.toFloat()
            ).toInt()
            rightIconHeight = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_rightIconHight,
                rightIconHeight.toFloat()
            ).toInt()
            rightText = ta.getString(R.styleable.fly_uikit_TitleBar_fu_rightText) ?: ""
            rightTextSize = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_rightTextSize,
                rightTextSize
            )
            rightTextColor =
                ta.getColor(R.styleable.fly_uikit_TitleBar_fu_rightTextColor, rightTextColor)
            rightTextLeftMargin = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_rightTextLeftMargin,
                rightTextLeftMargin.toFloat()
            ).toInt()

            // 分割线
            dividerHeight = ta.getDimension(
                R.styleable.fly_uikit_TitleBar_fu_dividerHeight,
                dividerHeight.toFloat()
            ).toInt()
            dividerColor =
                ta.getColor(R.styleable.fly_uikit_TitleBar_fu_dividerColor, dividerColor)

            // 其他属性
            barHeight =
                ta.getDimension(R.styleable.fly_uikit_TitleBar_fu_barHeight, barHeight.toFloat())
                    .toInt()
            addStatusBarHeight =
                ta.getBoolean(R.styleable.fly_uikit_TitleBar_fu_addStatusBarHeight, false)
            if (addStatusBarHeight) {
                setPadding(
                    paddingLeft,
                    paddingTop + getStatusBarHeight(),
                    paddingRight,
                    paddingBottom
                )
            }
            ta.recycle()
        }
    }


    private fun initView(context: Context) {
        childContainer = LinearLayout(context)
        childContainer.orientation = LinearLayout.VERTICAL
        val clp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(childContainer, clp)

        // Titlebar的主要部分
        val mainChildContainer = FrameLayout(context)
        val mainChildLp = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, barHeight)
        childContainer.addView(mainChildContainer, mainChildLp)

        // Titlebar的分割线
        if (dividerHeight > 0) {
            dividerView = View(context)
            dividerView!!.setBackgroundColor(dividerColor)
            val dividerLp = LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dividerHeight)
            childContainer.addView(dividerView, dividerLp)
        }

        /**
         * Titlebar的功能组件
         */
        // 标题
        titleView = TextView(context)
        val titleLp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        titleLp.gravity = Gravity.CENTER
        titleView.text = title
        titleView.setTextColor(titleColor)
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize)
        titleView.paint.isFakeBoldText = titleBold
        titleView.isSingleLine = true
        titleView.ellipsize = TextUtils.TruncateAt.END
        mainChildContainer.addView(titleView, titleLp)

        // 左侧功能区
        leftFuncView = FuncView(context)
        leftFuncView.setPadding(leftPaddingL, 0, leftPaddingR, 0)
        val lfvLp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        lfvLp.gravity = Gravity.START
        if (leftIcon == null) {
            leftFuncView.showIcon(false)
        } else {
            leftFuncView.showIcon(true)
            leftFuncView.setIcon(leftIcon)
        }
        if (leftIconWidth != LinearLayout.LayoutParams.WRAP_CONTENT
            && leftIconHeight != LinearLayout.LayoutParams.WRAP_CONTENT
        ) {
            leftFuncView.setIconSize(leftIconWidth, leftIconHeight)
        } else if (leftIconWidth != LinearLayout.LayoutParams.WRAP_CONTENT) {
            leftFuncView.setIconWidthOnly(leftIconWidth)
        } else if (leftIconHeight != LinearLayout.LayoutParams.WRAP_CONTENT) {
            leftFuncView.setIconHeightOnly(leftIconHeight)
        } else {
            leftFuncView.setIconSize(leftIconWidth, leftIconHeight)
        }
        leftFuncView.setText(leftText)
        leftFuncView.setTextColor(leftTextColor)
        leftFuncView.setTextSize(leftTextSize)
        leftFuncView.setTextLeftMargin(leftTextLeftMargin)
        mainChildContainer.addView(leftFuncView, lfvLp)

        // 右侧功能区
        rightFuncView = FuncView(context)
        rightFuncView.setPadding(rightPaddingL, 0, rightPaddingR, 0)
        val rfvLp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        rfvLp.gravity = Gravity.END
        rightFuncView.visibility = View.GONE
        if (rightIcon == null) {
            rightFuncView.showIcon(false)
        } else {
            rightFuncView.showIcon(true)
            rightFuncView.setIcon(rightIcon)
        }
        if (rightIconWidth != LinearLayout.LayoutParams.WRAP_CONTENT
            && rightIconHeight != LinearLayout.LayoutParams.WRAP_CONTENT
        ) {
            rightFuncView.setIconSize(rightIconWidth, rightIconHeight)
        } else if (rightIconWidth != LinearLayout.LayoutParams.WRAP_CONTENT) {
            rightFuncView.setIconWidthOnly(rightIconWidth)
        } else if (leftIconHeight != LinearLayout.LayoutParams.WRAP_CONTENT) {
            rightFuncView.setIconHeightOnly(rightIconHeight)
        } else {
            rightFuncView.setIconSize(rightIconWidth, rightIconHeight)
        }
        rightFuncView.setText(rightText)
        rightFuncView.setTextColor(rightTextColor)
        rightFuncView.setTextSize(rightTextSize)
        rightFuncView.setTextLeftMargin(rightTextLeftMargin)
        mainChildContainer.addView(rightFuncView, rfvLp)
    }

    fun showDivider(show: Boolean) {
        dividerView?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun setTitle(@StringRes resId: Int) {
        titleView.setText(resId)
    }

    override fun setTitle(title: CharSequence?) {
        titleView.setText(title)
    }

    override fun getTitle(): String {
        return titleView.text.toString()
    }

    override fun setTitleTextColor(color: Int) {
        titleView.setTextColor(color)
    }

    fun setTitleSize(size: Float) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    fun showLeftFuncView(show: Boolean) {
        leftFuncView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun showRightFuncView(show: Boolean) {
        rightFuncView.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setLeftClick(listener: OnClickListener?) {
        leftFuncView.setOnClickListener(listener)
    }

    fun setRightClick(listener: OnClickListener?) {
        rightFuncView.setOnClickListener(listener)
    }

    fun getLeftFuncView() = leftFuncView

    fun getRightFuncView() = rightFuncView

    /**
     * TitleBar的内部功能控件
     */
    class FuncView(context: Context?) :
        LinearLayout(context) {
        protected var iconView: ImageView
        protected var textView: TextView

        init {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL

            iconView = ImageView(context)
            iconView.adjustViewBounds = true
            addView(iconView)

            textView = TextView(context)
            textView.isSingleLine = true
            textView.ellipsize = TextUtils.TruncateAt.END
            textView.visibility = View.GONE
            addView(textView)
        }

        fun showIcon(show: Boolean) {
            iconView.visibility = if (show) View.VISIBLE else View.GONE
        }

        fun showText(show: Boolean) {
            textView.visibility = if (show) View.VISIBLE else View.GONE
        }

        fun setIcon(resId: Int) {
            iconView.setImageResource(resId)
        }

        fun setIcon(icon: Drawable?) {
            iconView.setImageDrawable(icon)
        }

        fun setIconWidthOnly(size: Int) {
            val lp = iconView.layoutParams as LayoutParams
            lp.width = size
            lp.height = LayoutParams.WRAP_CONTENT
            iconView.layoutParams = lp
        }

        fun setIconHeightOnly(size: Int) {
            val lp = iconView.layoutParams as LayoutParams
            lp.width = LayoutParams.WRAP_CONTENT
            lp.height = size
            iconView.layoutParams = lp
        }

        fun setIconSize(width: Int, height: Int) {
            val lp = iconView.layoutParams as LayoutParams
            lp.width = width
            lp.height = height
            iconView.layoutParams = lp
        }

        fun setTextLeftMargin(margin: Int) {
            val lp = textView.layoutParams as LayoutParams
            lp.leftMargin = margin
            textView.layoutParams = lp
        }

        fun setText(resId: Int) {
            textView.setText(resId)
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
    }

    private fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}