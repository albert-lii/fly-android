package org.we.fly.extensions

import android.view.View

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 5:32 PM
 * @description: View相关扩展
 * @since: 1.0.0
 */

fun <T : View> T.click(action: (T) -> Unit) {
    setOnClickListener {
        action(this)
    }
}

/**d
 * 带有限制快速点击的点击事件
 */
fun <T : View> T.singleClick(interval: Long = 500L, action: ((T) -> Unit)?) {
    setOnClickListener(SingleClickListener(interval, action))
}

class SingleClickListener<T : View>(
    private val interval: Long = 500L,
    private var clickFunc: ((T) -> Unit)?
) : View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastClickTime > interval) {
            // 单次点击事件
            if (clickFunc != null) {
                clickFunc!!(v as T)
            }
            lastClickTime = nowTime
        }
    }
}