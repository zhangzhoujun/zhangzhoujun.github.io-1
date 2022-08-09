package com.gos.module_web;

import android.app.Application;
import android.graphics.Color;
import android.text.TextUtils;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;

import com.dim.library.binding.command.BindingCommand;
import com.dim.library.bus.event.SingleLiveEvent;
import com.dim.library.utils.DLog;
import com.dim.library.utils.RxUtils;
import com.dim.library.utils.ToastUtils;
import com.gos.module_web.entity.CJsPagerInfo;
import com.gos.module_web.pay.AliPayUtils;
import com.qm.lib.entity.BaseResultBean;
import com.qm.lib.entity.CBaseAliPayBean;
import com.qm.lib.entity.MBaseAliPayBean;
import com.qm.lib.http.BaseService;
import com.qm.lib.http.RetrofitClient;
import com.qm.lib.widget.toolbar.JYToolbarOptions;
import com.qm.lib.widget.toolbar.ToolbarViewModel;
import com.tencent.smtt.sdk.WebView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author dim
 * @create at 2019/3/18 14:37
 * @description:
 */
public class GosWebViewModel extends ToolbarViewModel {

    private boolean doesHideLeft = false;
    private boolean doesHideClose = false;
    public ObservableField<String> url = new ObservableField<>();
    public ObservableField<String> title = new ObservableField();
    public ObservableBoolean rightClick = new ObservableBoolean(false);

    private CJsPagerInfo pagerInfo;

    private String finishUrl;
    private boolean isSetPage = false;

    public SingleLiveEvent<Boolean> doesCanLoadMore = new SingleLiveEvent<>();

    private WebView mWebView;
    private FragmentActivity mActivity;


    public void initParam(FragmentActivity activity) {
        this.mActivity = activity;
    }


    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //下拉刷新完成
        public ObservableBoolean finishRefreshing = new ObservableBoolean(false);
        //上拉加载完成
        public ObservableBoolean finishLoadmore = new ObservableBoolean(false);
    }

    public GosWebViewModel(@NonNull Application application) {
        super(application);
    }

    private void initToolbar() {
        JYToolbarOptions option = new JYToolbarOptions();
        option.setTitleString(title.get());
        option.setNeedNavigate(!doesHideLeft);
        option.setDoesShowClose(!doesHideClose);
        option.setBackId(R.drawable.lib_base_back);
        setOptions(option);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(() -> {

        Observable.just("")
                .delay(2, TimeUnit.SECONDS)
                .compose(RxUtils.bindToLifecycle(getLifecycleProvider()))
                .compose(RxUtils.schedulersTransformer())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(Object o) throws Exception {
                        refreshLoadUrl();

                        uc.finishRefreshing.set(!uc.finishRefreshing.get());
                    }
                });
    });

    public void refreshLoadUrl() {
        mWebView.reload();
    }

    public void setFinishUrl(String finishUrl) {
        this.finishUrl = finishUrl;
    }

    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(() -> {
        uc.finishLoadmore.set(!uc.finishLoadmore.get());
    });

    public void refreshWebView() {

    }

    public void setData(String webUrl, String webTitle, boolean hideLeft, boolean hideClose, WebView webView) {
        url.set(webUrl);
        title.set(webTitle);
        doesHideLeft = hideLeft;
        doesHideClose = hideClose;
        this.mWebView = webView;
        initToolbar();
    }

    public void setTitleInfo(String title) {
        // if (TextUtils.isEmpty(title) || isSetPage) {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        if (pagerInfo == null) {
            pagerInfo = new CJsPagerInfo();
        }
        pagerInfo.setTitle(title);
        setTitleInfo(pagerInfo);
    }

    public void setTitleInfo(CJsPagerInfo pagerInfo) {
        this.pagerInfo = pagerInfo;
        isSetPage = true;
        if (!TextUtils.isEmpty(pagerInfo.getTitle())) {
            title.set(pagerInfo.getTitle());
        }
        JYToolbarOptions option = new JYToolbarOptions();
        option.setTitleString(title.get());
        option.setDoesShowClose(!doesHideClose);
        option.setNeedNavigate(!doesHideLeft);
        option.setBackId(R.drawable.lib_base_back);
        if (pagerInfo.getMenus() != null && !pagerInfo.getMenus().isEmpty()) {
            CJsPagerInfo.MenusBean bean = pagerInfo.getMenus().get(0);
            if (!TextUtils.isEmpty(bean.getText())) {
                option.setRightString(bean.getText());
                if (!TextUtils.isEmpty(bean.getColor())) {
                    option.setRightStringColor(Color.parseColor(bean.getColor()));
                }
            } else {
                option.setRightIconUrl(bean.getIcon());
            }
        }
        try {
            if (!TextUtils.isEmpty(pagerInfo.getBg_color())) {
                option.setBgColor(Color.parseColor(pagerInfo.getBg_color()));
            }
            if (!TextUtils.isEmpty(pagerInfo.getColor())) {
                option.setTitleColor(Color.parseColor(pagerInfo.getColor()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setOptions(option);
    }

    @Override
    protected void onLoadRetry() {
        DLog.d("重加载");
        refreshLoadUrl();
        uc.finishRefreshing.set(!uc.finishRefreshing.get());
    }

    @Override
    protected void onViewCloseClick() {
        finish();
    }

    @Override
    protected void onRightClick() {
        rightClick.set(!rightClick.get());
    }

    @Override
    protected void setOnBackClick() {
        if (this.mWebView != null && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
        } else {
            super.setOnBackClick();
        }
    }

    public CJsPagerInfo getPagerInfo() {
        return pagerInfo;
    }

    public void getAliPayData(String orserSn) {
        BaseService service = RetrofitClient.getInstance().create(BaseService.class);

        RetrofitClient.getInstance().execute(service.createAliPay(new CBaseAliPayBean(orserSn)), new AppObserver<BaseResultBean<MBaseAliPayBean>>() {

            @Override
            protected void onSuccess(BaseResultBean<MBaseAliPayBean> resultBean) {
                if (resultBean.doesSuccess()) {
                    new AliPayUtils().startAliPay(mActivity, resultBean.getData().getUrl(), orserSn);
                } else {
                    ToastUtils.showShort(resultBean.getErrMsg());
                }

            }
        });
    }

    protected void onLocationBack(boolean aBoolean) {
        DLog.d("AAAA", "onLocationBack -> " + aBoolean);
        refreshLoadUrl();
    }
}
