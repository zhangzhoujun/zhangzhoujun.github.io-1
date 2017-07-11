package com.kalemao.library.utils;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

/**
 * @author robson.zhang
 * @version 创建时间：2016-1-13 上午10:14:23 类说明:
 */
public class ActivityManager {
    protected static ActivityManager mInstance;
    // private List<Activity> mActivities = new ArrayList<Activity>();

    protected Stack<Activity> mActivities = new Stack<Activity>();

    protected ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (mInstance == null) {
            mInstance = new ActivityManager();
        }

        return mInstance;
    }

    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void exitApp(final Context context) {
        for (Activity activity : mActivities) {
            if (activity != null) {
                activity.finish();
            }
        }
        mActivities.clear();
        // 使用Toast来显示异常信息

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context, "很抱歉,程序出现异常,即将退出。", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void exitAppByNormal(final Context context) {
        for (Activity activity : mActivities) {
            if (activity != null) {
                activity.finish();
            }
        }
        mActivities.clear();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    // 退出所有activity
    public void finishAllActivity() {
        if (mActivities != null) {
            while (mActivities.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null)
                    break;
                popOneActivity(activity);
            }
        }
    }

    // 移除一个activity
    public void popOneActivity(Activity activity) {
        if (mActivities != null && mActivities.size() > 0) {
            if (activity != null) {
                activity.finish();
                mActivities.remove(activity);
                activity = null;
            }
        }
    }

    // 获取栈顶的activity，先进后出原则
    public Activity getLastActivity() {
        if (mActivities == null || mActivities.size() == 0) {
            return null;
        }
        return mActivities.lastElement();
    }
}
