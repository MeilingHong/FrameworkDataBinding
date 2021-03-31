package com.meiling.framework.base;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class ToastUtil {
    public static void toastShort(Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastLong(Context context, @NonNull String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toastShortCenter(Context context, @NonNull String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastLongCenter(Context context, @NonNull String message) {
        Toast toast =  Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void toastShortOrder(Context context, View view, int gravity) {
        Toast toast = new Toast(context);
        toast.setView(view);
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

}
