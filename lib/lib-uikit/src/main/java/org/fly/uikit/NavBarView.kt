package org.fly.uikit

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/7/9 2:46 PM
 * @description: 与底部导航栏高度一致的View
 * @since: 1.0.0
 */
class NavBarView : View {
    private var navBarHeight = 0

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
        navBarHeight = getNavBarHeight()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), navBarHeight);
    }

    private fun getNavBarHeight(): Int {
        val res = Resources.getSystem()
        val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId != 0) res.getDimensionPixelSize(resourceId) else 0
    }
}