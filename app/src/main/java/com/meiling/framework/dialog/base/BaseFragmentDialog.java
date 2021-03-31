package com.meiling.framework.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.meiling.framework.R;
import com.meiling.framework.dialog.callback.IDialogDismissCallback;
import com.meiling.framework.dialog.callback.IDialogShowCallback;
import com.meiling.framework.dialog.config.DialogConfig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * Created by marisareimu@126.com on 2021-03-19  15:47
 * project FrameworkDataBinding
 */

public abstract class BaseFragmentDialog<T extends ViewDataBinding> extends DialogFragment {

    protected T layoutDialogBinding;
    protected DialogConfig config;
    protected Handler mDelayCloseHandler = null;
    private Dialog dialogInstance;

    public BaseFragmentDialog() {
    }

    /**
     * 配置Config的相关参数，不同的子类对话框，设置的参数会有不同
     */
    public abstract void setDialogConfig(@NonNull Context context, IDialogShowCallback iDialogShowCallback, IDialogDismissCallback iDialogDismissCallback);

    /**
     * 子类实现自定义的View【子类实现这个方法用于实例化特定的业务样式】
     *
     * @param view
     */
    public abstract void initContentView(View view);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().setSoftInputMode(config == null || config.getInputMode() == 0 ? WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN : config.getInputMode());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogInstance = new Dialog(getActivity(), config == null || config.getStyleId() == 0 ? R.style.Dialog_NoTitle_AlphaIn : config.getStyleId());//使用一个默认样式，
        dialogInstance.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogInstance.setCancelable(config == null ? true : config.isCancelable());
        dialogInstance.setCanceledOnTouchOutside(config == null ? true : config.isCancelOutside());
        //处理返回键是否可以使得Dialog消失
        dialogInstance.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//返回键,除返回键之外的HOME、MENU事件会由系统进行处理而不会被传递到这里来
                    return cancelableForBackKeyEvent();
                } else {
                    return false;
                }
            }
        });
        /**
         * @Description 由于一个在Dialog显示时同时显示键盘的需求，感觉设置这个回调还是有点必要的
         */
        dialogInstance.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (config != null && config.getShowCallback() != null) {
                    config.getShowCallback().afterDialogShow();
                }
            }
        });
        /**
         *
         */
        dialogInstance.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismissCallback();
            }
        });
        dialogInstance.setContentView(initView());
        return dialogInstance;
    }

    public View initView() {
        layoutDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), config != null && config.getContentViewLayout() != 0 ? config.getContentViewLayout() : R.layout.dialog_toast_alpha, null, false);
        initContentView(layoutDialogBinding.getRoot());
        return layoutDialogBinding.getRoot();
    }

    /**
     * 常用的几种值为
     * Gravity.CENTER
     * Gravity.TOP
     * Gravity.LEFT
     * Gravity.RIGHT
     * Gravity.BOTTOM
     *
     * @param mGravity
     */
    public void setShowGravity(int mGravity) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setGravity(mGravity);
        } else if (dialogInstance != null && dialogInstance.getWindow() != null) {
            dialogInstance.getWindow().setGravity(mGravity);
        }
    }

    /**
     * 设置Dialog显示的宽高
     *
     * @param widthPixels
     * @param heightPixels
     */
    public void setDialogWidthHeight(int widthPixels, int heightPixels) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(widthPixels, heightPixels);
        } else if (dialogInstance != null && dialogInstance.getWindow() != null) {
            dialogInstance.getWindow().setLayout(widthPixels, heightPixels);
        }
    }

    public int getDefaultWidth() {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels * 0.8);
    }

    public int getDefaultWidth(float radius) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels * (radius > 1 || radius <= 0 ? 1 : radius));
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * 设置对话框的宽高
         */
        if (config != null && (config.getDialogWidth() > 1 ||
                config.getDialogWidth() == ViewGroup.LayoutParams.WRAP_CONTENT ||
                config.getDialogWidth() == ViewGroup.LayoutParams.MATCH_PARENT) && (config.getDialogHeight() > 1 ||
                config.getDialogHeight() == ViewGroup.LayoutParams.WRAP_CONTENT ||
                config.getDialogHeight() == ViewGroup.LayoutParams.MATCH_PARENT)) {
            setDialogWidthHeight(config.getDialogWidth(), config.getDialogHeight());
        } else {
            setDialogWidthHeight(getDefaultWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        /**
         * 设置对话框位置
         */
        if (config != null && config.getShowPositionInScreen() != 0) {
            setShowGravity(config.getShowPositionInScreen());
        } else {
            setShowGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        }
        /**
         * 设置对话框是否自动关闭
         */
        if (config != null && config.isAutoClose() && config.getAutoCloseDelayTimeMillisec() > 0) {
            mDelayCloseHandler = new Handler();
            mDelayCloseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, config.getAutoCloseDelayTimeMillisec());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDelayCloseHandler != null) {//避免造成内存泄漏或者意想不到的结果
            mDelayCloseHandler.removeCallbacksAndMessages(null);
            mDelayCloseHandler = null;
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissCallback();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mDelayCloseHandler != null) {//避免造成内存泄漏或者意想不到的结果
            mDelayCloseHandler.removeCallbacksAndMessages(null);
            mDelayCloseHandler = null;
        }
        layoutDialogBinding.unbind();
        layoutDialogBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private boolean cancelableForBackKeyEvent() {
        return config != null ? !config.isCancelBackKey() :
                false;//使得back的事件能够传递下去
    }

    private void dismissCallback() {
        if (config != null && config.getDismissCallback() != null) {
            config.getDismissCallback().afterDialogDismiss();
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //解决  IllegalStateException: Can not perform this action after onSaveInstanceState：
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        try {
            if (getFragmentManager() != null) {// 当获取不到时，在源码层级会报空指针异常导致Crash
                super.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
