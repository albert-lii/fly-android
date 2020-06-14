package org.we.fly.extensions

import android.view.View

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/7 5:32 PM
 * @description: View相关扩展
 * @since: 1.0.0
 */

fun <T : View> T.click(block: (T) -> Unit) {
    setOnClickListener {
        block(this)
    }
}

/**
 * 带有限制快速点击的点击事件
 */
fun <T : View> T.singleClick(block: (T) -> Unit) {
    setOnClickListener(object : SingleClickListener() {
        override fun onSingleClick(view:View) {
            block(view as T)
        }
    })
}

/**
 * 带有限制快速点击的点击事件
 */
fun <T : View> T.singleClick(interval: Long, block: (T) -> Unit) {
    setOnClickListener(object : SingleClickListener(interval) {
        override fun onSingleClick(view:View) {
            block(view as T)
        }
    })
}

abstract class SingleClickListener : View.OnClickListener {
    private var lastClickTime: Long = 0
    private var timeInterval = 500L

    constructor() {}

    constructor(interval: Long) {
        timeInterval = interval
    }

    override fun onClick(v: View) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick(v)
            lastClickTime = nowTime
        }
    }

    protected abstract fun onSingleClick(view:View)
}