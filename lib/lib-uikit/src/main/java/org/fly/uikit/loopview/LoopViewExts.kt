package org.fly.uikit.loopview

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/8/13 12:55 下午
 * @description: 对Recyclerview的扩展
 * @since: 1.0.0
 */

fun RecyclerView.loopWithDelay(owner: LifecycleOwner, intervalTime: Long = 3000L): Job? {
    val banner = layoutManager as? ILoopView
    return if (banner != null) {
        owner.lifecycleScope.launchWhenResumed {
            smoothScrollToPositionAsLoopView(owner, banner, intervalTime)
        }
    } else null
}

suspend fun RecyclerView.smoothScrollToPositionAsLoopView(
    owner: LifecycleOwner,
    loopView: ILoopView,
    intervalTime: Long = 3000L
) {
    delay(intervalTime)
    val max = loopView.itemCount
    val idle = this.scrollState == RecyclerView.SCROLL_STATE_IDLE
    if (owner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
        if (max != 0 && idle) {
            this.smoothScrollToPosition((loopView.currentPosition + 1) % max)
        }
    } else delay(intervalTime)
    smoothScrollToPositionAsLoopView(owner, loopView)
}