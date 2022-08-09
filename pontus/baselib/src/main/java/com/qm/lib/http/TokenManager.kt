package com.qm.lib.http

import com.qm.lib.utils.JYMMKVManager
import java.io.IOException

/**
 * @ClassName TokenManagerEx
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/28 4:35 PM
 * @Version 1.0
 */
class TokenManager {
    /**
     * 刷新当前token
     *
     * @param currentTime 当前时间
     * @throws IOException
     */
    companion object {
        @Synchronized
        @Throws(IOException::class)
        fun getNewToken(currentTime: Long): String? {
            val tokenExpireTime = JYMMKVManager.instance.getTokenExpire()
            return if (currentTime < tokenExpireTime) {
                JYMMKVManager.instance.getToken()
            } else {
                val service =
                    RetrofitClient.getInstance().create(
                        BaseService::class.java
                    )
                val call = service.sendRefreshToken()
                val userBean = call.execute().body()
                if (userBean == null || !userBean.doesSuccess()) {
                    ""
                } else {
                    JYMMKVManager.instance.setUserToken(userBean.data.token)
                    JYMMKVManager.instance.setTokenExpire(userBean.data.exp)
                    RetrofitClient.getInstance().resetRetrofitClient()
                    userBean.data.token
                }
            }
        }
    }
}