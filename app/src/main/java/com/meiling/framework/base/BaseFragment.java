package com.meiling.framework.base;

import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meiling.framework.utils.log.Ulog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

/**
 * Created by marisareimu@126.com on 2021-03-19  15:41
 * project FrameworkDataBinding
 */

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {
    protected T layoutFragmentBinding;

    public abstract int layoutViewId();

    public abstract void afterDestroy();

    public abstract void initView();

    public abstract void lazyLoadCallback();// 延迟后的调用

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutFragmentBinding = (T)DataBindingUtil.inflate(inflater, layoutViewId(), container, false);
        initView();
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onCreateView(Fragment)");
                lazyLoadCallback();
                return false;
            }
        });
        return layoutFragmentBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onResume(Fragment)");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onDestroyView(Fragment)");
        afterDestroy();
    }
}
