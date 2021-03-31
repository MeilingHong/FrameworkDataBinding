package com.meiling.framework.base;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meiling.framework.utils.log.Ulog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    protected T layoutFragmentBinding = null;

    public abstract int layoutViewId();

    public abstract void afterDestroy();

    public abstract void initView();

    public abstract void lazyLoadCallback();// 延迟后的调用

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutFragmentBinding = (T) DataBindingUtil.inflate(inflater, layoutViewId(), container, false);
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
        layoutFragmentBinding.unbind();
        layoutFragmentBinding = null;
    }

    /*
     ****************************************************************************
     * 权限相关处理
     */
    protected int REQUEST_PERMISSION_CODE = 10086;
    private AtomicBoolean isRequestPermissionFinish = new AtomicBoolean(true);

    public synchronized void commonRequestPermission(String[] permissionString, int requestCode) {
        if (!isRequestPermissionFinish.get()) {
            requestPermissionIgnore(requestCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isRequestPermissionFinish.get()) {
            if (permissionString != null && permissionString.length > 0) {
                int size = permissionString.length;
                for (int i = 0; i < size; i++) {
                    if (TextUtils.isEmpty(permissionString[i])) {// 当权限参数不为空时
                        requestPermissionIllegalArgument(requestCode);
                        return;
                    }
                }
            }
            isRequestPermissionFinish.set(false);
            requestPermissions(permissionString, requestCode);
        } else {// 不是Android6或以上版本，则默认权限申请直接通过
            requestPermissionSuccess(requestCode);
        }
    }

    // todo 表示，权限请求传入参数不合法【避免直接崩导致问题】
    public void requestPermissionIllegalArgument(int requestCode) {

    }

    // todo 表示，当前权限申请被忽略，上一个申请尚未回调完成
    public void requestPermissionIgnore(int requestCode) {

    }

    public void requestPermissionSuccess(int requestCode) {

    }

    public void requestPermissionFailure(List<String> deniedPermission, int requestCode) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            isRequestPermissionFinish.set(true);// 控制
            List<String> deniedPermission = new ArrayList<>();
            int size = permissions.length;
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                    if (!(PackageManager.PERMISSION_GRANTED == grantResults[i])) {
                        deniedPermission.add(permissions[i]);
                    }
                }
            }
            if (deniedPermission != null && deniedPermission.size() > 0) {
                requestPermissionFailure(deniedPermission, requestCode);
            } else {
                requestPermissionSuccess(requestCode);
            }
        }
    }
}
