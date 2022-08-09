package com.qm.module_umshare.http

import com.qm.lib.entity.BaseResultBean
import com.qm.module_umshare.entity.MOnlyDialogBean
import com.qm.module_umshare.entity.MShareMainBean
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
interface ShareService {

    @POST("user/getShareInfo")
    fun getShareInfo(@Body bean: RequestBody): Observable<BaseResultBean<MShareMainBean>>

    @POST("user/homoPopUp")
    fun getHomePopUp(@Body bean: RequestBody): Observable<BaseResultBean<MOnlyDialogBean>>

    @POST("user/storePop")
    fun sendHomePopSure(@Body bean: RequestBody): Observable<BaseResultBean<Any>>
}