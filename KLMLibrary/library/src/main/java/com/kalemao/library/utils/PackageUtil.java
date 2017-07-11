package com.kalemao.library.utils;

import com.kalemao.library.base.RunTimeData;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 包相关工具类
 *
 */

public class PackageUtil {

    /**
     * 获取当前应用的版本号
     */
    public static String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = RunTimeData.getInstance().getmContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(RunTimeData.getInstance().getmContext().getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    //
    public static String getAppPackageName(Context context) {
        String packageName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            packageName = pi.packageName;
        } catch (Exception e) {
        }
        return packageName;
    }

    /**
     * 是否是卡乐猫APP
     * 
     * @param context
     * @return
     */
    public static boolean doesKLMApp(Context context) {
        if (getAppPackageName(context).equals("com.kalemao.thalassa")) {
            return true;
        }
        return false;
    }
}
