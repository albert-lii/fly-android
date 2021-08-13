package org.fly.widget.banner

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/13 2:43 下午
 * @description: Banner
 * @since: 1.0.0
 */
class Banner : RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun <VH : ViewHolder> startWithDefault(
        owner: LifecycleOwner,
        intervalTime: Long = 3000L,
        adapter: Adapter<VH>,
        lister: OnPageChangeListener
    ) {
        setAdapter(adapter)
        layoutManager = SlidingLayoutManager(
            1f,
            0,
            SlidingLayoutManager.HORIZONTAL
        ).apply {
            angle = 0f
            setOnPageChangeListener(lister)
        }
        start(owner, intervalTime)
    }

    fun start(owner: LifecycleOwner, intervalTime: Long = 3000L) {
        addOnScrollListener(CenterScrollListener())
        PagerSnapHelper().attachToRecyclerView(this)
        loopWithDelay(owner, intervalTime)
    }
}