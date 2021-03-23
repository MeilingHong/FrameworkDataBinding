package com.meiling.framework.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.KeyEvent;
import android.view.View;

import com.meiling.framework.utils.log.Ulog;
import com.meiling.framework.utils.statusbar.QMUIStatusBarHelper;
import com.meiling.framework.utils.statusbar.StatusBarColorUtil;

import androidx.annotation.ColorInt;
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

    @ColorInt
    protected int navigationBarColor = Color.TRANSPARENT;
    protected boolean isDarkNavigationBarButton = true;
    /**
     * todo 由databinding框架实例化生成的对应的与布局相关联的对象
     * 由于无法使用Module的形式来进行关联【跨module时无法通过。属性来获取对象，应该是跟其实现有关】，所以基类只能在启动module中
     * 不需要使用databinding框架的部分可以使用module的形式进行复用
     */
    protected T layoutBinding;

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
        Ulog.w(getClass().getName() + "---" + Thread.currentThread().getName() + "--- onDestroy");
        afterDestroy();
        layoutBinding.unbind();
    }

    private void applyConfiguration() {
        // 根据设置的配置信息，
        setRequestedOrientation(isPortrait ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //设置是否全屏，状态栏字体颜色是否是白色

        StatusBarColorUtil.setSystemUi(this,isFullScreen,isDarkNavigationBarButton,true);
        StatusBarColorUtil.setNaviagtionBarColor(this,navigationBarColor);
        setStatusFontColor(isWhiteStatusBarFontColor);
    }

    protected void setStatusFontColor(boolean isWhite) {
        QMUIStatusBarHelper.translucent(this);
        if (isWhite) {
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        }
    }

    private long firstTime = 0; // 双击退出

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (isDoubleBackExit) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    long secondTime = System.currentTimeMillis();
                    if (secondTime - firstTime > 2000) {
                        firstTime = secondTime;//更新firstTime
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
}
