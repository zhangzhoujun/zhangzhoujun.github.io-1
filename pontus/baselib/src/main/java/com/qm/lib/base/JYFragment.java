package com.qm.lib.base;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import com.dim.library.base.BaseFragment;
import com.qm.lib.utils.SLSLogUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * @author dim
 * @create at 2019/3/12 14:39
 * @description:
 */
public abstract class JYFragment<V extends ViewDataBinding, VM extends BaseAppViewModel> extends BaseFragment {

    @Override
    public void showDialog(String title) {
        super.showDialog(title);
    }

    @Override
    public void dismissDialog() {
        super.dismissDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getSimpleName());
        onResumeLog();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getSimpleName());
        onPauseLog();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onViewCreateEnd() {
        super.onViewCreateEnd();
        onCreateLog();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
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
                getClass().getSimpleName(),
                "",
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
