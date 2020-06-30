package org.we.fly.widget

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.PermissionUtils

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/29 1:58 PM
 * @description: --
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            statusBarHeight = BarUtils.getStatusBarHeight()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), statusBarHeight);
    }
}