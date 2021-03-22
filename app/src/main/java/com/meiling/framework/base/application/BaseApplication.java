package com.meiling.framework.base.application;


import com.meiling.framework.utils.log.Ulog;

import androidx.multidex.MultiDexApplication;

/**
 * Created by huangzhou@ulord.net on 2021-03-19  15:17
 * project FrameworkDataBinding
 */
public class BaseApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    private void initLog() {
        Ulog.setDEBUG(true);
    }
}
