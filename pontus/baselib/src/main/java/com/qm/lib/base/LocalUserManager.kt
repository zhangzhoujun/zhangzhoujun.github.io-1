package com.qm.lib.base

import com.qm.lib.entity.MUserBean
import com.qm.lib.http.RetrofitClient
import com.qm.lib.utils.JYMMKVManager
import com.umeng.analytics.MobclickAgent

/**
 * @ClassName JYMMKVManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 3:48 PM
 * @Version 1.0
 */
class LocalUserManager private constructor() {
    companion object {
        val instance: LocalUserManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LocalUserManager()
        }
    }

    private var mUser: MUserBean? = null

    fun getUser(): MUserBean? {
        if (mUser == null) {
            return null
        }
        return mUser
    }

    fun setUser(userBean: MUserBean) {
        this.mUser = userBean
        JYMMKVManager.instance.setUserIdentity(userBean.level)
    }

    fun getUserIdentity(): String {
        if (mUser == null) {
            return JYMMKVManager.instance.getUserIdentity()
        }
        return mUser!!.level
    }

    fun getUserNickname(): String {
        if (mUser == null) {
            return ""
        }
        return mUser!!.nick_name
    }

    fun getUserHead(): String {
        if (mUser == null) {
            return ""
        }
        return mUser!!.nick_name
    }

    fun getUserMobile(): String {
        if (mUser == null) {
            return ""
        }
        return mUser!!.mobile
    }

    fun getUserId(): String {
        if (mUser == null) {
            return JYMMKVManager.instance.getUserId()
        }
        return mUser!!.id
    }

    fun login(userBean: MUserBean) {
        this.mUser = userBean
        JYMMKVManager.instance.setAutoLogin(true)
        JYMMKVManager.instance.setUserIdentity(userBean.level)
        JYMMKVManager.instance.setUserToken(userBean.token)
        JYMMKVManager.instance.setUserId(userBean.id)
        MobclickAgent.onProfileSignIn(userBean.id)
        RetrofitClient.getInstance().resetRetrofitClient()
    }

    fun logonOut() {
        mUser = null
        JYMMKVManager.instance.setUserToken("")
        JYMMKVManager.instance.setUserId("")
        JYMMKVManager.instance.setUserIdentity("")
        MobclickAgent.onProfileSignOff()
        RetrofitClient.getInstance().resetRetrofitClient()
    }
}