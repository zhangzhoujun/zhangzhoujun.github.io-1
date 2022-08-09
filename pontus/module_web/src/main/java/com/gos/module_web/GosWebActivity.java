package com.gos.module_web;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dim.library.utils.DLog;
import com.gos.module_web.databinding.WebMainLayoutBinding;
import com.qm.lib.base.BaseAppViewModel;
import com.qm.lib.base.JYActivity;
import com.qm.lib.router.RouterActivityPath;
import com.qm.lib.utils.JYComConst;
import com.qm.lib.utils.SLSLogUtils;
import com.qm.lib.utils.StringUtils;

import kotlin.jvm.JvmField;

/**
 * @author dim
 * @create at 2019/3/18 14:38
 * @description:
 */
@Route(path = RouterActivityPath.Webview.WEBVIEW_MAIN)
public class GosWebActivity extends JYActivity<WebMainLayoutBinding, BaseAppViewModel> {
    @JvmField
    @Autowired(name = JYComConst.WEBVIEW_URL)
    String url = "";

    @JvmField
    @Autowired(name = JYComConst.WEBVIEW_TITLE)
    String title = "";

    @JvmField
    @Autowired(name = "hideClose")
    public String hideClose = "false";

    @JvmField
    @Autowired(name = "hideBack")
    public String hideBack = "false";

    // 是否需要默认不显示toolbar
    @JvmField
    @Autowired(name = "noToolbar")
    public String noToolbar = "false";

    private GosWebFragment mWebFragment;

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.web_main_layout;
    }

    @Override
    public int initVariableId() {
        return com.gos.module_web.BR.viewModel;
    }

    @Override
    public void initData() {
        super.initData();
        ARouter.getInstance().inject(this);
        DLog.d("url = " + url);
        DLog.d("title = " + title);

        if (hideBack == null) {
            hideBack = "false";
        }
        if (hideClose == null) {
            hideClose = "false";
        }
        if (noToolbar == null) {
            noToolbar = "false";
        }

        if (StringUtils.Companion.getInstance().isEmpty(url)) {
            finish();
            return;
        }
        initView();
    }

    @Override
    public void initParam() {
        super.initParam();
    }

    private void initView() {

        mWebFragment = new GosWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(JYComConst.WEBVIEW_URL, url);
        bundle.putString(JYComConst.WEBVIEW_TITLE, title);
        bundle.putString("noToolbar", noToolbar);
        bundle.putString("hideBack", hideBack);
        bundle.putString("hideClose", hideClose);
        bundle.putString("secondPage", "false");
        mWebFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, mWebFragment)
                .commit();
    }


    private BaseAppViewModel getViewModel() {
        return (BaseAppViewModel) viewModel;
    }

    private WebMainLayoutBinding getBinding() {
        return ((WebMainLayoutBinding) binding);
    }

    @Override
    public void onBackPressed() {
        if (!mWebFragment.doesWebCanBack()) {
            super.onBackPressed();
        }
    }

    public void showFull(boolean full) {
        DLog.d("显示WEB 全屏");
        showFullScreen(full);
    }

    protected void onPauseLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(),
                url,
                "ONPAUSE", -1, "");
    }

    protected void onCreateLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(),
                url,
                "ONCREATE", -1, "");
    }

    protected void onResumeLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(),
                url,
                "ONRESUME", -1, "");
    }

    protected void onDestoryLog() {
        SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(),
                url,
                "ONDEXTORY", -1, "");
    }
}
