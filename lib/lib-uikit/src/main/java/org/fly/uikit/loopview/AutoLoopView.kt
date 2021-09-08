package org.fly.uikit.loopview

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/13 2:43 下午
 * @description: 无限滚动View，可以用来做Banner、公告等类似视图
 * @since: 1.0.0
 */
open class AutoLoopView : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 自动开始视图循环
     */
    fun <VH : ViewHolder> auto(
        owner: LifecycleOwner,
        minScale: Float = 1f, // 两侧item缩放比
        angle: Float = 0f, // 翻转角度
        itemSpace: Int = 0, // item之间的相隔间距
        intervalTime: Long = 3000L,
        orientation: Int = OrientationHelper.HORIZONTAL,
        adapter: Adapter<VH>,
        listener: OnPageChangeListener? = null
    ) {
        setAdapter(adapter)
        layoutManager = OverFlyingLayoutManager(
            minScale,
            itemSpace,
            orientation
        ).apply {
            setAngle(angle)
            listener?.let {
                setOnPageChangeListener(it)
            }
        }
        start(owner, intervalTime)
    }

    /**
     * 在使用此方法前，必须先设置Adapter与LayoutManager
     */
    fun start(owner: LifecycleOwner, intervalTime: Long = 3000L) {
        addOnScrollListener(CenterScrollListener())
        PagerSnapHelper().attachToRecyclerView(this)
        loopWithDelay(owner, intervalTime)
    }
}