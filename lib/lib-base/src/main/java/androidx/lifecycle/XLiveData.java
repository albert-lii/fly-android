package androidx.lifecycle;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static androidx.lifecycle.Lifecycle.State.DESTROYED;
import static androidx.lifecycle.Lifecycle.State.STARTED;

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/2 4:44 PM
 * @description: 修改LiveData的活跃状态标准，把默认的 STARTED 改为 CREATED
 * @since: 1.0.0
 */
public class XLiveData<T> extends MutableLiveData<T> {
    public static final int START_VERSION = LiveData.START_VERSION;

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
//        super.observe(owner, observer);
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            // ignore
            return;
        }
        try {
            LifecycleBoundObserver wrapper = new XLifecycleBoundObserver(owner, (Observer<T>) observer);
            LifecycleBoundObserver existing = (LifecycleBoundObserver) callMethodPutIfAbsent(observer, wrapper);
            if (existing != null && !existing.isAttachedTo(owner)) {
                throw new IllegalArgumentException("Cannot add the same observer"
                        + " with different lifecycles");
            }
            if (existing != null) {
                return;
            }
            owner.getLifecycle().addObserver(wrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getVersion() {
        return super.getVersion();
    }

    /**
     * determine when the observer is active, means the observer can receive message
     * the default value is STARTED, means if the observer's state is above start,
     * for example, the onStart() of activity is called
     * you can change this value to CREATED/STARTED/RESUMED
     * determine on witch state, you can receive message
     *
     * @return Lifecycle.State
     */
    protected Lifecycle.State observerActiveLevel() {
        return STARTED;
    }

    /**
     * 使用反射完成 LiveData 内部的这句 ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
     */
    private Object callMethodPutIfAbsent(Object observer, Object wrapper) throws Exception {
        Object mObservers = getFieldObservers();
        Class<?> classOfSafeIterableMap = mObservers.getClass();
        Method putIfAbsent = classOfSafeIterableMap.getDeclaredMethod("putIfAbsent",
                Object.class, Object.class);
        putIfAbsent.setAccessible(true);
        return putIfAbsent.invoke(mObservers, observer, wrapper);
    }

    private Object getFieldObservers() throws Exception {
        Field fieldObservers = LiveData.class.getDeclaredField("mObservers");
        fieldObservers.setAccessible(true);
        return fieldObservers.get(this);
    }

    private class XLifecycleBoundObserver extends LifecycleBoundObserver {

        XLifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<T> observer) {
            super(owner, observer);
        }

        @Override
        boolean shouldBeActive() {
            return mOwner.getLifecycle().getCurrentState().isAtLeast(observerActiveLevel());
        }
    }
}
