package org.we.fly.utils

import android.os.CountDownTimer
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.we.fly.utils.livebus.LiveBus

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2021/4/25 3:27 PM
 * @description: 倒计时相关工具类
 * @since: 1.0.0
 */
class CountDownUtils {

    companion object {
        @Volatile
        private var instance: CountDownUtils? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CountDownUtils().also { instance = it }
            }

        private var countDownPool = HashMap<Any, MyCountDownTimer>()
    }

    /**
     * 开启倒计时
     */
    fun startCountDown(
        key: Any,
        millisInFuture: Long,
        countDownInterval: Long,
        owner: LifecycleOwner,
        onTick: ((millisUntilFinished: Long) -> Unit)? = null
    ) {
        var timer = countDownPool.get(key)
        if (timer != null) {
            timer.cancel()
            countDownPool.remove(key)
        }
        timer = MyCountDownTimer(key, millisInFuture, countDownInterval)
        timer.observe(owner,onTick)
        timer.start()
        countDownPool.put(key, timer)
    }

    /**
     * 恢复倒计时
     */
    fun retoreCountDown(
        key: Any,
        owner: LifecycleOwner,
        onTick: ((millisUntilFinished: Long) -> Unit)? = null
    ) {
        val timer = countDownPool.get(key)
        if (timer != null) {
            timer.observe(owner, onTick)
        }
    }

    /**
     * 取消倒计时
     */
    fun cancel(key: Any) {
        val timer = countDownPool.get(key)
        timer?.let { it.cancel() }
    }

    /**
     * 取消所有的倒计时
     */
    fun cancelAll() {
        if (countDownPool.size > 0) {
            for ((key, value) in countDownPool) {
                value.cancel()
            }
        }
        countDownPool.clear()
    }

    private class MyCountDownTimer : CountDownTimer {
        private var key: Any

        constructor(
            key: Any,
            millisInFuture: Long,
            countDownInterval: Long
        ) : super(
            millisInFuture,
            countDownInterval
        ) {
            this.key = key
        }

        override fun onTick(millisUntilFinished: Long) {
            LiveBus.get(key).post(millisUntilFinished)
        }

        override fun onFinish() {
            countDownPool.remove(key)
        }

        fun observe(owner: LifecycleOwner, onTick: ((millisUntilFinished: Long) -> Unit)?) {
            LiveBus.get(key, Long::class.java)
                .observeSticky(owner, object : Observer<Long> {
                    override fun onChanged(t: Long) {
                        onTick?.invoke(t)
                    }
                })
        }
    }
}