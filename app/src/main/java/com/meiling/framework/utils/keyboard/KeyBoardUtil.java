package com.meiling.framework.utils.keyboard;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

public class KeyBoardUtil {
    private static KeyBoardUtil instance;
    private InputMethodManager mInputMethodManager;
    private WeakReference<Activity> mActivity;

    private KeyBoardUtil() {

    }

    public static KeyBoardUtil getInstance() {
        if (instance == null) {
            instance = new KeyBoardUtil();
        }
        return instance;
    }

    /**
     * 强制显示输入法
     */
    public void show(Activity activity) {
        if (activity != null) {
            if (mInputMethodManager == null) mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!isSoftInputShow(activity)) {
                show(activity.getWindow().getCurrentFocus());
            }
        }
    }

    /**
     * 判断当前软键盘是否打开
     *
     * @param activity
     * @return
     */
    public static boolean isSoftInputShow(Activity activity) {
        // 虚拟键盘隐藏 判断view是否为空
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
//       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);

            return inputmanger.isActive() && activity.getWindow().getCurrentFocus() != null;
        }
        return false;
    }

    /**
     * Show.
     *
     * @param view the view
     */
    public void show(View view) {
        mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 强制关闭输入法
     */
    public void hide() {
        Activity activity = mActivity.get();
        if (activity != null) {
            if (isSoftInputShow(activity)) {
                hide(activity.getWindow().getCurrentFocus());
            }
        }
    }

    /**
     * Hide.
     *
     * @param view the view
     */
    public void hide(View view) {
        mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 如果输入法已经显示，那么就隐藏它；如果输入法现在没显示，那么就显示它
     */
    public void showOrHide() {
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /*
     ************************************************************************************************************
     * Flag默认使用0即可
     */

    public void show(Activity activity, View view, int flag) {
        if (activity != null) {
            if (mInputMethodManager == null) mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!isSoftInputShow(activity)) {
                mInputMethodManager.showSoftInput(view, flag);
            }
        }
    }

    public void hide(Activity activity, View view, int flag) {
        if (activity != null) {
            if (mInputMethodManager == null) mInputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (!isSoftInputShow(activity)) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), flag);
            }
        }
    }
}
