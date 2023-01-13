package org.fly.utils.countdown

import android.os.Handler

/**
 * 倒计时
 */
class Ticker(
    private val intervalTime: Long,  // 倒计时间隔时间
    private val totalTime: Long // 倒计时总时间
) {
    private var currentCount: Int = 0 // 目前的执行次数
    private var totalCount: Int = 0  // 倒计时总执行的次数
    private var startTime: Long = 0L // 倒计时开始的时间
    private var isCancel: Boolean = false // 倒计时是否被取消
    private val handler = Handler()
    private var listener: OnTickListener? = null


    init {
        this.totalCount = (totalTime / intervalTime).toInt()
    }

    /**
     * 开始倒计时
     */
    @Synchronized
    fun start() {
        isCancel = false
        if (intervalTime < 0 || totalCount <= 0) {
            listener?.onTick(0, intervalTime, true)
            return
        }
        currentCount = 0
        startTime = System.currentTimeMillis()
        handler.post(tickRunnable)
    }

    /**
     * 取消倒计时
     */
    @Synchronized
    fun cancel() {
        isCancel = true
        handler.removeCallbacks(tickRunnable)
        listener?.onTick(0, intervalTime, true)
        listener = null
    }

    /**
     * 是否已经取消
     */
    fun isCancel(): Boolean {
        return isCancel
    }

    /**
     * 是否已经结束
     */
    fun isFinish(): Boolean {
        if (isCancel) {
            return true
        }
        val diffTime = System.currentTimeMillis() - startTime
        if (diffTime >= totalTime) {
            return true
        }
        val leftCount = totalCount - currentCount
        if (leftCount <= 0) {
            return true
        }
        return false
    }

    fun setListener(listener: OnTickListener?): Ticker {
        this.listener = listener
        return this
    }

    private var tickRunnable = object : Runnable {

        override fun run() {
            if (isCancel) {
                return
            }
            currentCount++
            // 剩余执行次数
            val leftCount = totalCount - currentCount
            if (leftCount <= 0) {
                listener?.onTick(0, intervalTime, true)
            } else {
                listener?.onTick(leftCount, intervalTime, false)
                // 延迟时间
                val delay = currentCount * intervalTime - (System.currentTimeMillis() - startTime)
                handler.postDelayed(this, delay)
            }
        }
    }

    interface OnTickListener {
        /**
         * 倒计时
         *
         * @param leftCount 剩余的执行次数
         * @param intervel 倒计时间隔时间
         * @param isFinish 倒计时是否结束
         */
        fun onTick(leftCount: Int, intervel: Long, isFinish: Boolean)
    }
}