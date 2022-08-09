package com.qm.lib.base;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.databinding.ViewDataBinding;

import com.dim.library.base.BaseActivity;
import com.dim.library.utils.DLog;
import com.qm.lib.R;
import com.qm.lib.utils.SLSLogUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * @author dim
 * @create at 2019/3/12 14:54
 * @description:
 */
public abstract class JYActivity<V extends ViewDataBinding, VM extends BaseAppViewModel> extends BaseActivity {

    @Override
    public void showDialog(String title) {
        super.showDialog(title);
    }

    @Override
    public void dismissDialog() {
        super.dismissDialog();
    }

    @Override
    public void initData() {
        super.initData();
        performTranslucent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showSoftInputKetboard(final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);
    }

    protected void hideSoftInputKetboard(final EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
    }

    protected void performTranslucent() {
        QMUIStatusBarHelper.translucent(this);
    }

    /**
     * 全屏显示
     */
    protected void showFullScreen(boolean full) {
        if (full) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                    Window window = getWindow();
                    View decorView = window.getDecorView();
                    //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                    int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    decorView.setSystemUiVisibility(option);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                } else {
                    Window window = getWindow();
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                    attributes.flags |= flagTranslucentStatus;
                    window.setAttributes(attributes);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    View decorView = window.getDecorView();
                    int option = View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    decorView.setSystemUiVisibility(option);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    Window window = getWindow();
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                    attributes.flags &= ~flagTranslucentStatus;
                    window.setAttributes(attributes);
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (!isAppOnForeground()) {
            //app 进入后台,停止IMService,采用push机制接收离线消息
            DLog.i("im", "app enter background");
//            MessageEnterBack enterBack = new MessageEnterBack(true);
//            RxBus.getDefault().post(enterBack);
        }
    }

    /**
     * 程序是否在前台运行
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager =
                (ActivityManager) getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance
                    == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

        onPauseLog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

        onResumeLog();
    }

    @Override
    protected void onCreateEnd() {
        super.onCreateEnd();
        onCreateLog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestoryLog();
    }

    protected void onPauseLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(), "",
                "ONPAUSE",
                -1,
                ""
        );
    }

    protected void onCreateLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(), "",
                "ONCREATE",
                -1,
                ""
        );
    }

    protected void onResumeLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(), "",
                "ONRESUME",
                -1,
                ""
        );
    }

    protected void onDestoryLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(),
                "",
                "ONDESTORY",
                -1,
                ""
        );
    }
}
