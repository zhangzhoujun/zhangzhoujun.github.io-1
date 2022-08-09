package com.qm.module_juggle.http

import com.google.gson.JsonObject
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.entity.MGoodsBean
import com.qm.lib.entity.MUserBean
import com.qm.module_juggle.entity.MHomeBtGetBean
import com.qm.module_juggle.entity.MHomeBtList
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.entity.MHomeDiyBean
import com.qm.module_juggle.entity.MShowDialogServerBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.Objects

/**
 * @ClassName HomeService
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 4:47 PM
 * @Version 1.0
 */
interface HomeService {

    @GET("/api/get_loki_data")
    fun getPageConfig(@Query("key") key :String): Observable<BaseResultBean<MHomeDataBean>>

    @GET("/api/get_loki_data")
    fun getPageDiyConfig(@Query("key") key :String): Observable<BaseResultBean<MHomeDiyBean>>

    @POST("/api/login")
    fun sendLogin(@Body bean: RequestBody): Observable<BaseResultBean<MUserBean>>

    @POST("user/home/token")
    fun getHomeNtList(@Body bean: RequestBody): Observable<BaseResultBean<MHomeBtList>>

    @POST("user/home/gainToken")
    fun getBt(@Body bean: RequestBody): Observable<BaseResultBean<MHomeBtGetBean>>

    @GET
    fun getDiyInfo(
        @Url url: String
    ): Observable<BaseResultBean<ArrayList<JsonObject>>>


    @POST
    fun postDiyInfo(
        @Url url: String
    ): Observable<BaseResultBean<ArrayList<JsonObject>>>

    @GET
    fun getIndoAssets(
        @Url url: String
    ): Observable<BaseResultBean<JsonObject>>

    @POST
    fun getGoodsList(
        @Url url: String,
        @Body bean: RequestBody
    ): Observable<BaseResultBean<MGoodsBean>>

    @GET
    fun getDialogByServer(
        @Url url: String
    ): Observable<BaseResultBean<MShowDialogServerBean>>

    @GET
    fun getStatusByServer(
        @Url url: String
    ): Observable<BaseResultBean<MShowDialogServerBean>>
}