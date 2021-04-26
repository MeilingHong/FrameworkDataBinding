package com.meiling.framework.base.lifecycle.handler;

import android.os.Handler;
import android.os.Looper;

import com.meiling.framework.base.lifecycle.FullLifecycleObserver;
import com.meiling.framework.base.lifecycle.FullLifecycleObserverAdapter;

import androidx.lifecycle.LifecycleOwner;

/**
 * @Author huangzhou@ubanquan.cn
 * @time 2021-04-26 14:36
 */
public class LifecycleHandler extends Handler implements FullLifecycleObserver {


    private LifecycleOwner mLifecycleOwner;

    public LifecycleHandler(LifecycleOwner lifecycleOwner) {
        mLifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(LifecycleOwner lifecycleOwner, Callback callback) {
        super(callback);
        mLifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(LifecycleOwner lifecycleOwner, Looper looper) {
        super(looper);
        mLifecycleOwner = lifecycleOwner;
        addObserver();
    }

    public LifecycleHandler(LifecycleOwner lifecycleOwner, Looper looper, Callback callback) {
        super(looper, callback);
        mLifecycleOwner = lifecycleOwner;
        addObserver();
    }

    private void addObserver() {
        if (mLifecycleOwner != null) {
            mLifecycleOwner.getLifecycle().
                    addObserver(new FullLifecycleObserverAdapter(mLifecycleOwner, this));
            // todo 在这里与Activity或者Fragment对应的声明周期 进行关联，
            //  当其声明周期方法回调回来的时候，会触发对应的回调方法，从而在相应方法时
            //  相关的声明周期的事件分发依赖于Adapter类来进行处理
        }
    }


    @Override
    public void onCreate(LifecycleOwner owner) {

    }

    @Override
    public void onStart(LifecycleOwner owner) {

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onStop(LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(LifecycleOwner owner) {
        removeCallbacksAndMessages(null);
    }
}
