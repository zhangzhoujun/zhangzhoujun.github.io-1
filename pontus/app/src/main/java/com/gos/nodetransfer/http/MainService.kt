package com.gos.nodetransfer.http

import com.gos.nodetransfer.entity.*
import com.qm.lib.entity.BaseResultBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * @ClassName MimeService
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 9:06 PM
 * @Version 1.0
 */
interface MainService {

    @POST("user/getAdvertisingDayNumber")
    fun getAdvCount(@Body bean: RequestBody): Observable<BaseResultBean<MAdvertisingDayNum>>

    @POST("user/getAdvertisingStoreView")
    fun sendSaveAdvView(@Body bean: RequestBody): Observable<BaseResultBean<MAdvertisingDayNum>>

    @POST("user/task/list")
    fun sendGetTaskList(@Body bean: RequestBody): Observable<BaseResultBean<MTaskListBean>>

    @POST("user/task/store")
    fun sendStoreTask(@Body bean: RequestBody): Observable<BaseResultBean<MTaskResultBean>>

    @POST("user/thirdParty/auth")
    fun getUserThirdAuth(@Body bean: RequestBody): Observable<BaseResultBean<MThirdAuth>>

    @POST("user/getPackageConfig")
    fun getAppDownload(@Body bean: RequestBody): Observable<BaseResultBean<MGetDownloadBean>>

    @POST("step/sync")
    fun sendStepSync(@Body bean: RequestBody): Observable<BaseResultBean<Any>>
}