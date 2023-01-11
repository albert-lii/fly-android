package org.fly.eventbus

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 事件
 */
open class Event<T>(
    val key: Any,
    val isSticky: Boolean,
    extraBufferCapacity: Int = Int.MAX_VALUE,
    bufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
) {
    // replay: 重放的数据个数，只针对新订阅者（即在发射数据后的订阅），决定新来的订阅者能接收到的数据个数
    // extraBufferCapacity: 额外的数据缓存，Flow存在发送过快，消费太慢的情况，这种情况下，就需要使用缓存池
    // 把未消费的数据存下来，缓存池容量 = replay + extraBufferCapacity，如果总数为0，则视为Int.MAX_VALUE
    // bufferOverflow: 数据在超出缓存池容量后的缓存策略
    protected open var source = MutableSharedFlow<T?>(
        replay = if (isSticky) 1 else 0,
        extraBufferCapacity = extraBufferCapacity,
        onBufferOverflow = bufferOverflow
    )

    /**
     * 获取执行线程
     */
    private fun getDispatcher(scheduler: Scheduler?): CoroutineDispatcher {
        return when (scheduler) {
            Scheduler.MAIN -> Dispatchers.Main
            Scheduler.IO -> Dispatchers.IO
            Scheduler.DEFAULT -> Dispatchers.Default
            else -> Dispatchers.Default
        }
    }

    /**
     * 获取Lifecycle状态
     */
    private fun getLifecycleState(state: ActiveState?): Lifecycle.State {
        return when (state) {
            ActiveState.CREATED -> Lifecycle.State.CREATED
            ActiveState.STARTED -> Lifecycle.State.STARTED
            ActiveState.RESUMED -> Lifecycle.State.RESUMED
            ActiveState.DESTROYED -> Lifecycle.State.DESTROYED
            else -> Lifecycle.State.CREATED
        }
    }

    /**
     * 清空缓存数据
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun empty() {
        source.resetReplayCache()
    }

    /**
     * 发送事件
     */
    suspend fun postInternal(value: T?) {
        source.emit(value)
    }

    /**
     * 发送事件
     */
    fun postInScope(
        scope: CoroutineScope,
        scheduler: Scheduler? = null,
        value: T?
    ): Job {
        val dispatcher = getDispatcher(scheduler)
        return scope.launch(dispatcher) {
            postInternal(value)
        }
    }

    /**
     * 发送事件
     */
    fun postInLifecycle(
        owner: LifecycleOwner,
        scheduler: Scheduler? = null,
        value: T?
    ): Job {
        return postInScope(
            scope = owner.lifecycleScope,
            scheduler = scheduler,
            value = value
        )
    }

    /**
     * 发送事件
     */
    fun post(scheduler: Scheduler? = null, value: T?): Job {
        return postInScope(
            scope = GlobalScope,
            scheduler = scheduler,
            value = value
        )
    }

    /**
     * 订阅事件
     */
    suspend fun observeInternal(onReceived: suspend (value: T?) -> Unit) {
        try {
            source.collect { value ->
                onReceived.invoke(value)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 订阅事件
     */
    fun observeInScope(
        scope: CoroutineScope,
        scheduler: Scheduler? = null,
        onReceived: suspend (value: T?) -> Unit
    ): Job {
        val dispatcher = getDispatcher(scheduler)
        return scope.launch(dispatcher) {
            observeInternal(onReceived = onReceived)
        }
    }

    /**
     * 订阅事件，带生命周期检测
     */
    fun observeInLifecycle(
        owner: LifecycleOwner,
        minActiveState: ActiveState? = ActiveState.CREATED,
        scheduler: Scheduler? = Scheduler.MAIN,
        isAutoClear: Boolean = true,
        onReceived: suspend (value: T?) -> Unit
    ): Job {
        if (isAutoClear) {
            autoClearInLifecycle(owner)
        }
        val dispatcher = getDispatcher(scheduler)
        /**
         * repeatOnLifecycle: 在指定生命周期中执行代码块，不在指定状态，会取消协程，下次重新进入指定状态后，会重
         * 开协程来执行，即代码块会被多次执行，推荐使用。
         *
         * whenStateAtLeast: 在指定生命周期中执行代码块，不在指定状态，会挂起协程，不会取消，下次重新进入指定状态后，
         * 继续执行挂起的协程，代码块只被执行一次，挂起时上游数据流仍然处于活跃状态，会产生资源消耗。
         */
        return owner.lifecycleScope.launch {
            owner.lifecycle.repeatOnLifecycle(getLifecycleState(minActiveState)) {
                withContext(dispatcher) {
                    observeInternal(onReceived = onReceived)
                }
            }
        }
    }

    /**
     * 每当LifecycleOwner销毁时，检测当前事件是否还有订阅者，如果没有则移除
     */
    private fun autoClearInLifecycle(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                val subscriptCount = source.subscriptionCount.value
                if (subscriptCount <= 0) {
                    EventManager.getInstance().remove(key, isSticky)
                }
            }
        })
    }
}