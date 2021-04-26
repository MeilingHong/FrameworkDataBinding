package com.meiling.framework.base.lifecycle;

import com.meiling.framework.utils.log.Ulog;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @Author huangzhou@ubanquan.cn
 * @time 2021-04-26 14:29
 */
public class FullLifecycleObserverAdapter implements LifecycleObserver {
    private final FullLifecycleObserver mObserver;
    private final LifecycleOwner mLifecycleOwner;
    public FullLifecycleObserverAdapter(LifecycleOwner lifecycleOwner, FullLifecycleObserver observer) {
        mLifecycleOwner = lifecycleOwner;
        mObserver = observer;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        Ulog.i("FullLifecycleObserverAdapter onCreate: ");
        mObserver.onCreate(mLifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Ulog.i("FullLifecycleObserverAdapter onStart: ");
        mObserver.onStart(mLifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Ulog.i("FullLifecycleObserverAdapter onResume: ");
        mObserver.onResume(mLifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Ulog.i("FullLifecycleObserverAdapter onPause: ");
        mObserver.onPause(mLifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        Ulog.i("FullLifecycleObserverAdapter onStop: ");
        mObserver.onStop(mLifecycleOwner);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Ulog.i("FullLifecycleObserverAdapter onDestroy: ");
        mObserver.onDestroy(mLifecycleOwner);
    }
}
