package org.we.fly.extensions

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/9 7:41 PM
 * @description: Obserser相关的扩展
 * @since: 1.0.0
 */

/**
 * 简化LiveData的订阅操作
 */
fun <T> LiveData<T>.observeSimply(owner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(owner, Observer {
        block(it)
    })
}

/**
 * 简化LiveData的订阅操作，参数不为null
 */
fun <T> LiveData<T>.observeNonNullSimply(owner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(owner, Observer {
        if (it != null) {
            block(it)
        }
    })
}
