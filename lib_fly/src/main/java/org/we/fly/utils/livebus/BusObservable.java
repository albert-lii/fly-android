package org.we.fly.utils.livebus;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/2 4:04 PM
 * @description: LiveBus的被观察者
 * @since: 1.0.0
 */
public interface BusObservable<T> {
    /**
     * 发送消息
     *
     * @param value 发送的消息
     */
    void post(final T value);

    /**
     * 延迟发送消息
     *
     * @param value 发送的消息
     * @param delay 延迟的毫秒数
     */
    void postDelay(final T value, final long delay);

    /**
     * 延迟发送，带生命周期
     * 如果延时发送消息的时候sender处于非激活状态，消息取消发送
     *
     * @param owner 消息发送者
     * @param value 发送的消息
     * @param delay 延迟毫秒数
     */
    void postDelay(@NonNull final LifecycleOwner owner, final T value, final long delay);

    /**
     * 发送消息
     * 强制接收到消息的顺序和发送顺序一致
     *
     * @param value 发送的消息
     */
    void postOrderly(final T value);

    /**
     * 注册一个Observer，带有生命周期感知，自动取消订阅
     * 在注册之前的发送消息，在注册时不会被接收
     *
     * @param owner    LifecycleOwner
     * @param observer 观察者
     */
    void observe(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer);

    /**
     * 注册一个Observer，带有生命周期感知，自动取消订阅
     * 在注册之前发送的消息，在注册时会被接收（消息同步）
     *
     * @param owner    LifecycleOwner
     * @param observer 观察者
     */
    void observeSticky(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer);

    /**
     * 注册一个Observer，需手动解除绑定
     * 在注册之前的发送消息，在注册时不会被接收
     *
     * @param observer 观察者
     */
    void observeForever(@NonNull final Observer<T> observer);

    /**
     * 注册一个Observer，需手动解除绑定
     * 在注册之前发送的消息，在注册时会被接收（消息同步）
     *
     * @param observer 观察者
     */
    void observeStickyForever(@NonNull final Observer<T> observer);

    /**
     * 通过Forever方式注册的，需要调用该方法取消订阅
     *
     * @param observer 观察者
     */
    void removeObserver(@NonNull final Observer<T> observer);

    /**
     * 订阅了本BusObservable的所有Observer的生命周期状态是否一直处于活跃状态
     *
     * @param alwaysBeActive {@code true}-Observer可以在Activity的onCreate到onStop之间的生命周期状态接收消息
     *                       {@code false}-Observer可以在Activity的onStart到onPause之间的生命周期状态接收消息
     */
    BusObservable<T> observerAlwaysBeActive(final boolean alwaysBeActive);

    /**
     * 当BusObservable的所有的Observer都被移除时，BusObservable对应的Event是否从事件总线中移除
     *
     * @param autoClear
     */
    BusObservable<T> autoClear(final boolean autoClear);
}
