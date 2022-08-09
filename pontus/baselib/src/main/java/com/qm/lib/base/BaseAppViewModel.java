package com.qm.lib.base;

import android.app.Activity;
import android.app.Application;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.dim.library.base.BaseViewModel;
import com.dim.library.utils.GsonUtils;
import com.google.gson.Gson;
import com.qm.lib.entity.BaseResultBean;
import com.qm.lib.entity.MAppConfigBean;
import com.qm.lib.entity.MUserBean;
import com.qm.lib.http.BaseObserver;
import com.qm.lib.http.BaseService;
import com.qm.lib.http.PostJsonBody;
import com.qm.lib.http.RetrofitClient;
import com.qm.lib.utils.JYMMKVManager;
import com.qm.lib.utils.RuntimeData;
import com.qm.lib.utils.SLSLogUtils;
import com.qm.lib.utils.StringUtils;
import com.qm.lib.widget.statusview.Gloading;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * @ClassName BaseAppViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/12 8:21 PM
 * @Version 1.0
 */
public class BaseAppViewModel extends BaseViewModel {

    protected Gloading.Holder mHolder;

    public BaseAppViewModel(@NonNull Application application) {
        super(application);
    }

    public class AppObserver<T> extends BaseObserver<T> {

        private boolean showDialog;
        private String dialogText;

        public AppObserver() {
            showDialog = true;
        }

        public AppObserver(boolean showDialog) {
            this.showDialog = showDialog;
        }

        @Override
        public void onSubscribe(Disposable d) {
            super.onSubscribe(d);
            if (showDialog) {
                showDialog("正在请求...");
            }
        }

        @Override
        public void onNext(T t) {
            super.onNext(t);
        }

        @Override
        public void onError(Throwable throwable) {
            super.onError(throwable);
            //关闭对话框
            dismissDialog();
        }

        @Override
        public void onComplete() {
            super.onComplete();
            //关闭对话框
            dismissDialog();
        }

        @Override
        protected void onSuccess(T o) {

        }

        @Override
        protected void onFailed(T t) {
            super.onFailed(t);
            //关闭对话框
            dismissDialog();
        }
    }

    public class AppObserverNoDialog<T> extends BaseObserver<T> {

        @Override
        public void onSubscribe(Disposable d) {
            super.onSubscribe(d);
        }

        @Override
        public void onError(Throwable throwable) {
            super.onError(throwable);
        }

        @Override
        public void onComplete() {
            super.onComplete();
        }

        @Override
        protected void onSuccess(T o) {

        }

        @Override
        protected void onFailed(T t) {
            super.onFailed(t);
        }

        @Override
        public void onNext(T t) {
            super.onNext(t);
        }
    }

    public void initLoadingStatusView(Activity activity) {
        initLoadingStatusView(activity, "");
    }

    public void initLoadingStatusView(ViewGroup view) {
        initLoadingStatusView(view, "");
    }

    public void initLoadingStatusView(Activity activity, String des) {
        mHolder = Gloading.getDefault().wrap(activity, des).withRetry(new Runnable() {
            @Override
            public void run() {
                onLoadRetry();
            }
        });
    }

    public void initLoadingStatusView(ViewGroup view, String des) {
        mHolder = Gloading.getDefault().wrap(view, des).withRetry(new Runnable() {
            @Override
            public void run() {
                onLoadRetry();
            }
        });
    }

    protected void onLoadRetry() {
        // override this method in subclass to do retry task
    }

    public void showLoading() {
        if (mHolder != null) {
            mHolder.showLoading();
        }
    }

    public void showLoadSuccess() {
        if (mHolder != null) {
            mHolder.showLoadSuccess();
        }
    }

    public void showLoadFailed() {
        if (mHolder != null) {
            mHolder.showLoadFailed();
        }
    }

    public void showEmpty() {
        if (mHolder != null) {
            mHolder.showEmpty();
        }
    }

    protected RequestBody getRequestBody(BaseBean bean) {
        return PostJsonBody.create(new Gson().toJson(bean));
    }

    protected RequestBody getRequestBody(Object bean) {
        return PostJsonBody.create(new Gson().toJson(bean));
    }

    protected void getUserInfo() {
        if (JYMMKVManager.Companion.getInstance().getAppConfigData().equals("")) {
            getAppConfig();
            return;
        }

        BaseService service = RetrofitClient.getInstance().create(BaseService.class);

        RetrofitClient.getInstance()
            .execute(service.getUserInfo(),
                new AppObserverNoDialog<BaseResultBean<MUserBean>>() {

                    @Override
                    protected void onSuccess(BaseResultBean<MUserBean> userBean) {
                        if (userBean.doesSuccess()) {
                            LocalUserManager.Companion.getInstance().setUser(userBean.getData());
                            // 判断当前用户是不是黑名单用户
                            String appConfig = JYMMKVManager.Companion.getInstance().getAppConfigData();
                            if (appConfig != null && !StringUtils.Companion.getInstance().isEmpty(appConfig)) {
                                MAppConfigBean config = GsonUtils.fromJson(appConfig, MAppConfigBean.class);
                                if (config != null) {
                                    if (config.getBlacklist() != null) {
                                        String blackListData = config.getBlacklist();
                                        if (blackListData != null && !StringUtils.Companion.getInstance()
                                            .isEmpty(blackListData)) {
                                            String[] blackList = blackListData.split(",");
                                            for (int i = 0; i < blackList.length; i++) {
                                                if (blackList[i].equals(userBean.getData().getMobile())) {
                                                    RuntimeData.getInstance().setHideBlock(true);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            onGetUserInfoBack(userBean.getData());
                        } else {
                            SLSLogUtils.Companion.getInstance()
                                .sendLogHttp("HTTP", "USER", "getUserInfo", -1, "", userBean.getErrMsg());
                            onGetUserInfoBack(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        SLSLogUtils.Companion.getInstance()
                            .sendLogHttp("HTTP", "USER", "getUserInfo", -1, "", throwable.getMessage());
                        onGetUserInfoBack(null);
                    }
                });
    }

    protected void onGetUserInfoBack(MUserBean userBean) {

    }

    protected void getAppConfig() {
        BaseService service = RetrofitClient.getInstance().create(BaseService.class);

        RetrofitClient.getInstance().execute(service.getAppConfig(),
            new AppObserverNoDialog<BaseResultBean<MAppConfigBean>>() {

                @Override
                protected void onSuccess(BaseResultBean<MAppConfigBean> userBean) {
                    if (userBean.doesSuccess()) {
                        if (userBean.getData() != null) {
                            // 本地缓存
                            JYMMKVManager.Companion.getInstance()
                                .setAppConfigData(GsonUtils.objectToJsonStr(userBean.getData()));

                            if (userBean.getData() != null) {
                                //JYMMKVManager.Companion.getInstance().setOpenScreen(!userBean.getData().getAppConfig().getOpenScreen().equals("0"));
                                //JYMMKVManager.Companion.getInstance().setOpenScreenType(userBean.getData().getAppConfig().getOpenScreen());
                                RuntimeData.getInstance().setMustLogin(userBean.getData().getMust_login());
                            }
                        }
                        onGetAppConfig(userBean.getData());
                    } else {
                        onGetAppConfig(null);
                    }
                }

                @Override
                public void onError(Throwable throwable) {
                    super.onError(throwable);
                    onGetAppConfig(null);
                }
            });
    }

    protected void onGetAppConfig(MAppConfigBean bean) {

    }
}
