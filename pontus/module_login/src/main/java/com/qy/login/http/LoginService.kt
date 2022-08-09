package com.qy.login.http

import com.qm.lib.entity.BaseResultBean
import com.qm.lib.widget.piccode.MPicCode
import com.qy.login.entity.MLoginResultBean
import com.qy.login.entity.MThirdAuth
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
interface LoginService {

    @POST("user/checkMobileVcode")
    open fun doLogin(@Body bean: RequestBody): Observable<BaseResultBean<MLoginResultBean>>

    @POST("user/thirdParty/auth")
    fun getUserThirdAuth(@Body bean: RequestBody): Observable<BaseResultBean<MThirdAuth>>
}