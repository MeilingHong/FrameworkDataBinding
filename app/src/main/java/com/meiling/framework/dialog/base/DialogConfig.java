package com.meiling.framework.dialog.base;

import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.meiling.framework.dialog.callback.IDialogDismissCallback;
import com.meiling.framework.dialog.callback.IDialogShowCallback;

public class DialogConfig {
    /**
     * 配置的Dialog使用的样式，当这个Style不符合规范时，使用一个默认的style
     */
    private int styleId = 0;
    /**
     * 配置dialog的显示位置，默认显示位置在下面
     */
    private int showPositionInScreen = Gravity.BOTTOM;
    /**
     * 对话框内容布局
     */
    private int contentViewLayout = 0;
    /**
     * 默认的输入模式
     */
    private int inputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
    /**
     * 是否可以取消
     */
    private boolean cancelable = false;
    /**
     * 是否可以点击外部取消
     */
    private boolean cancelOutside = false;
    /**
     * 对话框的宽
     */
    private int dialogWidth = 0;
    /**
     * 对话框的高
     */
    private int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 是否可以通过back键来关闭Dialog  true 为可以取消，false 为不可通过back键取消
     */
    private boolean cancelBackKey = false;
    /**
     * 是否延时关闭
     */
    private boolean isAutoClose;
    /**
     * 延时多少毫秒后关闭
      */
    private long autoCloseDelayTimeMillisec;

    /**
     * 对话框显示后的回调
     */
    private IDialogShowCallback showCallback;

    /**
     * 对话框关闭后的回调
     */
    private IDialogDismissCallback dismissCallback;


    public int getStyleId() {
        return styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    public int getShowPositionInScreen() {
        return showPositionInScreen;
    }

    public void setShowPositionInScreen(int showPositionInScreen) {
        this.showPositionInScreen = showPositionInScreen;
    }

    public int getContentViewLayout() {
        return contentViewLayout;
    }

    public void setContentViewLayout(int contentViewLayout) {
        this.contentViewLayout = contentViewLayout;
    }

    public int getInputMode() {
        return inputMode;
    }

    public void setInputMode(int inputMode) {
        this.inputMode = inputMode;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public boolean isCancelOutside() {
        return cancelOutside;
    }

    public void setCancelOutside(boolean cancelOutside) {
        this.cancelOutside = cancelOutside;
    }

    public int getDialogWidth() {
        return dialogWidth;
    }

    public void setDialogWidth(int dialogWidth) {
        this.dialogWidth = dialogWidth;
    }

    public int getDialogHeight() {
        return dialogHeight;
    }

    public void setDialogHeight(int dialogHeight) {
        this.dialogHeight = dialogHeight;
    }

    public boolean isCancelBackKey() {
        return cancelBackKey;
    }

    public void setCancelBackKey(boolean cancelBackKey) {
        this.cancelBackKey = cancelBackKey;
    }

    public boolean isAutoClose() {
        return isAutoClose;
    }

    public void setAutoClose(boolean autoClose) {
        isAutoClose = autoClose;
    }

    public long getAutoCloseDelayTimeMillisec() {
        return autoCloseDelayTimeMillisec;
    }

    public void setAutoCloseDelayTimeMillisec(long autoCloseDelayTimeMillisec) {
        this.autoCloseDelayTimeMillisec = autoCloseDelayTimeMillisec;
    }

    public IDialogShowCallback getShowCallback() {
        return showCallback;
    }

    public void setShowCallback(IDialogShowCallback showCallback) {
        this.showCallback = showCallback;
    }

    public IDialogDismissCallback getDismissCallback() {
        return dismissCallback;
    }

    public void setDismissCallback(IDialogDismissCallback dismissCallback) {
        this.dismissCallback = dismissCallback;
    }

    public static class Builder {
        private int styleId = 0;
        private int showPositionInScreen = Gravity.BOTTOM;
        private int contentViewLayout = 0;
        private int inputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

        private boolean cancelable = false;
        private boolean cancelOutside = false;
        private boolean cancelBackKey = true;

        private int dialogWidth = 0;
        private int dialogHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        private IDialogShowCallback showCallback = null;
        private IDialogDismissCallback dismissCallback = null;

        /**
         * 设置自动关闭的时间与开关标志
         */
        private boolean isAutoClose = false;
        private long autoCloseDelayTimeMillisec = 2000;

        public Builder setDialogStyle(int styleId) {
            this.styleId = styleId;
            return this;
        }

        public Builder setDialogShowPosition(int showPositionInScreen) {
            this.showPositionInScreen = showPositionInScreen;
            return this;
        }

        public Builder setContentViewLayout(int contentViewLayout) {
            this.contentViewLayout = contentViewLayout;
            return this;
        }

        public Builder setInputMode(int inputMode) {
            this.inputMode = inputMode;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }
        public Builder setCancelOutside(boolean cancelOutside) {
            this.cancelOutside = cancelOutside;
            return this;
        }

        /**
         * true 为可以取消，false 为不可通过back键取消
         * @param cancelBackKey
         * @return
         */
        public Builder setCancelForBackKey(boolean cancelBackKey) {
            this.cancelBackKey = cancelBackKey;
            return this;
        }

        public Builder setDialogWidth(int dialogWidth) {
            this.dialogWidth = dialogWidth;
            return this;
        }

        public Builder setDialogHeight(int dialogHeight) {
            this.dialogHeight = dialogHeight;
            return this;
        }

        public Builder isAutoClose(boolean autoClose) {
            this.isAutoClose = autoClose;
            return this;
        }

        public Builder setCloseDelayTime(long milSecond) {
            this.autoCloseDelayTimeMillisec = milSecond;
            return this;
        }

        public Builder setShowCallback(IDialogShowCallback iDialogShowCallback){
            this.showCallback = iDialogShowCallback;
            return this;
        }

        public Builder setDismissCallback(IDialogDismissCallback iDialogDismissCallback){
            this.dismissCallback = iDialogDismissCallback;
            return this;
        }


        public DialogConfig build(){
            DialogConfig dialogConfig = new DialogConfig();
            if(styleId!=0){
                dialogConfig.setStyleId(styleId);
            }
            if(showPositionInScreen!=0){
                dialogConfig.setShowPositionInScreen(showPositionInScreen);
            }
            if(contentViewLayout!=0){
                dialogConfig.setContentViewLayout(contentViewLayout);
            }
            if(inputMode!=0){
                dialogConfig.setInputMode(inputMode);
            }
            //
            dialogConfig.setCancelable(cancelable);
            dialogConfig.setCancelOutside(cancelOutside);
            dialogConfig.setCancelBackKey(cancelBackKey);
            //
            dialogConfig.setDialogWidth(dialogWidth);
            dialogConfig.setDialogHeight(dialogHeight);
            //
            dialogConfig.setAutoClose(isAutoClose);
            dialogConfig.setAutoCloseDelayTimeMillisec(autoCloseDelayTimeMillisec);
            //
            dialogConfig.setShowCallback(showCallback);
            dialogConfig.setDismissCallback(dismissCallback);

            return dialogConfig;
        }

    }
}
