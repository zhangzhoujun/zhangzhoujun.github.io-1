package com.qm.lib.http;

import com.dim.library.utils.DLog;
import com.dim.library.utils.ToastUtils;
import com.qm.lib.entity.BaseResultBean;
import com.qm.lib.utils.JYMMKVManager;

import java.net.ConnectException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * 网络请求返回数据状态通用解析
 * <p>
 * Created by dim on 2017/7/2 10:52 邮箱：271756926@qq.com
 */

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {
        DLog.i("BaseObserver -> ", "onSubscribe");
    }

    @Override
    public void onComplete() {
        DLog.i("BaseObserver -> ", "onComplete");
    }

    @Override
    public void onError(Throwable e) {
        DLog.e("BaseObserver -> ", "onError -> " + e.getMessage() + "class Name :" + e.getClass().getName());
        Exception ex = handleException(e);
        ToastUtils.showShort(ex.getMessage());
    }

    @Override
    public void onNext(T t) {
        DLog.i("BaseObserver", "onNext --> " + t.toString());
        if (t == null) {
            ToastUtils.showShort("系统升级中");
            return;
        }

        if (t instanceof BaseResultBean) {
            onSuccess(t);
        } else {
            ToastUtils.showShort("系统升级中");
            throw new RuntimeException("class is not BaseResultBean !!!");
        }
    }

    protected void onFailed(T t) {

    }

    protected abstract void onSuccess(T t);

    public static final int HTTP_OK = 200;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public Exception handleException(Throwable e) {
        Exception ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ResponseBody responseBody = httpException.response().errorBody();
            DLog.d("App", "httpException.code() == " + httpException.code());
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    ex = new Exception("登录状态失效，请重新登录");
                    JYMMKVManager.Companion.getInstance().doLoginOut();
                    break;
                case INTERNAL_SERVER_ERROR:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex = new Exception("系统升级中");
                    break;
            }
            return ex;
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            ex = new Exception();
//            RxBus.getDefault().post(new ServerCrashEvent());
            return ex;
        }
        return new Exception();
    }
}
