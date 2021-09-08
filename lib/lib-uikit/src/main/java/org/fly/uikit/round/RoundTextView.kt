package org.fly.uikit.round

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import org.fly.uikit.R

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/31 10:34 上午
 * @description: 圆角TextView
 * @since: 1.0.0
 */
open class RoundTextView : AppCompatTextView, IRoundImpl {

    override var helper: RHelper = RHelper(this)

    private var normalTextColor = Color.BLACK
    private var disabledTextColor = normalTextColor


    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        initAttrs(attrs)
        processRoundBackground()
        processTextColor()
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_uikit_RoundView)
            initRoundAttrs(ta)
            normalTextColor = ta.getColor(
                R.styleable.fly_uikit_RoundView_fu_disabledColor,
                normalTextColor
            )
            disabledTextColor = ta.getColor(
                R.styleable.fly_uikit_RoundView_fu_disabledColor,
                RHelper.INVALID_COLOR
            )
            if (disabledTextColor == RHelper.INVALID_COLOR) {
                disabledTextColor = normalTextColor
            }
            ta.recycle()
        }
    }

    /**
     * 处理文字颜色
     */
    fun processTextColor(enabled: Boolean? = null) {
        if (enabled ?: isEnabled) {
            setTextColor(normalTextColor)
        } else {
            setTextColor(disabledTextColor)
        }
    }

    fun setNormalTextColor(color: Int) {
        this.normalTextColor = color
    }

    fun setDisabledTextColor(color: Int) {
        this.disabledTextColor = color
    }
}