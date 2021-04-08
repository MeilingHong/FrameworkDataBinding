package com.meiling.framework.base.callback;

import android.view.View;

import com.meiling.framework.utils.log.Ulog;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author huangzhou@ubanquan.cn
 * @time 2021-04-08 17:10
 */
public abstract class BaseClickPresenter implements View.OnClickListener {
    private ConcurrentHashMap<Integer, Long> clickView;
    private int INTERVAL = 2000;

    /*
    但是调用时，仍然按照这种方式进行调用
    todo android:onClick="@{(view)->presenter.onViewClicked(view)}"
     */
    public BaseClickPresenter() {// 默认两次点击之间需要间隔2秒才会进行响应
        clickView = new ConcurrentHashMap<>();
        INTERVAL = 2000;
    }

    public BaseClickPresenter(int intervalTime) {
        clickView = new ConcurrentHashMap<>();
        if (intervalTime < 1000) {
            INTERVAL = 1000;
            return;
        }
        if (intervalTime > 5000) {
            INTERVAL = 5000;
            return;
        }
        INTERVAL = intervalTime;
    }

    public void onViewClicked(View view) {
        if (view == null) {
            return;
        }
//        synchronized (this) {// 理论上，不进设置同步代码块也不会有影响
        long currentSystemTime = System.currentTimeMillis();// 获取当前时间
        Long lastTime = clickView.get(view.getId());//
        if (lastTime == null || currentSystemTime - lastTime > INTERVAL) {
            clickView.put(view.getId(), currentSystemTime);
            onClick(view);
        } else {
            Ulog.w("----屏蔽当前View的点击事件" + view.getId() + "---" + (lastTime != null ? lastTime : 0) + "---" + currentSystemTime);
        }
//        }
    }
}
