package com.qm.lib.http;

import android.content.Context;
import android.text.TextUtils;

import com.dim.library.http.cookie.CookieJarImpl;
import com.dim.library.http.cookie.store.PersistentCookieStore;
import com.dim.library.http.interceptor.BaseInterceptor;
import com.dim.library.http.interceptor.logging.Level;
import com.dim.library.http.interceptor.logging.LoggingInterceptor;
import com.dim.library.utils.DLog;
import com.dim.library.utils.Utils;
import com.qm.lib.utils.JYMMKVManager;
import com.qm.lib.utils.JYUtils;
import com.qm.lib.utils.MD5Utils;
import com.qm.lib.utils.RuntimeData;
import com.qm.lib.utils.SystemUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitClient封装单例类, 实现网络请求
 */
public class RetrofitClient {
    //超时时间
    private static final int DEFAULT_TIMEOUT = 20;
    //缓存大小
    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    //服务端根路径
    public static String baseUrl = RuntimeData.getInstance().getHttpUrl() + "/";


    private static Context mContext = Utils.getContext();

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private Cache cache = null;
    private File httpCacheDirectory;

    public static OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient() {
        this(baseUrl, null);
    }

    public void resetRetrofitClient() {
        new RetrofitClient();
    }

    private Map<String, String> createHttpHead() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Auth-Token", JYMMKVManager.Companion.getInstance().getToken());
        headers.put("Content-Type", "application/json");
        headers.put("X-Requested-With", "XMLHttpRequest");
        headers.put("X-App-Type", "Android");
        headers.put("appsystem", "Android");
        headers.put("X-App-Version", SystemUtil.getVersionName(Utils.getContext()));
        headers.put("X-Session-Id", JYMMKVManager.Companion.getInstance().getDeviceId());

        String deviceId = JYMMKVManager.Companion.getInstance().getDeviceId();
        long timeStamp = System.currentTimeMillis();
        String appId = JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "APP_ID");
        String appSecret = JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "APP_SECRET");
        headers.put("appId", appId);
        headers.put("phoneModel", SystemUtil.getSystemModel());
        headers.put("phoneBrand", SystemUtil.getDeviceBrand());
        headers.put("timeZone", "GMT+8");
        headers.put("language", "zh-CN");
        headers.put("appKey", JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "APP_KEY"));
        headers.put("timestamp", String.valueOf(timeStamp));
        headers.put("sign", MD5Utils.digest(appId + timeStamp + appSecret));
        headers.put("deviceId", deviceId);

        return headers;
    }

    private RetrofitClient(String url, Map<String, String> headers) {
        DLog.e("初始化 RetrofitClient");
        if (headers == null) {
            headers = createHttpHead();
        } else {
            headers.putAll(createHttpHead());
        }

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

//        if (httpCacheDirectory == null) {
//            httpCacheDirectory = new File(FileUtil.getAppCachePath());
//        }
//
//        try {
//            if (cache == null) {
//                cache = new Cache(httpCacheDirectory, CACHE_SIZE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            DLog.e(e.toString());
//        }
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJarImpl(new PersistentCookieStore(mContext)))
//                .cache(cache)
                .addInterceptor(new BaseInterceptor(headers))
                .addInterceptor(new RequestInterceptor())
//                .addInterceptor(new TokenInterceptor())
//                .addInterceptor(new CacheInterceptor(mContext))
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        //.loggable(BuildConfig.DEBUG) //是否开启日志打印
                        .loggable(true) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build()
                )
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    /**
     * /**
     * execute your customer API
     * For example:
     * MyApiService service =
     * RetrofitClient.getInstance(MainActivity.this).create(MyApiService.class);
     * <p>
     * RetrofitClient.getInstance(MainActivity.this)
     * .execute(service.lgon("name", "password"), subscriber)
     * * @param subscriber
     */

    public <T> T execute(Observable<T> observable, Observer<T> subscriber) {
        if (subscriber == null) {
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }

        return null;
    }
}
