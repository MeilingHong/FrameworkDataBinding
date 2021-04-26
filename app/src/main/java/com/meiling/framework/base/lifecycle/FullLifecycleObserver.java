package com.meiling.framework.base.lifecycle;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * @Author huangzhou@ubanquan.cn
 * @time 2021-04-26 14:25
 */
public interface FullLifecycleObserver extends LifecycleObserver {

    void onCreate(LifecycleOwner owner);

    void onStart(LifecycleOwner owner);

    void onResume(LifecycleOwner owner);

    void onPause(LifecycleOwner owner);

    void onStop(LifecycleOwner owner);

    void onDestroy(LifecycleOwner owner);
}
