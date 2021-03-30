package com.meiling.framework.app.dialog.loading;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.meiling.framework.R;
import com.meiling.framework.databinding.DialogLoadingBinding;
import com.meiling.framework.dialog.base.BaseFragmentDialog;
import com.meiling.framework.dialog.base.DialogConfig;
import com.meiling.framework.dialog.callback.IDialogDismissCallback;
import com.meiling.framework.dialog.callback.IDialogShowCallback;
import com.meiling.framework.utils.datahandle.UnitExchangeUtil;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

public class LoadingDialog extends BaseFragmentDialog<DialogLoadingBinding> {
    private String hintMsg;
    private boolean isCancel = true;

    @Override
    public void setDialogConfig(@NonNull Context context, IDialogShowCallback iDialogShowCallback, IDialogDismissCallback iDialogDismissCallback) {
        this.config = new DialogConfig.Builder().
                setDialogStyle(R.style.Dialog_NoTitle_AlphaIn2).
                setDialogShowPosition(/*Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL*/Gravity.CENTER).
                setContentViewLayout(R.layout.dialog_loading).
                setCancelable(isCancel).
                setCancelOutside(isCancel).
                setCancelForBackKey(isCancel).
                setShowCallback(iDialogShowCallback).
                setDismissCallback(iDialogDismissCallback).
                setDialogWidth(UnitExchangeUtil.dip2px(context, 125)).
                setDialogHeight(UnitExchangeUtil.dip2px(context, 125)).
                build();
    }

    @Override
    public void initContentView(View view) {
        layoutDialogBinding.pbLoading.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(hintMsg)) {
            layoutDialogBinding.tvLoadingHint.setVisibility(View.GONE);
        } else {
            layoutDialogBinding.tvLoadingHint.setVisibility(View.VISIBLE);
            layoutDialogBinding.tvLoadingHint.setText(hintMsg);
        }
    }

    public void setHintMsg(String hintMsg) {
        this.hintMsg = hintMsg;
    }

    public void modifyHintMsg(String hintMsg) {
        // Condition 'layoutDialogBinding.tvLoadingHint != null' is always 'true'
        // DataBinding形式的使用将使得针对View的非空判断成为不必要的条件
        if (layoutDialogBinding.tvLoadingHint != null) {
            layoutDialogBinding.tvLoadingHint.setText(hintMsg);
        }
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void show(FragmentManager manager, String tag, int delayTime) {
        super.show(manager, tag);
    }
}
