package com.qm.lib.widget.piccode;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.databinding.ObservableField;

import com.dim.library.utils.DLog;
import com.dim.library.utils.ToastUtils;
import com.google.gson.Gson;
import com.qm.lib.base.BaseBean;
import com.qm.lib.entity.BaseResultBean;
import com.qm.lib.http.BaseObserver;
import com.qm.lib.http.BaseService;
import com.qm.lib.http.PostJsonBody;
import com.qm.lib.http.RetrofitClient;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * @author dim
 * @create at 2019/3/25 15:37
 * @description:
 */
public class PicCodeViewModel {
    private PicCodeViewListener mListener;

    public ObservableField<String> codeInput = new ObservableField();
    public ObservableField<String> codeShow1 = new ObservableField();
    public ObservableField<String> codeShow2 = new ObservableField();
    public ObservableField<String> codeShow3 = new ObservableField();
    public ObservableField<String> codeShow4 = new ObservableField();

    public ObservableField<String> picCodePath = new ObservableField();
    private String picCodeKey;

    private String mobile;

    public PicCodeViewModel(PicCodeViewListener listener) {
        this.mListener = listener;
        sendGetPicCode();
    }

    public PicCodeViewModel(String mobile, PicCodeViewListener listener) {
        this.mListener = listener;
        this.mobile = mobile;
        sendGetPicCode();

    }

    public void setPicCodeViewListener(PicCodeViewListener listener) {
        this.mListener = listener;
    }

    public TextWatcher textWatah = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String afterText = s.toString();
            codeInput.set(afterText);

            if (afterText.length() == 4) {
                codeShow1.set(afterText.substring(0, 1));
                codeShow2.set(afterText.substring(1, 2));
                codeShow3.set(afterText.substring(2, 3));
                codeShow4.set(afterText.substring(3, 4));
                checkoutInputCode();
            } else if (afterText.length() == 3) {
                codeShow1.set(afterText.substring(0, 1));
                codeShow2.set(afterText.substring(1, 2));
                codeShow3.set(afterText.substring(2, 3));
                codeShow4.set("");
            } else if (afterText.length() == 2) {
                codeShow1.set(afterText.substring(0, 1));
                codeShow2.set(afterText.substring(1, 2));
                codeShow3.set("");
                codeShow4.set("");
            } else if (afterText.length() == 1) {
                codeShow1.set(afterText);
                codeShow2.set("");
                codeShow3.set("");
                codeShow4.set("");
            } else {
                codeShow1.set("");
                codeShow2.set("");
                codeShow3.set("");
                codeShow4.set("");
            }
        }
    };

    public void checkoutInputCode() {
        if (mListener != null) {
            mListener.onCheckoutResult(picCodeKey, codeInput.get());
        }
    }

    public void onRefreshPicCodeClick(View v) {
        sendGetPicCode();
    }

    public void sendGetPicCode() {
        BaseService service = RetrofitClient.getInstance().create(BaseService.class);


        BaseBean bean = new BaseBean(new CPicMobile(mobile));

        RequestBody requestBody = PostJsonBody.create(new Gson().toJson(bean));

        RetrofitClient.getInstance().execute(service.getCaptcha(requestBody), new JYObserverNoDialog<BaseResultBean<MPicCode>>() {

            @Override
            protected void onSuccess(BaseResultBean<MPicCode> picCodeBean) {
                if (picCodeBean.doesSuccess()) {
                    DLog.d("获取图片验证码成功");
                    picCodeKey = picCodeBean.getData().getRequestId();
                    picCodePath.set(picCodeBean.getData().getImg());
                } else {
                    ToastUtils.showShort(picCodeBean.getErrMsg());
                }

            }
        });
    }

    public interface PicCodeViewListener {
        public void onCheckoutResult(String key, String code);
    }

    public class JYObserverNoDialog<T> extends BaseObserver<T> {

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
}
