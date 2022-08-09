package com.qm.lib.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Environment
import com.dim.library.utils.DLog
import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Constructor
import java.lang.reflect.Method


/**
 * @ClassName HookManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/15 5:25 PM
 * @Version 1.0
 */
class HookManager {

    companion object {
        val instance: HookManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HookManager()
        }
    }

        var dexPath: String =
        Environment.getExternalStorageDirectory().toString().toString() + File.separator + "jyzj"+ File.separator + "利安健康管家.apk"
//    var dexPath: String =
//        Environment.getExternalStorageDirectory().toString()
//            .toString() + File.separator + "jyzj" + File.separator + "全民健康链_V1.0.0-1.apk"

    @SuppressLint("NewApi", "WrongConstant")
    fun launchTargetActivity(activity: Activity) {
        //根据sd卡的路径获取未安装的apk的信息
        val packageInfo: PackageInfo =
            activity.packageManager.getPackageArchiveInfo(dexPath, 1)
        if (packageInfo.activities != null
            && packageInfo.activities.isNotEmpty()
        ) {
            val activityName = packageInfo.activities[0].name //获取插件中第一个activity
            var mClass = activityName
            launchTargetActivity(activity, mClass)
        }
    }

    @SuppressLint("NewApi")
    fun launchTargetActivity(activity: Activity, className: String) {
        DLog.e("MainActivity", "start launchTargetActivity, className=$className")
        val dexOutputDir: File = activity.getDir("dex", 0)
        val dexOutputPath: String = dexOutputDir.absolutePath
        val localClassLoader = ClassLoader.getSystemClassLoader()
        val dexClassLoader = DexClassLoader(
            dexPath,
            dexOutputPath, null, localClassLoader
        )
        try {
            val localClass =
                dexClassLoader.loadClass(className) //创建插件Activity的实例
            //调用插件Activity的构造函数，将当前activity的上下文传递到插件activity中去
            val localConstructor: Constructor<*> = localClass.getConstructor(
                *arrayOf<Class<*>>(
                    Activity::class.java
                )
            )
            val instance: Any = localConstructor.newInstance(arrayOf<Any>(this))
            DLog.e("MainActivity", "instance = $instance")
            val onCreate: Method = localClass.getDeclaredMethod(
                "onCreate", *arrayOf<Class<*>>(
                    Bundle::class.java
                )
            )
            onCreate.isAccessible = true
            val bundle = Bundle()
            onCreate.invoke(instance, bundle) //调用插件activity的onCreate方法
        } catch (e: Exception) {
            DLog.e("MainActivity", e.message)
            e.printStackTrace()
        }
    }

    ///////////
}