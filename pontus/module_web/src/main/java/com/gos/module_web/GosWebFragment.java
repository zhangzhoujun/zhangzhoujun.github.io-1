package com.gos.module_web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.databinding.Observable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dim.library.bus.RxBus;
import com.dim.library.bus.RxSubscriptions;
import com.dim.library.utils.CheckApkExist;
import com.dim.library.utils.DLog;
import com.dim.library.utils.GsonUtils;
import com.dim.library.utils.ToastUtils;
import com.dim.library.utils.Utils;
import com.dim.library.utils.VersionUtils;
import com.gos.module_web.databinding.WebLayoutBinding;
import com.gos.module_web.entity.CAppSuccess;
import com.gos.module_web.entity.CJSCheckPackage;
import com.gos.module_web.entity.CJsAliPayInfo;
import com.gos.module_web.entity.CJsCallBack;
import com.gos.module_web.entity.CJsCallBackError;
import com.gos.module_web.entity.CJsCheckoutPackageResult;
import com.gos.module_web.entity.CJsDownLoad;
import com.gos.module_web.entity.CJsFullScreenInfo;
import com.gos.module_web.entity.CJsLogin;
import com.gos.module_web.entity.CJsOpenAdvertisement;
import com.gos.module_web.entity.CJsOpenBrowser;
import com.gos.module_web.entity.CJsOpenNative;
import com.gos.module_web.entity.CJsOpenWXMiniProgram;
import com.gos.module_web.entity.CJsOpenWeb;
import com.gos.module_web.entity.CJsPagerInfo;
import com.gos.module_web.entity.CRightClick;
import com.gos.module_web.entity.CWebDeviceInfo;
import com.gos.module_web.entity.CWebUsers;
import com.gos.module_web.pay.AliPayResultEvent;
import com.gos.module_web.x5.X5WebView;
import com.qm.lib.base.JYFragment;
import com.qm.lib.base.LocalUserManager;
import com.qm.lib.entity.CVideoLookResoultBean;
import com.qm.lib.entity.IBXOpenMessage;
import com.qm.lib.entity.MRefreshWeb;
import com.qm.lib.message.RefreshUserByServer;
import com.qm.lib.message.RefreshUserInfo;
import com.qm.lib.router.RouterFragmentPath;
import com.qm.lib.router.RouterManager;
import com.qm.lib.utils.JYComConst;
import com.qm.lib.utils.JYMMKVManager;
import com.qm.lib.utils.JYUtils;
import com.qm.lib.utils.RuntimeData;
import com.qm.lib.utils.SLSLogUtils;
import com.qm.lib.utils.SystemUtil;
import com.qm.lib.widget.popupWindow.CommonPopupWindow;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import kotlin.jvm.JvmField;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-18
 * 更改时间：2019-07-18
 * 版本号：1
 */
@Route(path = RouterFragmentPath.Webview.WEB_INDEX)
public class GosWebFragment extends JYFragment<WebLayoutBinding, GosWebViewModel> {

    @JvmField
    @Autowired(name = JYComConst.WEBVIEW_URL)
    public String url = "";

    @JvmField
    @Autowired(name = JYComConst.WEBVIEW_TITLE)
    public String title;

    //  是否需要隐藏左边的返回和关闭按钮
    @JvmField
    @Autowired(name = "hideClose")
    public String hideClose = "false";

    @JvmField
    @Autowired(name = "hideBack")
    public String hideBack = "false";

    // 是否需要默认不显示toolbar
    @JvmField
    @Autowired(name = "noToolbar")
    public String noToolbar = "true";

    @JvmField
    @Autowired(name = "secondPage")
    public String secondPage = "true";

    //        private GosWebview mWebView;
    private X5WebView mWebView;

    // 是否是全屏，默认全屏
    private boolean isShowFull = true;

    private SwipeRefreshLayout swipeLayout;

    private String pageFinishUrl;

    public String getPageFinishUrl() {
        return pageFinishUrl;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        return R.layout.web_layout;
    }

    @Override
    public int initVariableId() {
        return com.gos.module_web.BR.viewModel;
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        swipeLayout = getBind().swipeRefresh;
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getVm().refreshLoadUrl();

                SLSLogUtils.Companion.getInstance().sendLogLoad(
                    getClass().getSimpleName(),
                    url,
                    "REFRESH", -1, "");
            }
        });
        swipeLayout.setColorScheme(R.color.holo_blue_bright,
            R.color.holo_green_light, R.color.holo_orange_light,
            R.color.holo_red_light);

        swipeLayout.setEnabled(false);

        swipeLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(@NonNull SwipeRefreshLayout parent, @Nullable View child) {
                return getWebView().getScrollY() > 0;
            }
        });

        getVm().rightClick.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (getVm().getPagerInfo() != null && getVm().getPagerInfo().getMenus() != null) {
                    sendJsFunctionSuccess(getVm().getPagerInfo().getCallbackKey(),
                        new CRightClick(getVm().getPagerInfo().getMenus().get(0).getKey()));
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setStateBar() {
        int result = 60;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        ViewGroup.LayoutParams lp = getBind().statusBarTop.getLayoutParams();
        lp.height = result;
        getBind().statusBarTop.setLayoutParams(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initData() {
        super.initData();
        ARouter.getInstance().inject(this);

        DLog.d("url: "
            + url
            + " title : "
            + title
            + " hideClose : "
            + hideClose
            + " hideBack : "
            + hideBack
            + " noToolbar : "
            + noToolbar);

        if (hideBack == null) {
            hideBack = "false";
        }
        if (hideClose == null) {
            hideClose = "false";
        }
        if (noToolbar == null) {
            noToolbar = "true";
        }
        if (secondPage == null) {
            secondPage = "true";
        }

        getVm().initParam(getActivity());

        initWebview();
        getVm().setData(url, title, hideBack.equals("true"), hideClose.equals("true"), getBind().signWebview);
        getVm().initLoadingStatusView(getBind().signWebview);

        initEvent();
        if (noToolbar.equals("true")) {
            getBind().toolbar.setVisibility(View.GONE);
            getBind().statusBarTop.setVisibility(View.GONE);
        } else {
            setStateBar();
        }

        getWebView().setOnScrollListener(new X5WebView.IScrollListener() {
            @Override
            public void onScrollChanged(int scrollY) {
                if (isCanRefresh) {
                    if (scrollY == 0) {
                        //开启下拉刷新
                        swipeLayout.setEnabled(true);
                    } else {
                        //关闭下拉刷新
                        swipeLayout.setEnabled(false);
                    }
                }
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        if (getBind() != null
            && getBind().signWebview != null
            && getBind().signWebview.getUrl() != null
            && (getBind().signWebview.getUrl()
            .startsWith("https://qr.alipay.com/") || getBind().signWebview.getUrl()
            .startsWith("https://render.alipay.com"))) {
            getBind().signWebview.goBack();
            //showPaySure();
        }
        if (getWebView() != null) {
            getWebView().onResume();
            getWebView().getSettings().setJavaScriptEnabled(true);
        }
        if (isNeedRefresh) {
            getVm().refreshLoadUrl();
            isNeedRefresh = false;
            getVm().dismissDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getWebView() != null) {
            getWebView().onPause();
            getWebView().getSettings().setJavaScriptEnabled(false);
        }
    }

    private void gobackOrClose() {
        if (getBind().signWebview.canGoBack()) {
            getBind().signWebview.goBack();
        } else {
            getVm().onBackPressed();
        }
    }

    private WebLayoutBinding getBind() {
        return (WebLayoutBinding) binding;
    }

    public GosWebViewModel getVm() {
        return (GosWebViewModel) viewModel;
    }

    private X5WebView getWebView() {
        if (getBind() == null) {
            return null;
        }
        if (mWebView == null) {
            mWebView = getBind().signWebview;
        }
        return mWebView;
    }

    private void initWebview() {
        setCookie();
        if (getWebView() != null) {
            WebSettings webSettings = getWebView().getSettings();
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setTextZoom(100);
            //webSettings.setUseWideViewPort(true);
            //webSettings.setLoadWithOverviewMode(true);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

            webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);

            // 触摸焦点起作用
            getWebView().requestFocus();
            getWebView().setWebChromeClient(webChromeClient);

            getWebView().addJavascriptInterface(this, "lokiBridge");
            // 部分手机不能加载图片问题
            //            try {
            //                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //                    getWebView().getSettings()
            //                            .setMixedContentMode(getWebView().getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
            //                }
            //            } catch (Exception e) {
            //                e.printStackTrace();
            //            }
            getWebView().setWebViewClient(webViewClient);
        }

        getWebView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (null == result) {
                    return false;
                }
                int type = result.getType();
                if (type == WebView.HitTestResult.IMAGE_TYPE) {
                    String imgurl = result.getExtra();
                    DLog.d("AAAA", "imgurl = " + imgurl);
                    decoderBase64File(imgurl);
                    return true;
                }

                return false;
            }
        });
    }

    private void decoderBase64File(String base64Code) {
        try {
            Bitmap bitmap = base64ToBitmap(base64Code);
            saveImageToGallery(getContext(), bitmap);
            ToastUtils.showShort("图片保存成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "transfer");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "node_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        context.sendBroadcast(
            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }

    private Bitmap base64ToBitmap(String base64Data) {
        if (!TextUtils.isEmpty(base64Data)) {
            byte[] decode = null;
            if (isBase64Img(base64Data)) {
                base64Data = base64Data.split(",")[1];
            }
            decode = Base64.decode(base64Data, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            return decodedByte;
        }
        return null;
    }

    private boolean isBase64Img(String imgurl) {
        if (!TextUtils.isEmpty(imgurl) && (imgurl.startsWith("data:image/png;base64,") || imgurl.startsWith(
            "data:image/jpeg;base64,")
            || imgurl.startsWith("data:image/*;base64,") || imgurl.startsWith("data:image/jpg;base64,")
        )) {
            return true;
        }
        return false;
    }

    private void setCookie() {
        CWebDeviceInfo info = getDeviceInfo();
        String brandCookie = "brand=" + info.getBrand();
        String deviceIdCookie = "deviceId=" + info.getDeviceId();
        String modelCookie = "model=" + info.getModel();
        String sysVersionCookie = "sysVersion=" + info.getSysVersion();
        String appVersionCookie = "appVersion=" + info.getAppVersion();
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, brandCookie);
        cookieManager.setCookie(url, deviceIdCookie);
        cookieManager.setCookie(url, modelCookie);
        cookieManager.setCookie(url, sysVersionCookie);
        cookieManager.setCookie(url, appVersionCookie);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        String referer = "";

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            DLog.d("GosWebFragment", "onPageStarted()11111 :  " + url);
            DLog.d("GOS_WEBVIEW", "onPageStarted  = " + url);
            getVm().showLoadSuccess();
            if (url != null && url.startsWith("http")) {
                showDialog("");
            }

            setUrlRefrer(url);
            if (secondPage.equals("true") && getWebView() != null && getWebView().canGoBack()) {
                RouterManager.Companion.getInstance().gotoWebviewActivity(url, "");
                getWebView().goBack();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            DLog.d("GosWebFragment", "onReceivedError()11111 :  " + error.toString());

            if (url != null && url.startsWith("http")) {
                dismissDialog();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            pageFinishUrl = url;
            getVm().setFinishUrl(url);

            DLog.d("GosWebFragment", "onPageFinished() :  " + url);
            if (url != null && url.startsWith("http")) {
                dismissDialog();
            }
        }

        // 因为兼容H5的支付宝支付改为这个方法（此方法为过时方法）
        // 替代方法为下面注释掉的方法
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            DLog.d("GOS_WEBVIEW", "shouldOverrideUrlLoading 222  = " + url);

            SLSLogUtils.Companion.getInstance().sendLogLoad(
                getClass().getSimpleName(),
                url,
                "WEB_CHANGE",
                -1, ""
            );

            try {
                if (url.startsWith("tbopen")
                    || url.startsWith("tmall")
                    || url.startsWith("weixin://")
                    || url.startsWith("alipays://")
                    || url.startsWith("alipay")
                    || url.startsWith("pinduoduo:")
                    || url.startsWith("mailto://")
                    || url.startsWith("tel://")
                    || url.startsWith("openapp.jdmobile://")
                    || url.startsWith("openapp.")
                    || url.contains("alipays://platformapi")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort("未安装当前APP，请先下载");
                return true;
            }

            if (url.contains("https://wx.tenpay.com")) {
                //微信支付要用，不然说"商家参数格式有误"
                DLog.d("GOS_WEBVIEW", "跳转微信支付要用 referer = " + referer);
                Map<String, String> extraHeaders = new HashMap<>();
                extraHeaders.put("Referer", referer);
                view.loadUrl(url, extraHeaders);
                referer = url;
                return true;
            }
            setUrlRefrer(url);
            return false;
        }

        private void setUrlRefrer(String cUrl) {
            String[] urlData = cUrl.split("\\?");
            if (urlData.length > 0) {
                referer = urlData[0];
            } else {
                referer = cUrl;
            }
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                //隐藏进度条
                swipeLayout.setRefreshing(false);
            } else {
                if (!swipeLayout.isRefreshing()) {
                    swipeLayout.setRefreshing(true);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
            DLog.d("AAAAAA", "onReceivedTouchIconUrl url = " + url);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
            DLog.d("AAAAAA", "onReceivedIcon ");
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            DLog.d("AAAAAA", "onReceivedTitle = " + title);
            if (title.equals("喜马拉雅")) {
                return;
            }
            if (title.equals("全民价值链")) {
                title = VersionUtils.getAppName(view.getContext());
            }
            if (title.length() > 15 || title.startsWith("open") || title.length() > 10 && title.startsWith("1")) {
                return;
            }
            Pattern httpPattern;
            //初始化正则
            httpPattern =
                Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
            //开始判断了
            if (httpPattern.matcher(title).matches()) {
                //这是一个网址链接
            } else {
                //这不是一个网址链接
                getVm().setTitleInfo(title);
            }
        }

        private Intent createChooserIntent(Intent... intents) {
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
            chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
            return chooser;
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            DLog.d("AAAA", "openFileChoose( ValueCallback uploadMsg, String acceptType )");
            uploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            DLog.d("AAAA", "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            uploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
            FileChooserParams fileChooserParams) {
            DLog.d("AAAA", "onShowFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
            uploadMessageAboveL = filePathCallback;
            openImageChooserActivity();
            return true;
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            DLog.d("AAAA", "onGeolocationPermissionsHidePrompt");
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String s,
            GeolocationPermissionsCallback geolocationPermissionsCallback) {
            DLog.d("AAAA", "onGeolocationPermissionsShowPrompt s = " + s);
            DLog.d("AAAA", "onGeolocationPermissionsShowPrompt s2 = " + geolocationPermissionsCallback.toString());
            getVm().hasLocationPermission(getActivity());
            super.onGeolocationPermissionsShowPrompt(s, geolocationPermissionsCallback);
        }
    };

    /**
     * 设置页面标题等
     */
    private void setTitleInfo(CJsPagerInfo pagerInfo) {
        getActivity().runOnUiThread(() -> {
            isCanRefresh = pagerInfo.isPullRefresh();
            getBind().swipeRefresh.setEnabled(isCanRefresh);    // 不用
            if (pagerInfo.isHasHead()) {
                getVm().setTitleInfo(pagerInfo);
                getBind().toolbar.setVisibility(View.VISIBLE);
                getBind().statusBarTop.setVisibility(View.GONE);
            } else {
                getVm().setTitleInfo(pagerInfo);
                getBind().toolbar.setVisibility(View.GONE);
                getBind().statusBarTop.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean isCanRefresh = false;

    /**
     * 刷新用户
     */
    @JavascriptInterface
    public void refresh_user(String params) {
        DLog.d("refresh_user() refresh_user params = " + params);
        CJsCallBack callBack = GsonUtils.fromJson(params, CJsFullScreenInfo.class);
        getActivity().runOnUiThread(() -> {
            try {
                RxBus.getDefault().post(new RefreshUserByServer());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sendJsFunctionSuccess(callBack.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 刷新用户
     */
    @JavascriptInterface
    public void refreshUser(String params) {
        DLog.d("refreshUser() refreshUser params = " + params);
        CJsCallBack callBack = GsonUtils.fromJson(params, CJsFullScreenInfo.class);
        getActivity().runOnUiThread(() -> {
            try {
                RxBus.getDefault().post(new RefreshUserInfo());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sendJsFunctionSuccess(callBack.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 支付宝支付
     */
    @JavascriptInterface
    public void goPay(String params) {
        DLog.d("startAlipay() startAlipay params = " + params);
        CJsAliPayInfo callBack = GsonUtils.fromJson(params, CJsAliPayInfo.class);

        getVm().getAliPayData(callBack.getOrder_sn());

        sendJsFunctionSuccess(callBack.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 设置全屏
     */
    @JavascriptInterface
    public void fullScreen(String params) {
        DLog.d("fullScreen() fullScreen params = " + params);
        CJsFullScreenInfo callBack = GsonUtils.fromJson(params, CJsFullScreenInfo.class);
        getActivity().runOnUiThread(() -> {
            try {
                showFullWeb(callBack.isFullScreen());
                //RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getBind().refreshLayout.getLayoutParams();
                //lp.removeRule(RelativeLayout.BELOW);
                //// lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
                //getBind().refreshLayout.setLayoutParams(lp);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        sendJsFunctionSuccess(callBack.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 全屏显示
     */
    protected void showFullWeb(boolean full) {
        if (full) {
            if (getActivity().getClass().equals(GosWebActivity.class)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    View decorView = getActivity().getWindow().getDecorView();
                    int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    decorView.setSystemUiVisibility(option);
                    getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
            }
            getBind().statusBarTop.setVisibility(View.GONE);
        }
    }

    /**
     * 当前设备的信息
     */
    private CWebDeviceInfo getDeviceInfo() {
        String brand = Build.BRAND; // 设备品牌
        String model = Build.MODEL; // 设备名称
        String vrelease = Build.VERSION.RELEASE;    // 系统版本
        String deviceid = JYMMKVManager.Companion.getInstance().getDeviceId();
        String appVersion = SystemUtil.getVersionName(Utils.getContext());

        CWebDeviceInfo deviceInfo = new CWebDeviceInfo(brand, model, vrelease, deviceid, appVersion);
        DLog.d("设备信息：" + deviceInfo.toString());
        return deviceInfo;
    }

    /**
     * 获取用户信息
     */
    @JavascriptInterface
    public void getUserData(String params) {
        DLog.d("getUserData() getUserData params = " + params);

        CJsCallBack callBack = GsonUtils.fromJson(params, CJsCallBack.class);

        if (TextUtils.isEmpty(JYMMKVManager.Companion.getInstance().getToken())) {
            DLog.d("错误：10003 ... ");
            CJsCallBackError error = new CJsCallBackError("10003", "当前用户未登录");
            sendJsFunctionFailer(callBack.getCallbackKey(), error);
        } else {
            CWebUsers users = new CWebUsers(JYMMKVManager.Companion.getInstance().getToken(),
                LocalUserManager.Companion.getInstance().getUserId(),
                LocalUserManager.Companion.getInstance().getUserMobile(),
                JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "APP_KEY"));
            DLog.d("用户信息：" + users.toString());
            sendJsFunctionSuccess(callBack.getCallbackKey(), users);
        }
    }

    /**
     * 打开浏览器页面
     */
    @JavascriptInterface
    public void openBrowser(String params) {
        DLog.d("getUserData() getUserData params = " + params);
        CJsOpenBrowser callBack = GsonUtils.fromJson(params, CJsOpenBrowser.class);
        Uri uri = Uri.parse(callBack.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        sendJsFunctionSuccess(callBack.getCallbackKey(), null);
    }

    /**
     * 下载
     */
    @JavascriptInterface
    public void doDownload(String params) {
        DLog.d("dowDownload() params = " + params);

        CJsDownLoad callBack = GsonUtils.fromJson(params, CJsDownLoad.class);
        downLoadFile(callBack);

        sendJsFunctionSuccess(callBack.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 获取设备信息
     */
    @JavascriptInterface
    public void getAppData(String params) {
        DLog.d("getAppData() getAppData params = " + params);
        CJsCallBack callBack = GsonUtils.fromJson(params, CJsCallBack.class);

        sendJsFunctionSuccess(callBack.getCallbackKey(), getDeviceInfo());
    }

    /**
     * 设置页面基础信息
     *
     * @param {String} title ——页面标题
     * @param {Boolean} is_pull_refresh ——是否下拉刷新
     * @param {String} bg_color ——header背景颜色
     * @param {String} color ——header文字颜色
     * @param {Boolean} has_head ——是否显示header
     **/
    @JavascriptInterface
    public void setPage(String params) {
        DLog.d("setPage() setPage param  = " + params);
        CJsPagerInfo pageInfo = GsonUtils.fromJson(params, CJsPagerInfo.class);
        setTitleInfo(pageInfo);
    }

    /**
     * 新开一张H5页
     */
    @JavascriptInterface
    public void openWebview(String params) {
        DLog.d("openWebview() openWebview params =  " + params);
        CJsOpenWeb webInfo = GsonUtils.fromJson(params, CJsOpenWeb.class);
        isNeedRefresh = webInfo.isNeedReFresh();
        RouterManager.Companion.getInstance().gotoWebviewActivity(webInfo.getUrl(), "");

        sendJsFunctionSuccess(webInfo.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 跳转到原生
     */
    @JavascriptInterface
    public void gotoNative(String params) {
        DLog.d("gotoNative() gotoNative params =  " + params);
        CJsOpenNative webInfo = GsonUtils.fromJson(params, CJsOpenNative.class);

        try {
            if (webInfo.getPath().startsWith("arouter")) {
                RouterManager.Companion.getInstance().gotoNativeApp(webInfo.getPath(), "");
                return;
            }

            Postcard postcard = ARouter.getInstance()
                .build(webInfo.getPath());
            postcard.navigation(null, new NavCallback() {
                @Override
                public void onFound(Postcard postcard) {
                    DLog.d("GOS_WEBVIEW" + " gotoNative  --> onFound");
                    sendJsFunctionSuccess(webInfo.getCallbackKey(), new CAppSuccess());
                }

                @Override
                public void onLost(Postcard postcard) {
                    DLog.d("GOS_WEBVIEW" + "gotoNative  --> onLost");
                    sendJsFunctionFailer(webInfo.getCallbackKey(), new CJsCallBackError("10003", "页面未找到"));
                }

                @Override
                public void onArrival(Postcard postcard) {
                    DLog.d("GOS_WEBVIEW" + "gotoNative  --> onArrival");
                }

                @Override
                public void onInterrupt(Postcard postcard) {
                    DLog.d("GOS_WEBVIEW" + "gotoNative  --> onInterrupt");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开爱变现 游戏
     */
    @JavascriptInterface
    public void openIbx(String params) {
        DLog.d("openIbx params = " + params);
        CJsCallBack callBack = GsonUtils.fromJson(params, CJsCallBack.class);

        RxBus.getDefault().post(new IBXOpenMessage());

        sendJsFunctionSuccess(callBack.getCallbackKey(), null);
    }

    /**
     * 打开微信小程序
     */
    @JavascriptInterface
    public void openWXMiniProgram(String params) {
        DLog.d(" openWXMiniProgram params = " + params);
        CJsOpenWXMiniProgram callBack = GsonUtils.fromJson(params, CJsOpenWXMiniProgram.class);

        try {
            String appId = RuntimeData.getInstance().getWxAppId(); // 填应用AppId
            IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);

            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = callBack.getUserName(); // 填小程序原始id
            req.path =
                callBack.getPath();                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
            req.miniprogramType = callBack.getMiniprogramType();// 可选打开 开发版，体验版和正式版
            api.sendReq(req);

            sendJsFunctionSuccess(callBack.getCallbackKey(), null);
        } catch (Exception e) {
            e.printStackTrace();
            CJsCallBackError error = new CJsCallBackError("10007", "应用未安装或解析错误");
            sendJsFunctionFailer(callBack.getCallbackKey(), error);
        }
    }

    /**
     * 打开广告
     */
    @JavascriptInterface
    public void openAdvertisement(String params) {
        DLog.d("openAdvertisement params = " + params);
        CJsOpenAdvertisement callBack = GsonUtils.fromJson(params, CJsOpenAdvertisement.class);

        // 打开激励视频
        if (callBack.getType().equals("RewardVideoFN")) {
            RouterManager.Companion.getInstance().gotoOtherPage("/main/third?third_type=videoFN&third_dialog=false");
        } else {
            RouterManager.Companion.getInstance()
                .gotoOtherPage("/main/third?third_type=video&third_dialog=false&third_key=" + callBack.getId());
        }
        getVm().showDialog();
        sendJsFunctionSuccess(callBack.getCallbackKey(), null);
    }

    @JavascriptInterface
    public void openAdvertisement2(String params) {
        DLog.d("openAdvertisement2 params = " + params);
        CJsOpenAdvertisement callBack = GsonUtils.fromJson(params, CJsOpenAdvertisement.class);

        DLog.d("openAdvertisement2 data = " + callBack.getData());

        videoCallback = callBack.getCallbackKey();

        // 打开激励视频
        RouterManager.Companion.getInstance()
            .gotoOtherPage("/main/third?third_type=videoAll&third_dialog=false&data=" + GsonUtils.objectToJsonStr(
                callBack.getData()));
    }

    /**
     * 登录
     */
    @JavascriptInterface
    public void login(String params) {
        DLog.d("login() 参数： " + params);
        DLog.d("GOS_WEBVIEW", "login params = " + params);
        CJsLogin login = GsonUtils.fromJson(params, CJsLogin.class);

        sendJsFunctionSuccess(login.getCallbackKey(), new CAppSuccess());
        JYMMKVManager.Companion.getInstance().doLoginOut();
    }

    /**
     * 返回上一个页面
     */
    @JavascriptInterface
    public void appBack(String params) {
        DLog.d("appBack() appBack = " + params);
        CJsLogin login = GsonUtils.fromJson(params, CJsLogin.class);
        getActivity().runOnUiThread(() -> getVm().onBackPressed());
        sendJsFunctionSuccess(login.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 关闭当前的网页
     */
    @JavascriptInterface
    public void closeWebview(String params) {
        DLog.d("closeWebview(params) closeWebview = " + params);
        CJsLogin login = GsonUtils.fromJson(params, CJsLogin.class);
        getActivity().runOnUiThread(() -> getVm().onViewCloseClick());
        sendJsFunctionSuccess(login.getCallbackKey(), new CAppSuccess());
    }

    /**
     * 关闭当前的网页
     */
    @JavascriptInterface
    public void closeWebview() {
        DLog.d("closeWebview() ... ");
        getActivity().runOnUiThread(() -> getVm().onViewCloseClick());
    }

    /**
     * 判断是否安装了某个APK
     */
    @JavascriptInterface
    public void checkPackageExist(String params) {
        DLog.d("checkPackageExist() checkPackageExist params = " + params);
        CJSCheckPackage callBack = GsonUtils.fromJson(params, CJSCheckPackage.class);
        sendJsFunctionSuccess(callBack.getCallbackKey(), new CJsCheckoutPackageResult(
            CheckApkExist.checkApkExist(getContext(), callBack.getPackageName())
        ));
    }

    /**
     * 打开某个APK
     */
    @JavascriptInterface
    public void openPackage(String params) {
        DLog.d("openPackage() openPackage params = " + params);
        CJSCheckPackage callBack = GsonUtils.fromJson(params, CJSCheckPackage.class);
        sendJsFunctionSuccess(callBack.getCallbackKey(), new CJsCheckoutPackageResult(
            CheckApkExist.openApkWithPackageName(getContext(), callBack.getPackageName())
        ));
    }

    /**
     * 回掉js方法成功
     */
    private void sendJsFunctionSuccess(final String callBackKey, final Object object) {
        DLog.d("sendJsFunctionSuccess", "SUCCESS  =  " + GsonUtils.objectToJsonStr(object));
        getActivity().runOnUiThread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWebView().evaluateJavascript("javascript:"
                    + callBackKey
                    + "("
                    + GsonUtils.objectToJsonStr(object)
                    + ","
                    + GsonUtils.objectToJsonStr("")
                    + ")", null);
            } else {
                getWebView().loadUrl("javascript:"
                    + callBackKey
                    + "("
                    + GsonUtils.objectToJsonStr(object)
                    + ","
                    + GsonUtils.objectToJsonStr("")
                    + ")", null);
            }
        });
    }

    /**
     * 回掉js方法失败
     */
    private void sendJsFunctionFailer(final String callBackKey, final Object object) {
        DLog.d("sendJsFunctionFailer", "ERROR  =  " + GsonUtils.objectToJsonStr(object));
        getActivity().runOnUiThread(() -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWebView().evaluateJavascript(
                    "javascript:"
                        + callBackKey
                        + "("
                        + GsonUtils.objectToJsonStr("")
                        + ","
                        + GsonUtils.objectToJsonStr(
                        object)
                        + ")", null);
            } else {
                getWebView().loadUrl(
                    "javascript:"
                        + callBackKey
                        + "("
                        + GsonUtils.objectToJsonStr("")
                        + ","
                        + GsonUtils.objectToJsonStr(
                        object)
                        + ")");
            }
        });
    }

    public boolean doesWebCanBack() {
        if (downPop != null && downPop.isShowing()) {
            downPop.dismiss();
            return true;
        } else if (getWebView().canGoBack()) {
            getWebView().goBack();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        cleanEvent();
        if (getWebView() != null) {
            getWebView().destroy();
        }
        cleanDialog();

        super.onDestroy();
    }

    //判断文件是否存在
    private boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private String getAlbumDir() {
        return getSDPath() + "/" + "node_transfer" + "/";
    }

    private void showRedownload() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("文件已存在");
        builder.setMessage("文件已存在,保存至 " + filePath + "，是否重新下载？");
        // 为对话框设置取消按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (deleteSingleFile(filePath)) {
                    downLoadFile(mDownload);
                }
            }
        });
        builder.create().show();
    }

    private void downLoadFile(CJsDownLoad download) {
        mDownload = download;
        // 如果文件已存在，不下载
        filePath =
            getAlbumDir() + mDownload.getDownloadName() + mDownload.getFileSuffix();
        DLog.e("downLoadFile", "filePath = " + filePath);
        if (fileIsExists(filePath)) {
            showRedownload();
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDownloadStatus(0, 0, null);
            }
        });

        mHandler.postDelayed(downloadRun, 1000);
    }

    Handler mHandler = new Handler();
    Runnable downloadRun = new Runnable() {
        @Override
        public void run() {
            if (mDownload == null) {
                return;
            }
            DownloadUtil.get()
                .download(mDownload.getDownloadUrl(), getAlbumDir(),
                    mDownload.getDownloadName() + mDownload.getFileSuffix(),
                    new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            DLog.d("downLoadFile", "onDownloadSuccess = " + file.getAbsolutePath());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDownloadStatus(1, 0, file);
                                }
                            });
                        }

                        @Override
                        public void onDownloading(int progress) {
                            DLog.d("downLoadFile", "progress = " + progress);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDownloadStatus(0, progress, null);
                                }
                            });
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                            DLog.d("onDownloadFailed", "Exception = " + e.getMessage());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showDownloadStatus(-1, 0, null);
                                }
                            });
                        }
                    });
        }
    };

    private CommonPopupWindow downPop;
    TextView progressName;
    ProgressBar progressBar;
    CJsDownLoad mDownload;
    String filePath;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (downPop != null && downPop.isShowing() && mDownload != null) {
            DownloadUtil.get().cancelDownload();
            mHandler.removeCallbacks(downloadRun);
            deleteSingleFile(filePath);
            mDownload = null;
            downPop = null;
        }
    }

    private void showDownloadStatus(int downLoadType, int progress, File file) {
        if (mDownload == null) {
            return;
        }
        if (downPop == null) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.web_download, null);
            downPop = new CommonPopupWindow.Builder(getActivity())
                .setView(view)
                .setWidthAndHeight(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimMiddle)
                .setOutsideTouchable(false)
                .setBackGroundLevel(0.4f)
                .create();

            TextView name = (TextView) view.findViewById(R.id.down_title);
            progressName = (TextView) view.findViewById(R.id.down_size);
            TextView cancel = (TextView) view.findViewById(R.id.down_cancel);
            progressBar = (ProgressBar) view.findViewById(R.id.down_progress);

            name.setText(mDownload.getDownloadName());

            cancel.setOnClickListener(e -> {
                DownloadUtil.get().cancelDownload();
                mHandler.removeCallbacks(downloadRun);
                deleteSingleFile(filePath);
                mDownload = null;
                downPop.dismiss();
            });
            downPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (mDownload != null) {
                        DownloadUtil.get().cancelDownload();
                        mHandler.removeCallbacks(downloadRun);
                        deleteSingleFile(filePath);
                        mDownload = null;
                        downPop = null;
                    }
                }
            });

            downPop.showAtLocation(getBind().root, Gravity.CENTER, 0, 0);
        } else {
            if (!downPop.isShowing()) {
                downPop.showAtLocation(getBind().root, Gravity.CENTER, 0, 0);
            }
        }
        if (downLoadType == -1) {
            mDownload = null;
            ToastUtils.showShort("下载失败，请重试");
            deleteSingleFile(filePath);
            downPop.dismiss();
            downPop = null;
        } else if (downLoadType == 1) {
            mDownload = null;
            ToastUtils.showShort("下载至 " + file.getAbsolutePath());
            updateMediaStore(getActivity(), file);
            downPop.dismiss();
            downPop = null;
        } else {
            double total = mDownload.getTotalSize();
            progressName.setText(getSizeShow(progress * total / 100) + " / " + getSizeShow(total));
            progressBar.setProgress(progress);
        }
    }

    public String getSizeShow(double size) {
        if (mDownload.getTotalSize() > 1024) {
            size = size / 1024;
        }
        DecimalFormat df = new DecimalFormat("#.0");
        String sizeString = df.format(size);
        if (sizeString.startsWith(".")) {
            sizeString = "0" + sizeString;
        }
        if (mDownload.getTotalSize() > 1024) {
            return sizeString + "MB";
        } else {
            return sizeString + "KB";
        }
    }

    private void updateMediaStore(Context context, File savedFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(savedFile);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 删除单个文件
     *
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                DLog.e("--Method--", "Copy_Delete.deleteSingleFile: 删除单个文件" + filePath$Name + "成功！");
                return true;
            } else {
                DLog.e("--Method--", "删除单个文件" + filePath$Name + "失败！");
                return false;
            }
        } else {
            DLog.e("--Method--", "删除单个文件失败：" + filePath$Name + "不存在！");
            return false;
        }
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
            .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    ///// 拍照的问题

    //5.0以下使用
    private ValueCallback<Uri> uploadMessage;
    // 5.0及以上使用
    private ValueCallback<Uri[]> uploadMessageAboveL;
    //图片
    private final static int FILE_CHOOSER_RESULT_CODE = 128;
    //拍照
    private final static int FILE_CAMERA_RESULT_CODE = 129;
    //拍照图片路径
    private String cameraFielPath;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        if (requestCode == 1221) {
            if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                openImageChooserActivity();
            } else {
                ToastUtils.showShort("请先开启相机权限");
                if (uploadMessageAboveL != null) {
                    onActivityResultAboveL(null);
                } else if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }
            }
        }
    }

    private void openImageChooserActivity() {
        if (PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
            != PermissionChecker.PERMISSION_GRANTED) {
            String[] permissions = { Manifest.permission.CAMERA };
            requestPermissions(permissions, 1221);
            return;
        }
        CharSequence[] items = { "拍照", "相册" };
        new MaterialDialog.Builder(getContext())
            .items(items)
            .positiveText("取消")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    if (uploadMessageAboveL != null) {
                        uploadMessageAboveL.onReceiveValue(null);
                        uploadMessageAboveL = null;
                    }
                    if (uploadMessage != null) {
                        uploadMessage.onReceiveValue(null);
                        uploadMessage = null;
                    }
                    dialog.dismiss();
                }
            })
            .cancelable(false)
            .canceledOnTouchOutside(false)
            .itemsCallback(new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                    if (position == 0) {
                        takeCamera();
                    } else if (position == 1) {
                        takePhoto();
                    }
                }
            }).show();
    }

    //选择图片
    private void takePhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    //拍照
    public void takeCamera() {
        File saveFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");

        cameraFielPath = saveFile.getPath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(getContext(), saveFile));
            startActivityForResult(intent, FILE_CAMERA_RESULT_CODE);
        } else {
            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(saveFile));
            startActivityForResult(intentFromCapture, FILE_CAMERA_RESULT_CODE);
        }
    }

    private Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri =
                FileProvider.getUriForFile(context.getApplicationContext(),
                    RuntimeData.getInstance().getApplicationId() + ".fileProvider",
                    file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == uploadMessage && null == uploadMessageAboveL) return;
        if (resultCode != Activity.RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(null);
                uploadMessageAboveL = null;
            }
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }
            return;
        }
        Uri result = null;
        if (requestCode == FILE_CAMERA_RESULT_CODE) {
            if (null != data && null != data.getData()) {
                result = data.getData();
            }
            if (result == null && hasFile(cameraFielPath)) {
                result = Uri.fromFile(new File(cameraFielPath));
            }
            if (result == null) {
                return;
            }
            if (uploadMessageAboveL != null) {
                uploadMessageAboveL.onReceiveValue(new Uri[] { result });
                uploadMessageAboveL = null;
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (data != null) {
                result = data.getData();
            }
            if (uploadMessageAboveL != null) {
                onActivityResultAboveL(data);
            } else if (uploadMessage != null) {
                uploadMessage.onReceiveValue(result);
                uploadMessage = null;
            }
        }
    }

    /**
     * 判断文件是否存在
     */
    public static boolean hasFile(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null) {
                results = new Uri[] { Uri.parse(dataString) };
            }
        }
        uploadMessageAboveL.onReceiveValue(results);
        uploadMessageAboveL = null;
    }

    private String videoCallback;
    private Disposable payResult;
    private Disposable refreshWeb;
    private Disposable videoLookCallBean;

    public void cleanEvent() {
        if (payResult != null) {
            RxSubscriptions.remove(payResult);
            payResult = null;
        }
        if (refreshWeb != null) {
            RxSubscriptions.remove(refreshWeb);
            refreshWeb = null;
        }
        if (videoLookCallBean != null) {
            RxSubscriptions.remove(videoLookCallBean);
            videoLookCallBean = null;
        }
    }

    public void initEvent() {
        DLog.d("GosWebFragment", "init event ... ");
        payResult = RxBus.getDefault().toObservable(AliPayResultEvent.class)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<AliPayResultEvent>() {
                @Override
                public void accept(final AliPayResultEvent event) throws Exception {
                    if (event.getPayStatus() == AliPayResultEvent.PAYRESULT_OK) {
                        DLog.d("跳支付成功");
                    } else if (event.getPayStatus() == AliPayResultEvent.PAYRESULT_CANCEL) {
                        DLog.d("跳支付失败 cancel");
                    } else if (event.getPayStatus() == AliPayResultEvent.PAYRESULT_FAILE) {
                        DLog.d("跳支付失败");
                    }

                    sendJsFunctionSuccess("ntBridgePayResultCallback", event);
                }
            });

        RxSubscriptions.add(payResult);

        refreshWeb = RxBus.getDefault().toObservable(MRefreshWeb.class)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<MRefreshWeb>() {
                @Override
                public void accept(final MRefreshWeb event) throws Exception {
                    isNeedRefresh = true;
                }
            });

        RxSubscriptions.add(refreshWeb);

        videoLookCallBean = RxBus.getDefault().toObservable(CVideoLookResoultBean.class)
            .observeOn(AndroidSchedulers.mainThread()) //回调到主线程更新UI
            .subscribe(new Consumer<CVideoLookResoultBean>() {
                @Override
                public void accept(final CVideoLookResoultBean bean) throws Exception {
                    if (TextUtils.isEmpty(videoCallback)) {
                        return;
                    }
                    if (bean.getSuccess()) {
                        sendJsFunctionSuccess(videoCallback, "success");
                    } else {
                        sendJsFunctionFailer(videoCallback, "fail");
                    }
                }
            });
        //将订阅者加入管理站
        RxSubscriptions.add(videoLookCallBean);
    }

    private boolean isNeedRefresh = false;

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
