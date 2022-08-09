package com.dim.library.http.interceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by  on 2017/5/10.
 */
public class BaseInterceptor implements Interceptor {
    private Map<String, String> headers;

    public BaseInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request()
            .newBuilder();
        if (headers != null && headers.size() > 0) {
            Set<String> keys = headers.keySet();
            for (String headerKey : keys) {
                builder.addHeader(headerKey, headers.get(headerKey)).build();
            }
        }

        CacheControl cc = new CacheControl.Builder()
            //不使用缓存，但是会保存缓存数据
            //.noCache()
            //不使用缓存，同时也不保存缓存数据
            .noStore()
            //只使用缓存，（如果我们要加载的数据本身就是本地数据时，可以使用这个，不过目前尚未发现使用场景）
            //.onlyIfCached()
            //手机可以接收响应时间小于当前时间加上10s的响应
            .minFresh(10, TimeUnit.SECONDS)
            //手机可以接收有效期不大于10s的响应
            //                .maxAge(10,TimeUnit.SECONDS)
            //手机可以接收超出5s的响应
            //.maxStale(5, TimeUnit.SECONDS)
            .build();
        builder.cacheControl(cc);

        //请求信息
        return chain.proceed(builder.build());
    }
}