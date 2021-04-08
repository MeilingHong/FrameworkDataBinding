package com.meiling.framework.base.callback;

import android.os.Message;

import androidx.annotation.NonNull;

/**
 *
 */
public interface IHandleMessage {
    void handleMessage(@NonNull Message msg);
}
