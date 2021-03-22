package com.meiling.framework.utils.statusbar;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.ColorInt;

public class StatusBarColorUtil {
    public static void setFullScreenStatusBarWhiteFontColor(Activity activity, boolean isWhite) {
        if (isWhite) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    /*
2.隐藏状态栏

  getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);

  参数：

    View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
    View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
    View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
    View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
     */
    public static void setFullScreenNoStatusBarWhiteFontColor(Activity activity, boolean isWhite) {

        if (isWhite) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
//        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    /**
     * 设置状态栏字体颜色是：白色，黑色
     *
     * @param activity
     * @param isWhite
     */
    public static void setStatusBarWhiteFontColor(Activity activity, boolean isWhite) {
        if (isWhite) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
//        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
    }

    public static void setNaviagtionBarColor(Activity activity, @ColorInt int color) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 该参数设置后会影响到设置导航栏颜色
        // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
        activity.getWindow().setNavigationBarColor(color);
    }

    //***************************************************************************************************************************************
    // 似乎没有什么作用，系统导航栏遮盖不掉

    public static void setStatusBarWhiteFontColor(Activity activity, boolean isWhite, boolean isHideNavigator) {
        if (isWhite) {
            if (isHideNavigator) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } else {
            if (isHideNavigator) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    public static void setFullScreenStatusBarWhiteFontColor(Activity activity, boolean isWhite, boolean isHideNavigator) {
        if (isWhite) {
            if (isHideNavigator) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } else {
            if (isHideNavigator) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//
    }

}
