package com.meiling.framework.base;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.meiling.framework.utils.log.Ulog;
import com.meiling.framework.utils.statusbar.QMUIStatusBarHelper;
import com.meiling.framework.utils.statusbar.StatusBarColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by marisareimu@126.com on 2021-03-19  15:40
 * project FrameworkDataBinding
 */
public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {
    protected boolean isFullScreen = true;// true 表示需要设置全屏
    protected boolean isWhiteStatusBarFontColor = false;// true 表示白色文字
    protected boolean isDoubleBackExit = false;
    protected boolean isIgnoreBackKey = false;
    protected boolean isPortrait = true;
    protected boolean keepScreenOn = false;

    @ColorInt
    protected int navigationBarColor = Color.TRANSPARENT;
    protected boolean isDarkNavigationBarButton = true;

    /**
     * todo 由databinding框架实例化生成的对应的与布局相关联的对象
     * 由于无法使用Module的形式来进行关联【跨module时无法通过。属性来获取对象，应该是跟其实现有关】，所以基类只能在启动module中
     * 不需要使用databinding框架的部分可以使用module的形式进行复用
     */
    protected T layoutBinding = null;

    public abstract void setConfiguration();

    public abstract int layoutViewId();

    public abstract void afterDestroy();

    public abstract void initView();

    public abstract void lazyLoadCallback();// 延迟后的调用

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setConfiguration();
        applyConfiguration();
        super.onCreate(savedInstanceState);
        layoutBinding = DataBindingUtil.setContentView(this, layoutViewId());
        initView();

        // 实际测试发现，该回调的调用在执行完onWindowFocusChanged方法之后【满足当界面显示完成之后进行回调】
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onCreate");
                lazyLoadCallback();
                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onWindowFocusChanged");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (keepScreenOn) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onDestroy");
        afterDestroy();
        layoutBinding.unbind();// todo 当页面销毁时，对databinding对象进行解绑操作
        layoutBinding = null;
    }

    private void applyConfiguration() {
        // 根据设置的配置信息，
        setRequestedOrientation(isPortrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置是否全屏，状态栏字体颜色是否是白色

        StatusBarColorUtil.setSystemUi(this, isFullScreen, isDarkNavigationBarButton, true);
        StatusBarColorUtil.setNaviagtionBarColor(this, navigationBarColor);
        setStatusFontColor(isWhiteStatusBarFontColor);

        // 屏幕常亮
        if (keepScreenOn) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕常亮，避免录制过程屏幕熄灭引起录制问题
        }
    }

    protected void setStatusFontColor(boolean isWhite) {
        QMUIStatusBarHelper.translucent(this);
        if (isWhite) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        }
    }

    /*
     ****************************************************************************
     * 处理双击关闭
     */

    private long firstTime = 0; // 双击退出

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isDoubleBackExit) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {
                        firstTime = secondTime;//更新firstTime
                        doubleBackExitToast();
                        return true;
                    } else {
                        //两次按键小于2秒时，退出应用
                        finish();
                        System.exit(0);
                    }
                    break;
            }
        } else if (isIgnoreBackKey) {
            return true;//忽略返回键
        } else {//未做额外处理时，返回键为关闭当前页面

        }
        return super.onKeyUp(keyCode, event);
    }

    // todo 当开启了双击退出Flag时，第一次点击【间隔超出2秒的多次点击】进行退出提示
    public void doubleBackExitToast() {

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
