package com.kalemao.library.http;

import com.kalemao.library.base.RunTimeData;
import com.kalemao.library.logutils.LogUtil;
import com.kalemao.library.utils.BaseToast;
import com.kalemao.library.utils.PackageUtil;

import android.content.Intent;

import retrofit2.HttpException;
import rx.Observer;

/**
 * Created by dim on 2017/7/2 10:52 邮箱：271756926@qq.com
 */

public abstract class BaseObserver<T> implements Observer<T> {

    @Override
    public void onCompleted() {

    }

    public void onError(Throwable e) {
        LogUtil.e("BaseObserver", e.getMessage());
        // todo error somthing

        handleException(e);
    }

    @Override
    public void onNext(T t) {
        LogUtil.e("BaseObserver", "");
    }

    private static final int UNAUTHORIZED          = 401;
    private static final int FORBIDDEN             = 403;
    private static final int NOT_FOUND             = 404;
    private static final int REQUEST_TIMEOUT       = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY           = 502;
    private static final int SERVICE_UNAVAILABLE   = 503;
    private static final int GATEWAY_TIMEOUT       = 504;

    public Exception handleException(Throwable e) {
        Exception ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new Exception();
            LogUtil.d("App", "httpException.code() == " + httpException.code());
            switch (httpException.code()) {
            case UNAUTHORIZED:
                ex = new Exception("登录状态失效，请重新登录");
                BaseToast.showShort(RunTimeData.getInstance().getmContext(), "登录状态失效，请重新登录");
                // 401错误，需要重新登陆
                Intent intent = new Intent();
                if (PackageUtil.doesKLMApp(RunTimeData.getInstance().getmContext())) {
                    intent.setClassName(RunTimeData.getInstance().getmContext(), "com.kalemao.thalassa.ui.person.Login");
                    RunTimeData.getInstance().getmContext().startActivity(intent);
                } else {
                    intent.setClassName(RunTimeData.getInstance().getmContext(), "com.ewanse.cn.login.LoginActivity");
                    intent.putExtra("relogin_as_invalide_token", true);
                }
                RunTimeData.getInstance().getmContext().startActivity(intent);
                break;
            case NOT_FOUND:
            case REQUEST_TIMEOUT:
            case GATEWAY_TIMEOUT:
            case INTERNAL_SERVER_ERROR:
            case BAD_GATEWAY:
            case SERVICE_UNAVAILABLE:
            default:
                // ex.message = "网络错误";
                break;
            }
            return ex;
        }
        return new Exception();
    }
}
