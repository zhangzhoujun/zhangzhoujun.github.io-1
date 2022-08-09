package com.qm.lib.http;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.qm.lib.router.RouterManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @ClassName TokenInterceptor
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/28 2:46 PM
 * @Version 1.0
 */
public class TokenInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int responseCode = response.code();
        if (responseCode == 401 || responseCode == 403) {
            String token = TokenManager.Companion.getNewToken(response.receivedResponseAtMillis() / 1000);
            if (TextUtils.isEmpty(token)) {
                failed();
            } else {
                Request newRequest = request.newBuilder()
                        .header("X-Auth-Token", token)
                        .build();
                response.body().close();
                return chain.proceed(newRequest);
            }
        }
        return response;
    }

    private void failed() {
        RouterManager.Companion.getInstance().gotoLoginActivity();
    }
}