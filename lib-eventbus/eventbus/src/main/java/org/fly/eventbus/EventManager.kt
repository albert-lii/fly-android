package org.fly.eventbus

import kotlinx.coroutines.channels.BufferOverflow
import java.util.concurrent.ConcurrentHashMap

/**
 * Event管理类
 */
class EventManager {

    companion object {
        fun getInstance() = Singleton.holder
    }

    object Singleton {
        val holder = EventManager()
    }

    // 普通事件
    private val events = ConcurrentHashMap<Any, Event<*>>()

    // 粘性事件
    private val stickyEvents = ConcurrentHashMap<Any, Event<*>>()

    /**
     * 获取事件
     */
    fun <T> get(
        key: Any,
        isSticky: Boolean,
        extraBufferCapacity: Int = Int.MAX_VALUE,
        bufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
    ): Event<T> {
        if (isSticky) {
            var e = stickyEvents[key]
            if (e != null) {
                return e as Event<T>
            }
            e = Event<T>(
                key = key,
                isSticky = true,
                extraBufferCapacity = extraBufferCapacity,
                bufferOverflow = bufferOverflow
            )
            stickyEvents[key] = e
            return e
        } else {
            var e = events[key]
            if (e != null) {
                return e as Event<T>
            }
            e = Event<T>(
                key = key,
                isSticky = false,
                extraBufferCapacity = extraBufferCapacity,
                bufferOverflow = bufferOverflow
            )
            events[key] = e
            return e
        }
    }

    fun remove(key: Any, isSticky: Boolean) {
        if (isSticky) {
            stickyEvents.remove(key)
        } else {
            events.remove(key)
        }
    }

    fun remove(key: Any) {
        events.remove(key)
        stickyEvents.remove(key)
    }

    fun clear() {
        events.clear()
    }

    fun count(): Int {
        val c1 = events.size
        val c2 = stickyEvents.size
        return c1 + c2
    }
}