package com.dim.library.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * 文件描述：判断某APP是否安装
 * 作者：dim
 * 创建时间：2020/4/9
 * 更改时间：2020/4/9
 * 版本号：1
 */
public class CheckApkExist {

    /**
     * 判断某个APP是否存在
     *
     * @param context     Context对象
     * @param packageName 要打开的app包名
     * @return APP是否存在
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            DLog.d(info.toString());
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            DLog.d(e.toString());
            return false;
        }
    }

    /**
     * 打开某个APP
     *
     * @param context     Context对象
     * @param packageName 要打开的app包名
     * @return APP是否成功打开
     */
    public static boolean openApkWithPackageName(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        if (!checkApkExist(context, packageName)) {
            return false;
        }
        Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        context.startActivity(LaunchIntent);
        return true;
    }
}