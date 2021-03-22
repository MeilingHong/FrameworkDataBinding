package com.meiling.framework.widget.status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.meiling.framework.utils.statusbar.QMUIStatusBarHelper;

import androidx.annotation.Nullable;


/**
 * Created by kiun_2007 on 2018/3/29.
 */

public class StatusLayout extends LinearLayout {
    public StatusLayout(Context context) {
        this(context, null);
    }

    public StatusLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onAttachedToWindow() {
        ViewGroup.LayoutParams lp = this.getLayoutParams();
        lp.width = -1;
        lp.height = QMUIStatusBarHelper.getStatusbarHeight(getContext());
        this.setLayoutParams(lp);
        super.onAttachedToWindow();
    }
}
