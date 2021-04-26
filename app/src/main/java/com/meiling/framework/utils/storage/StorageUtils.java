package com.meiling.framework.utils.storage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.meiling.framework.utils.log.Ulog;

/**
 * @Author huangzhou@ubanquan.cn
 * @time 2021-04-15 09:37
 */
public class StorageUtils {
    private static long getAvailableSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return fileStats.getAvailableBlocksLong() * fileStats.getBlockSizeLong(); // 注意与fileStats.getFreeBlocks()的区别
    }

    private static long getTotalSize(String path) {
        StatFs fileStats = new StatFs(path);
        fileStats.restat(path);
        return fileStats.getBlockCountLong() * fileStats.getBlockSizeLong();
    }

    public static long getSDAvailableSize(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (Build.VERSION.SDK_INT >= 29) {
                if (context.getExternalFilesDir(null) != null) {
                    Ulog.w("context.getExternalFilesDir:");
                    return getAvailableSize(context.getExternalFilesDir(null).toString());
                } else {
                    Ulog.w("context.getFilesDir:");
                    return getAvailableSize(context.getFilesDir().toString());
                }
            } else {
                Ulog.w("Environment.getExternalStorageDirectory:");
                return getAvailableSize(Environment.getExternalStorageDirectory().toString());
            }
        }
        Ulog.w("Environment.getRootDirectory:");
        return getAvailableSize(Environment.getRootDirectory().toString());
    }

    public static long getSystemAvailableSize(Context context) {
        // context.getFilesDir().getAbsolutePath();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Ulog.w("context.getFilesDir:");
            return getAvailableSize(context.getFilesDir().toString());
        }
        Ulog.w("Environment.getRootDirectory:");
        return getAvailableSize(Environment.getRootDirectory().toString());
    }

    /**
     * 获取SD卡的总空间
     *
     * @return
     */
    public static long getSDTotalSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return getTotalSize(Environment.getExternalStorageDirectory().toString());
        }
        return 0;
    }
}
