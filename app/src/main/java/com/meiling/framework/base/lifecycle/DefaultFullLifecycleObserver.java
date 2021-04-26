package com.meiling.framework.base.lifecycle;

import androidx.lifecycle.LifecycleOwner;

/**
 * @Author huangzhou@ubanquan.cn
 * @time 2021-04-26 14:41
 */
public abstract class DefaultFullLifecycleObserver implements FullLifecycleObserver {
    /*
     * todo 由于Java本身没有多继承，所以，除非串行继承，否则，在一个父类（非接口类）的基础上没办法实现
     *  只针对需要的回调方法进行实现的操作
     */
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

    }
}
