package com.meiling.framework.utils.log;

import android.util.Log;


public class Ulog {

    private static boolean DEBUG = false;
    private static final int LENGTH_LIMIT = 2000;
    private static final String TAG = "_AndroidRuntime";//方面能够同时查看到异常信息

    public static void setDEBUG(boolean DEBUG) {
        Ulog.DEBUG = DEBUG;
    }

    public static void e(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.e(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.e(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.e(TAG, msg.toString());
                }
            }
        }
    }

    public static void w(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.w(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.w(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.w(TAG, msg.toString());
                }
            }
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.i(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.i(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.i(TAG, msg.toString());
                }
            }
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            if (msg != null && msg.length() > 0) {
                if (msg.length() > LENGTH_LIMIT) {
                    int chunkCount = msg.length() / LENGTH_LIMIT;     // integer division
                    for (int i = 0; i <= chunkCount; i++) {
                        int max = LENGTH_LIMIT * (i + 1);
                        if (max >= msg.length()) {
                            Log.d(TAG, msg.substring(LENGTH_LIMIT * i));
                        } else {
                            Log.d(TAG, msg.substring(LENGTH_LIMIT * i, max));
                        }
                    }
                } else {
                    Log.d(TAG, msg.toString());
                }
            }
        }
    }

    // 打印异常信息
    public static void printThrowableStackTrace(Throwable msg) {
        if (msg != null &&
                msg.getStackTrace() != null &&
                msg.getStackTrace().length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("异常：").
                    append(msg.getMessage()).
                    append("\n");
            StackTraceElement[] stackTraceElements = msg.getStackTrace();
            for (int i = 0; i < stackTraceElements.length; i++) {
                if (stackTraceElements[i] != null) {
                    stringBuilder.append(stackTraceElements[i].getClassName());
                    stringBuilder.append(".");
                    stringBuilder.append(stackTraceElements[i].getMethodName());
                    stringBuilder.append("(");
                    stringBuilder.append(stackTraceElements[i].getFileName());
                    stringBuilder.append(".");
                    stringBuilder.append(stackTraceElements[i].getLineNumber());
                    stringBuilder.append(");\n");
                }
            }
            e(stringBuilder.toString());
        }
    }

    public static String getThrowableStackTrace(Throwable msg) {
        if (msg != null &&
                msg.getStackTrace() != null &&
                msg.getStackTrace().length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("异常：").
                    append(msg.getMessage()).
                    append("\n");
            StackTraceElement[] stackTraceElements = msg.getStackTrace();
            for (int i = 0; i < stackTraceElements.length; i++) {
                if (stackTraceElements[i] != null) {
                    stringBuilder.append(stackTraceElements[i].getClassName());
                    stringBuilder.append(".");
                    stringBuilder.append(stackTraceElements[i].getMethodName());
                    stringBuilder.append("(");
                    stringBuilder.append(stackTraceElements[i].getFileName());
                    stringBuilder.append(".");
                    stringBuilder.append(stackTraceElements[i].getLineNumber());
                    stringBuilder.append(");\n");
                }
            }
            return stringBuilder.toString();
        }
        return "";
    }
}
