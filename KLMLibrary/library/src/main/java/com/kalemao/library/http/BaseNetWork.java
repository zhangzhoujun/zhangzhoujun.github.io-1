package com.kalemao.library.http;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kalemao.library.base.MResponse;
import com.kalemao.library.base.RunTimeData;
import com.kalemao.library.http.api.KLMAPI;
import com.kalemao.library.http.api.MyHttpLoggingInterceptor;
import com.kalemao.library.http.api.UploadService;
import com.kalemao.library.logutils.okhttplog.KLMHttpLoggingInterceptor;
import com.kalemao.library.logutils.okhttplog.LogInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by dim on 2017/5/19 14:03 邮箱：271756926@qq.com
 */

public class BaseNetWork {
    /**
     * 测试
     */
    // protected static final String IP_FOR_MIAOMI =
    // "http://ares-test.wsmall.com.my/";
    // protected static final String IP_FOR_KALEMAO =
    // "http://test.wsmall.com.my/";
    // protected static final String IP_FOR_WANSE = "http://tt.ewanse.com/";

    /**
     * ol
     */
    protected static final String IP_FOR_MIAOMI  = "http://ares.wsmall.com.my/";
    protected static final String IP_FOR_KALEMAO = "https://www.wsmall.com.my/";

    // /**
    // * dev
    // */
    // protected static final String IP_FOR_MIAOMI =
    // "http://ares-dev.wsmall.com.my/";
    // protected static final String IP_FOR_KALEMAO =
    // "http://ares-test.wsmall.com.my/";
    // protected static final String IP_FOR_WANSE = "http://tt.ewanse.com/";

    public static OkHttpClient    okHttpClient;
    public static Object          klmApi;
    public static Object          mmApi;
    private static boolean        mDoesNeedLog   = false;

    protected static BaseNetWork  instance;

    public static BaseNetWork getInstance() {
        if (instance == null) {
            instance = new BaseNetWork();
        }
        return instance;
    }

    public void afterHttpHeadChanged() {
        okHttpClient = null;
        if (klmApi != null) {
            klmApi = null;
            getKlmApi();
        }
        if (mmApi != null) {
            mmApi = null;
            getMmApi();
        }
        onHttpheadChanged();
    }

    protected void onHttpheadChanged() {
    }

    /**
     * 是否需要打开http请求的日志，并且重新new client
     * 
     * @param doesNeedLog
     */
    public void genericClientForLog(boolean doesNeedLog) {
        // 如果和当前的日志开关一样，那么不设置
        if (mDoesNeedLog == doesNeedLog) {
            return;
        }
        if (okHttpClient != null) {
            okHttpClient = null;
        }
        mDoesNeedLog = doesNeedLog;
        genericClient();
    }

    public OkHttpClient genericClient() {
        if (okHttpClient != null) {
            return okHttpClient;
        }
        KLMHttpLoggingInterceptor interceptor = new KLMHttpLoggingInterceptor(new LogInterceptor(), RunTimeData.getInstance().getHttpHead());
        if (mDoesNeedLog) {
            interceptor.setLevel(KLMHttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(KLMHttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(interceptor);

        // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
        final X509TrustManager trustAllCert = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        };
        final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert, RunTimeData.getInstance().getmContext());
        builder.sslSocketFactory(sslSocketFactory, trustAllCert);

        builder.build();
        okHttpClient = builder.build();
        return okHttpClient;
    }

    public KLMAPI getKlmApi() {
        if (klmApi == null) {
            klmApi = getApi(KLMAPI.class, IP_FOR_KALEMAO, new MResponse.JsonAdapter());
        }
        return (KLMAPI) klmApi;
    }

    public KLMAPI getMmApi() {
        if (mmApi == null) {
            mmApi = getApi(KLMAPI.class, IP_FOR_MIAOMI, new MResponse.JsonAdapter());
        }
        return (KLMAPI) mmApi;
    }

    public Object getApi(Class apiClass, String baseIp, Object typeAdapter) {
        Gson gson = new GsonBuilder().registerTypeAdapter(MResponse.class, typeAdapter).create();
        Retrofit retrofit = new Retrofit.Builder().client(genericClient()).baseUrl(baseIp).addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        return retrofit.create(apiClass);
    }

    /**
     * 获取上传参数
     * 
     * @param baseIp
     * @return
     */
    public UploadService getUploadApi(String baseIp) {
        Retrofit retrofit = new Retrofit.Builder().client(getUploadClient()).baseUrl(baseIp).addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        return retrofit.create(UploadService.class);
    }

    private OkHttpClient getUploadClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        MyHttpLoggingInterceptor interceptor = new MyHttpLoggingInterceptor().setLevel(MyHttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        builder.build();
        return builder.build();
    }
}