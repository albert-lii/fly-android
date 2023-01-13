package org.fly.utils.countdown

import androidx.lifecycle.LifecycleOwner

/**
 * 倒计时工具类
 */
class CountdownUtils {
    private var countdownPool = HashMap<Any, Ticker>()

    companion object {
        @Volatile
        private var instance: CountdownUtils? = null

        @JvmStatic
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: CountdownUtils().also { instance = it }
            }
    }

    /**
     * 开启倒计时
     */
    fun start(
        key: Any,
        interval: Long, // 倒计时间隔时间
        total: Long, // 倒计时总时间
        listener: Ticker.OnTickListener? = null
    ) {
        var ticker = countdownPool.get(key)
        if (ticker != null) {
            if (!ticker.isFinish()) {
                ticker.cancel()
            }
            countdownPool.remove(key)
        }
        ticker = Ticker(interval, total)
        ticker.setListener(listener)
        ticker.start()
        countdownPool[key] = ticker
    }

    /**
     * 开启倒计时
     */
    fun start(
        key: Any,
        interval: Long, // 倒计时间隔时间
        total: Long, // 倒计时总时间
        owner: LifecycleOwner,
        callback: (e: CountdownEvent) -> Unit
    ) {
        var ticker = countdownPool.get(key)
        if (ticker != null) {
            if (!ticker.isFinish()) {
                ticker.cancel()
            }
            countdownPool.remove(key)
        }
        ticker = Ticker(interval, total)
        ticker.setListener(object : Ticker.OnTickListener {
            override fun onTick(leftCount: Int, intervel: Long, isFinish: Boolean) {
                observeSticky(owner, key, callback)
            }
        })
        ticker.start()
        countdownPool.put(key, ticker)
    }

    /**
     * 取消倒计时
     */
    fun cancel(key: Any) {
        val ticker = countdownPool.get(key)
        if (ticker != null && !ticker.isCancel()) {
            ticker.cancel()
            countdownPool.remove(key)
        }
    }

    /**
     * 取消所有的倒计时
     */
    fun cancelAll() {
        if (countdownPool.size > 0) {
            for ((key, ticker) in countdownPool) {
                ticker.cancel()
            }
        }
        countdownPool.clear()
    }

    /**
     * 倒计时是否取消
     */
    fun isCancel(key: Any): Boolean {
        val ticker = getTicker(key)
        if (ticker != null) {
            return ticker.isCancel()
        }
        return true
    }

    /**
     * 倒计时是否结束
     */
    fun isFinish(key: Any): Boolean {
        val ticker = getTicker(key)
        if (ticker != null) {
            return ticker.isFinish()
        }
        return true
    }

    fun getTicker(key: Any): Ticker? {
        return countdownPool.get(key)
    }

    /**
     * 订阅倒计时
     */
    fun observe(
        owner: LifecycleOwner,
        key: Any,
        callback: (e: CountdownEvent) -> Unit
    ) {
//        LiveBus.get(key, CountdownEvent::class.java)
//            .observe(owner, object : Observer<CountdownEvent> {
//                override fun onChanged(e: CountdownEvent) {
//                    callback.invoke(e)
//                }
//            })
    }

    /**
     * 订阅倒计时
     */
    fun observeSticky(
        owner: LifecycleOwner,
        key: Any,
        callback: (e: CountdownEvent) -> Unit
    ) {
//        LiveBus.get(key, CountdownEvent::class.java)
//            .observeSticky(owner, object : Observer<CountdownEvent> {
//                override fun onChanged(e: CountdownEvent) {
//                    callback.invoke(e)
//                }
//            })
    }

    data class CountdownEvent(
        val key: Any,
        val leftCount: Int, // 剩余的执行次数
        val interval: Long, // 倒计时间隔时间
        val isFinish: Boolean // 倒计时是否结束
    )
}