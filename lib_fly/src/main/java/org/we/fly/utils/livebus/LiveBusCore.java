package org.we.fly.utils.livebus;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.XLiveData;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/2 3:43 PM
 * @description: 基于LiveData的事件总线核心类
 * @since: 1.0.0
 */
public class LiveBusCore {

    public static LiveBusCore getInstance() {
        return Holder.DEFAULT_BUS;
    }

    private static class Holder {
        private static final LiveBusCore DEFAULT_BUS = new LiveBusCore();
    }

    private Map<Object, LiveEvent> mBus;

    private LiveBusCore() {
        this.mBus = new HashMap<>();
    }

    public synchronized <T> BusObservable<T> with(Object key, Class<T> eventType) {
        if (!mBus.containsKey(key)) {
            mBus.put(key, new LiveEvent<T>(key));
        }
        return (BusObservable<T>) mBus.get(key);
    }

    public synchronized void removeEvent(@NonNull Object key) {
        if (!mBus.containsKey(key)) {
            mBus.remove(key);
        }
    }

    /**
     * 当前线程是否为主线程
     *
     * @return true-主线程 false-非主线程
     */
    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    private class LiveEvent<T> implements BusObservable<T> {
        @NonNull
        private final Object key;
        private final InternalLiveData<T> liveData;
        private final Map<Observer, ObserverWrapper<T>> observerMap;
        private final Handler mainHandler;

        public LiveEvent(@NonNull Object key) {
            this.key = key;
            this.liveData = new InternalLiveData<>();
            this.observerMap = new HashMap<>();
            this.mainHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void post(final T value) {
            if (isMainThread()) {
                postInternal(value);
            } else {
                mainHandler.post(new PostValueTask(value));
            }
        }

        @Override
        public void postDelay(final T value, final long delay) {
            mainHandler.postDelayed(new PostValueTask(value), delay);
        }

        @Override
        public void postDelay(@NonNull final LifecycleOwner owner, final T value, final long delay) {
            mainHandler.postDelayed(new PostLifeValueTask(value, owner), delay);
        }

        @Override
        public void postOrderly(final T value) {
            mainHandler.post(new PostValueTask(value));
        }

        @Override
        public void observe(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer) {
            if (isMainThread()) {
                observeInternal(owner, observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeInternal(owner, observer);
                    }
                });
            }
        }

        @Override
        public void observeSticky(@NonNull final LifecycleOwner owner, @NonNull final Observer<T> observer) {
            if (isMainThread()) {
                observeStickyInternal(owner, observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeStickyInternal(owner, observer);
                    }
                });
            }
        }

        @Override
        public void observeForever(@NonNull final Observer<T> observer) {
            if (isMainThread()) {
                observeForeverInternal(observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeForeverInternal(observer);
                    }
                });
            }
        }

        @Override
        public void observeStickyForever(@NonNull final Observer<T> observer) {
            if (isMainThread()) {
                observeStickyForeverInternal(observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        observeStickyForeverInternal(observer);
                    }
                });
            }
        }

        @Override
        public void removeObserver(@NonNull final Observer<T> observer) {
            if (isMainThread()) {
                removeObserverInternal(observer);
            } else {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        removeObserverInternal(observer);
                    }
                });
            }
        }

        @Override
        public BusObservable<T> alwaysBeActive(boolean alwaysBeActive) {
            liveData.observerAlwaysBeActive = alwaysBeActive;
            return this;
        }

        @Override
        public BusObservable<T> autoClear(final boolean autoClear) {
            liveData.autoClear = autoClear;
            return this;
        }

        @MainThread
        private void postInternal(T value) {
            liveData.setValue(value);
        }

        @MainThread
        private void observeInternal(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer);
            observerWrapper.isRejectEvent = liveData.getVersion() > XLiveData.START_VERSION;
            liveData.observe(owner, observerWrapper);
        }

        @MainThread
        private void observeStickyInternal(@NonNull LifecycleOwner owner, @NonNull Observer<T> observer) {
            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer);
            liveData.observe(owner, observerWrapper);
        }

        @MainThread
        private void observeForeverInternal(@NonNull Observer<T> observer) {
            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer);
            observerWrapper.isRejectEvent = liveData.getVersion() > XLiveData.START_VERSION;
            observerMap.put(observer, observerWrapper);
            liveData.observeForever(observerWrapper);
        }

        @MainThread
        private void observeStickyForeverInternal(@NonNull Observer<T> observer) {
            ObserverWrapper<T> observerWrapper = new ObserverWrapper<>(observer);
            observerMap.put(observer, observerWrapper);
            liveData.observeForever(observerWrapper);
        }

        @MainThread
        private void removeObserverInternal(@NonNull Observer<T> observer) {
            Observer<T> realObserver;
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer);
            } else {
                realObserver = observer;
            }
            liveData.removeObserver(realObserver);
        }

        private class InternalLiveData<T> extends XLiveData<T> {
            private boolean observerAlwaysBeActive = false;
            private boolean autoClear = true;

            @Override
            protected Lifecycle.State observerActiveLevel() {
                return observerAlwaysBeActive ? Lifecycle.State.CREATED : Lifecycle.State.STARTED;
            }

            @Override
            public void removeObserver(@NonNull Observer<? super T> observer) {
                super.removeObserver(observer);
                if (autoClear && !liveData.hasObservers()) {
                    LiveBusCore.getInstance().mBus.remove(key);
                }
            }
        }

        private class PostValueTask implements Runnable {
            private Object newValue;

            public PostValueTask(@NonNull Object newValue) {
                this.newValue = newValue;
            }

            @Override
            public void run() {
                postInternal((T) newValue);
            }
        }

        private class PostLifeValueTask implements Runnable {
            private Object newValue;
            private LifecycleOwner owner;

            public PostLifeValueTask(@NonNull Object newValue, @Nullable LifecycleOwner owner) {
                this.newValue = newValue;
                this.owner = owner;
            }

            @Override
            public void run() {
                if (owner != null) {
                    if (owner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                        postInternal((T) newValue);
                    }
                }
            }
        }
    }

    private class ObserverWrapper<T> implements Observer<T> {
        @NonNull
        private Observer<T> observer;
        // 是否拒收消息
        private boolean isRejectEvent = false;

        private ObserverWrapper(@NonNull Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onChanged(T t) {
            if (isRejectEvent) {
                isRejectEvent = false;
                return;
            }
            observer.onChanged(t);
        }
    }
}
