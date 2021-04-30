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
    private var countDownPool = HashMap<Any, MyCountDownTimer>()

    companion object {
        @Volatile
        private var instance: CountDownUtils? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CountDownUtils().also { instance = it }
            }
    }

    /**
     * 开启倒计时
     */
    fun startCountDown(
        key: Any,
        millisInFuture: Long,
        countDownInterval: Long,
        owner: LifecycleOwner,
        onTick: ((e: CountDownEvent) -> Unit)? = null
    ) {
        var timer = countDownPool.get(key)
        if (timer != null) {
            timer.cancel()
            countDownPool.remove(key)
        }
        timer = MyCountDownTimer(key, millisInFuture, countDownInterval)
        timer.observe(owner, onTick)
        timer.start()
        countDownPool.put(key, timer)
    }

    /**
     * 恢复倒计时
     */
    fun retoreCountDown(
        key: Any,
        owner: LifecycleOwner,
        onTick: ((e: CountDownEvent) -> Unit)? = null
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
        if (timer != null) {
            timer.isCancel = true
            timer.cancel()
            LiveBus.get(key).post(
                CountDownEvent(
                    key = key,
                    millisUntilFinished = 0,
                    isFinish = false,
                    isCancel = true
                )
            )
            countDownPool.remove(key)
        }
    }

    /**
     * 取消所有的倒计时
     */
    fun cancelAll() {
        if (countDownPool.size > 0) {
            for ((key, value) in countDownPool) {
                value.isCancel = true
                value.cancel()
                LiveBus.get(key).post(
                    CountDownEvent(
                        key = key,
                        millisUntilFinished = 0,
                        isFinish = false,
                        isCancel = true
                    )
                )
            }
        }
        countDownPool.clear()
    }

    /**
     * 订阅倒计时取消
     */
    fun observeCanccel(key: Any, owner: LifecycleOwner, block: (e: CountDownEvent) -> Unit) {
        LiveBus.get(key, CountDownEvent::class.java)
            .observe(owner, object : Observer<CountDownEvent> {
                override fun onChanged(e: CountDownEvent) {
                    block.invoke(e)
                }
            })
    }

    /**
     * 订阅倒计时取消
     */
    fun observeStickyCanccel(
        key: Any,
        owner: LifecycleOwner,
        block: (e: CountDownEvent) -> Unit
    ) {
        LiveBus.get(key, CountDownEvent::class.java)
            .observeSticky(owner, object : Observer<CountDownEvent> {
                override fun onChanged(e: CountDownEvent) {
                    block.invoke(e)
                }
            })
    }

    private inner class MyCountDownTimer : CountDownTimer {
        private var key: Any
        var isCancel = false

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
            if (!isCancel) {
                LiveBus.get(key).post(
                    CountDownEvent(
                        key = key,
                        millisUntilFinished = millisUntilFinished,
                        isFinish = false,
                        isCancel = false
                    )
                )
            }
        }

        override fun onFinish() {
            LiveBus.get(key).post(
                CountDownEvent(
                    key = key,
                    millisUntilFinished = 0,
                    isFinish = true,
                    isCancel = false
                )
            )
            countDownPool.remove(key)
        }

        fun observe(owner: LifecycleOwner, onTick: ((event: CountDownEvent) -> Unit)?) {
            LiveBus.get(key, CountDownEvent::class.java)
                .observeSticky(owner, object : Observer<CountDownEvent> {
                    override fun onChanged(e: CountDownEvent) {
                        onTick?.invoke(e)
                    }
                })
        }
    }

    data class CountDownEvent(
        val key: Any,
        val millisUntilFinished: Long,
        val isFinish: Boolean,
        val isCancel: Boolean
    )
}