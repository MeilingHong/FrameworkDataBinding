package com.meiling.framework.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.meiling.framework.R;
import com.meiling.framework.base.callback.IHandleMessage;
import com.meiling.framework.utils.log.Ulog;
import com.meiling.framework.utils.statusbar.QMUIStatusBarHelper;
import com.meiling.framework.utils.statusbar.StatusBarColorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleObserver;

/**
 * Created by marisareimu@126.com on 2021-03-19  15:40
 * project FrameworkDataBinding
 */


public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity implements LifecycleObserver {
    /*
     * LifecycleOwner 有本身继承了ComponentActivity，而ComponentActivity实现了LifecycleOwner，所以不需要额外再显示声明来实现LifecycleOwner接口
     *
     *
     * Lifecycle 本身解决的应该是需要依赖Activity或Fragment本身的声明周期进行对应资源操作的方法
     * 1、例如Handler（在onDestroy/onDestroyView中需要进行释放）
     * 2、Presenter在（在onDestroy/onDestroyView中需要进行释放）
     *
     */
    protected boolean isFullScreen = true;// true 表示需要设置全屏
    protected boolean isWhiteStatusBarFontColor = false;// true 表示白色文字
    protected boolean isDoubleBackExit = false;
    protected boolean isIgnoreBackKey = false;
    protected boolean isPortrait = true;
    protected boolean keepScreenOn = false;

    @ColorInt
    protected int customNavigationBarColor = Color.WHITE;
    /**
     * <ul>
     *     从Android 9 的表现上来看{ isDarkNavigationBarButton}似乎并没哟生效，
     *     而 { customNavigationBarColor} 的深浅直接就影响虚拟按键的颜色
     * </ul>
     */
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
//        getLifecycle().addObserver(this);
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

        StatusBarColorUtil.setSystemUi(this, isFullScreen, isDarkNavigationBarButton, true);// todo 可尝试不设置这个方法，开是否对导航按钮颜色有影响
        StatusBarColorUtil.setNaviagtionBarColor(this, customNavigationBarColor);

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
     *********************************************************************************************************
     */

    /**
     * 提供一个构件Handler的统一的方法
     *
     * @param iHandleMessage
     * @return
     */
    public Handler newHandler(@NonNull IHandleMessage iHandleMessage) {
        return new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (iHandleMessage != null) {
                    iHandleMessage.handleMessage(msg);
                }
            }
        };
    }

    /**
     * todo 移除Handler消息队列中的全部信息
     *
     * @param handler
     */
    public void removeHandlerMessages(Handler handler) {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * todo 移除Handler消息队列中的全部信息，并释放Handler对象
     *
     * @param handler
     */
    public void removeHandlerMessagesAndRelease(Handler handler) {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    /*
     *********************************************************************************************************
     */

    public void skipIntent(Bundle bundle, @NonNull Class<?> clz) {
        skipIntent(bundle, clz, -1);
    }

    /**
     * 跳转方法
     *
     * @param bundle
     * @param clz
     * @param requestCode
     */
    public void skipIntent(Bundle bundle, @NonNull Class<?> clz, @IntRange(from = -1) int requestCode) {
        Intent intent = new Intent(this, clz);
        //如果需要传参数
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        //设置页面进出动画
        this.overridePendingTransition(R.anim.up_in, R.anim.up_out);//往上进入，往上出去
        //是否进行有返回值得跳转
        if (requestCode != -1) {
            startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
    }

    /*
     *********************************************************************************************************
     */

    public void showHintCenterOrderMsg(String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                View view = LayoutInflater.from(this).inflate(R.layout.toast_center, null);
                TextView tvToast = view.findViewById(R.id.tvToast);
                tvToast.setText(msg);
                ToastUtil.toastShortOrder(this, view, Gravity.CENTER);
            }
        } catch (Exception e) {
            Looper.prepare();
            if (!TextUtils.isEmpty(msg)) {
                View view = LayoutInflater.from(this).inflate(R.layout.toast_center, null);
                TextView tvToast = view.findViewById(R.id.tvToast);
                tvToast.setText(msg);
                ToastUtil.toastShortOrder(this, view, Gravity.CENTER);
            }
            Looper.loop();
        }
    }

    public void showHintCenterOrderMsgRound100(String msg) {
        try {
            if (!TextUtils.isEmpty(msg)) {
                View view = LayoutInflater.from(this).inflate(R.layout.toast_round_100_center, null);
                TextView tvToast = view.findViewById(R.id.tvToast);
                tvToast.setText(msg);
                ToastUtil.toastShortOrder(this, view, Gravity.CENTER);
            }
        } catch (Exception e) {
            Looper.prepare();
            if (!TextUtils.isEmpty(msg)) {
                View view = LayoutInflater.from(this).inflate(R.layout.toast_round_100_center, null);
                TextView tvToast = view.findViewById(R.id.tvToast);
                tvToast.setText(msg);
                ToastUtil.toastShortOrder(this, view, Gravity.CENTER);
            }
            Looper.loop();
        }
    }

    /*
     ****************************************************************************
     * 权限相关处理【实际测试时，发现，在Android5.1系统中，由于无法使用该方式进行权限申请，需要进行修改】
     */
    protected final int REQUEST_CODE_PERMISSION = 10086;
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
        if (requestCode == REQUEST_CODE_PERMISSION) {
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
