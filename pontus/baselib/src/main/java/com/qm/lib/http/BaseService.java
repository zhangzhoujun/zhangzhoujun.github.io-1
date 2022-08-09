package com.qm.lib.http;

import com.qm.lib.entity.BaseResultBean;
import com.qm.lib.entity.CBaseAliPayBean;
import com.qm.lib.entity.MAppConfigBean;
import com.qm.lib.entity.MBaseAliPayBean;
import com.qm.lib.entity.MBaseConfig;
import com.qm.lib.entity.MGetUrlForServerBean;
import com.qm.lib.entity.MOssConfig;
import com.qm.lib.entity.MRefreshTokenBean;
import com.qm.lib.entity.MUserBean;
import com.qm.lib.entity.MVersionBean;
import com.qm.lib.widget.piccode.MPicCode;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @author dim
 * @create at 2019/3/8 14:01
 * @description:
 */
public interface BaseService {

    @POST("user/getCaptchaImg")
    Observable<BaseResultBean<MPicCode>> getCaptcha(@Body RequestBody bean);

    @GET("/api/get_user_data")
    Observable<BaseResultBean<MUserBean>> getUserInfo();

    @GET("/api/app_config")
    Observable<BaseResultBean<MAppConfigBean>> getAppConfig();

    @POST("user/mobileVcodeSend")
    Observable<BaseResultBean> getSms(@Body RequestBody bean);

    @POST("/api/save_user_data")
    Observable<BaseResultBean> sendSaveUserInfo(@Body RequestBody bean);

    @GET("/api/get_version")
    Observable<BaseResultBean<MVersionBean>> sendVersionCheck();

    @POST("api/home/refresh")
    Call<BaseResultBean<MRefreshTokenBean>> sendRefreshToken();

    @GET("api/oss/getsign")
    Observable<BaseResultBean<MOssConfig>> getOssConfig();

    @GET("api/config/general")
    Observable<BaseResultBean<MBaseConfig>> getbaseConfig();

    @POST("api/payment/alipay")
    Observable<BaseResultBean<MBaseAliPayBean>> createAliPay(@Body CBaseAliPayBean ben);

    /**
     * 根据配的地址，获取对应的接口数据
     *
     * @param url  配置的地址
     * @param bean RequestBody
     * @return
     */
    @POST
    Observable<BaseResultBean<MGetUrlForServerBean>> getUrlByServer(@Url String url, @Body RequestBody bean);
}