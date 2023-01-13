package org.fly.utils.exts

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import android.widget.EditText


/**=================================================================================================
 * 点击事件扩展
 *================================================================================================*/
@Suppress("UNCHECKED_CAST")
fun <T : View> T.click(block: (T) -> Unit) = setOnClickListener {
    block(it as T)
}

@Suppress("UNCHECKED_CAST")
fun <T : View> T.longClick(block: (T) -> Boolean) = setOnLongClickListener { block(it as T) }

/**
 * 上次点击时间
 */
private const val TRIGGER_LAST_TIME_KEY = 1234567890
private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(TRIGGER_LAST_TIME_KEY) != null) getTag(TRIGGER_LAST_TIME_KEY) as Long else 0
    set(value) {
        setTag(TRIGGER_LAST_TIME_KEY, value)
    }

private const val TRIGGER_DELAY_KEY = 1234567891
private var <T : View> T.triggerDelay: Long
    get() = if (getTag(TRIGGER_DELAY_KEY) != null) getTag(TRIGGER_DELAY_KEY) as Long else -1
    set(value) {
        setTag(TRIGGER_DELAY_KEY, value)
    }

/***
 * 带延迟过滤的点击事件View扩展
 *
 * @param delay Long 延迟时间，默认500毫秒，防抖动
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
@Suppress("UNCHECKED_CAST")
fun <T : View> T.clickWithTrigger(delay: Long = 500, block: (T) -> Unit) {
    triggerDelay = delay
    setOnClickListener {
        if (clickEnable()) {
            block(it as T)
        }
    }
}

/**
 * 判断是否可点击
 */
private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

/**
 *  判断是否是连续按点击
 *  @param time Long  设定的连续点击的间隔时间，单位毫秒，默认600
 *  @return
 */
fun <T : View> T.isSerialClick(time: Long = 600): Boolean {
    triggerDelay = time
    return (!clickEnable())
}

/**=================================================================================================
 * EditText扩展
 *================================================================================================*/

/**
 * 移动光标到文字指定位置
 */
fun EditText.setSelectionSafely(index: Int) {
    try {
        this.setSelection(index)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 将光标移动到文字最后面
 */
fun EditText.moveSelectionToEnd() {
    text.length.let { if (it > 0) setSelection(it) }
}

/**=================================================================================================
 * 其他扩展
 *================================================================================================*/
/**
 * 扩大View的Touch区域
 */
fun View.expendTouchArea(expendSize: Int = 16) {
    (this.parent as View).let { parentView ->
        parentView.post {
            val rect = Rect()
            this.getHitRect(rect)
            rect.left -= expendSize
            rect.top -= expendSize
            rect.right += expendSize
            rect.bottom += expendSize
            parentView.touchDelegate = TouchDelegate(rect, this)
        }
    }
}

