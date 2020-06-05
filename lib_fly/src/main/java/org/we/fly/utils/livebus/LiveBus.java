package org.we.fly.utils.livebus;

import androidx.annotation.NonNull;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/5 10:57 AM
 * @description: --
 * @since: 1.0.0
 */
public final class LiveBus {

    public static <T> BusObservable<T> get(@NonNull Object key, Class<T> type) {
        return LiveBusCore.getInstance().with(key, type);
    }

    public static <T> BusObservable<T> get(@NonNull Class<T> eventType) {
        return LiveBusCore.getInstance().with(eventType.getName(), eventType);
    }

    public static BusObservable<Object> get(@NonNull Object key) {
        return LiveBusCore.getInstance().with(key, Object.class);
    }

    public static void removeEvent(@NonNull Object key) {
        LiveBusCore.getInstance().removeEvent(key);
    }
}
