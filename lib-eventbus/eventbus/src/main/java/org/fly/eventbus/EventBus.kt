package org.fly.eventbus

import kotlinx.coroutines.channels.BufferOverflow

/**
 * 事件总线
 */
class EventBus {

    companion object {

        @JvmStatic
        fun <T> with(
            key: Any,
            extraBufferCapacity: Int = Int.MAX_VALUE,
            bufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
        ): Event<T> {
            return EventManager.getInstance().get(
                key = key,
                isSticky = false,
                extraBufferCapacity = extraBufferCapacity,
                bufferOverflow = bufferOverflow
            )
        }

        @JvmStatic
        fun <T> withSticky(
            key: Any,
            extraBufferCapacity: Int = 0,
            bufferOverflow: BufferOverflow = BufferOverflow.DROP_OLDEST
        ): Event<T> {
            return EventManager.getInstance().get(
                key = key,
                isSticky = true,
                extraBufferCapacity = extraBufferCapacity,
                bufferOverflow = bufferOverflow
            )
        }

        /**
         * 移除指定事件
         */
        @JvmStatic
        fun remove(key: Any) {
            EventManager.getInstance().remove(key)
        }

        /**
         * 移除普通事件
         */
        @JvmStatic
        fun removeOrdinary(key: Any) {
            EventManager.getInstance().remove(key, true)
        }

        /**
         * 移除粘性事件
         */
        @JvmStatic
        fun removeSticky(key: Any) {
            EventManager.getInstance().remove(key, true)
        }

        /**
         * 清除所有事件
         */
        @JvmStatic
        fun clear() {
            EventManager.getInstance().clear()
        }

        /**
         * 获取当前事件数量
         */
        @JvmStatic
        fun count(): Int {
            return EventManager.getInstance().count()
        }
    }
}