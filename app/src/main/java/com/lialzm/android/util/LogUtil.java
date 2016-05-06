package com.lialzm.android.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;


/**
 * Created by lcy on 2016/3/24.
 */
public class LogUtil {

    private static LogUtil logUtil;
    private static String mTag;
    private static boolean mIsOpen = true;

    public static void d(@Nullable String info, Object... args) {
        if (!mIsOpen) { // 如果把开关关闭了，那么就不进行打印
            return;
        }
        Logger.d(info, args);
    }

    /*public static void d(@Nullable String info) {
        Log.d("find", info);
    }*/


    private static String getClassName() {
        String result;
        // 这里的数组的index2是根据你工具类的层级做不同的定义，这里仅仅是关键代码
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result = thisMethodStack.getClassName();
        int lastIndex = result.lastIndexOf(".");
        result = result.substring(lastIndex + 1, result.length());
        return result;
    }

    public static void d(@NonNull String tag, String info, Object... args) {
        Logger.t(tag).d(info, args);
    }

}
