package org.fly.uikit.systembar

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View

/**
 * 与状态栏高度一致的View
 *
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/29 1:58 PM
 * @since: 1.0.0
 */
class StatusBarView : View {
    private var statusBarHeight = 0

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        statusBarHeight = getStatusBarHeight()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), statusBarHeight);
    }

    private fun getStatusBarHeight(): Int {
        val resources = Resources.getSystem()
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
}