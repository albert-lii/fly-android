package org.fly.uikit.round

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import org.fly.uikit.R

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/30 7:52 下午
 * @description: 圆角LinearLayout
 * @since: 1.0.0
 */
open class RoundLinearLayout : LinearLayout, IRoundImpl {
    override var helper: RHelper = RHelper(this)

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
    }

    @SuppressLint("CustomViewStyleable")
    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.fly_uikit_RoundView)
            initRoundAttrs(ta)
            ta.recycle()
        }
    }
}