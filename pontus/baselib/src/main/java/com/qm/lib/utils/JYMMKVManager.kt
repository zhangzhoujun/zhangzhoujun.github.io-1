package com.qm.lib.utils

import com.qm.lib.http.RetrofitClient
import com.qm.lib.router.RouterManager
import com.tencent.mmkv.MMKV

/**
 * @ClassName JYMMKVManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 3:48 PM
 * @Version 1.0
 */
class JYMMKVManager private constructor() {
    companion object {
        var kv = MMKV.defaultMMKV()
        val instance: JYMMKVManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JYMMKVManager()
        }
    }

    fun setDeviceId(token: String) {
        kv.encode("deviceId", token)
    }

    fun getDeviceId(): String {
        return kv.decodeString("deviceId") ?: ""
    }

    fun setUserToken(token: String) {
        kv.encode("token", token)
    }

    fun getToken(): String {
        return kv.decodeString("token") ?: ""
    }

    fun setUserId(token: String) {
        kv.encode("user_id", token)
    }

    fun getUserId(): String {
        return kv.decodeString("user_id") ?: ""
    }

    fun setUserIdentity(token: String) {
        kv.encode("user_identity", token)
    }

    fun getUserIdentity(): String {
        return kv.decodeString("user_identity") ?: ""
    }

    fun setOpenScreen(open: Boolean) {
        kv.encode("is_open_screen", open)
    }

    fun getIsOpenScreen(): Boolean {
        return kv.decodeBool("is_open_screen") ?: false
    }

    fun setOpenScreenType(open: String) {
        kv.encode("is_open_screen_type", open)
    }

    fun getOpenScreenType(): String {
        return kv.decodeString("is_open_screen_type") ?: "1"
    }

    fun setAutoLogin(auto: Boolean) {
        kv.encode("auto_login", auto)
    }

    fun isAutoLogin(): Boolean {
        return kv.decodeBool("auto_login") ?: false
    }

    fun setShowLoadingDialog(auto: Boolean) {
        kv.encode("loading_dialog", auto)
    }

    fun isShowLoadingDialog(): Boolean {
        return kv.decodeBool("loading_dialog", true)
    }

    fun setTokenExpire(tokenExpire: Long) {
        kv.encode("token_expire", tokenExpire)
    }

    fun getTokenExpire(): Long {
        return kv.decodeLong("token_expire") ?: 0L
    }

    fun setFirstForVersion(version: String, auto: Boolean) {
        kv.encode("first_version_$version", auto)
    }

    fun isFirstForVersion(version: String): Boolean {
        return kv.decodeBool("first_version_$version", true) ?: false
    }

    fun setTodayVideoLookTime(time: Long) {
        kv.encode("video_look_day_time${getUserId()}", time)
    }

    fun getTodayVideoLookTime(): Long {
        return kv.decodeLong("video_look_day_time${getUserId()}")
    }

    fun setDialogTime(time: Long, dialogId: String) {
        kv.encode("dialog_last_show_time_${dialogId}_${getUserId()}", time)
    }

    fun getDialogTime(dialogId: String): Long {
        return kv.decodeLong("dialog_last_show_time_${dialogId}_${getUserId()}")
    }

    fun setTodayVideoLook(day: Int) {
        kv.encode("video_look_day${getUserId()}", day)
    }

    fun getTodayVideoLook(): Int {
        return kv.decodeInt("video_look_day${getUserId()}")
    }

    fun setAppConfigData(data: String) {
        kv.encode("app_config_data", data)
    }

    fun getAppConfigData(): String {
        return kv.decodeString("app_config_data") ?: ""
    }

    fun setWatchNum(data: String) {
        kv.encode("watch_num", data)
    }

    fun getWatchNum(): String {
        return kv.decodeString("watch_num") ?: ""
    }

    fun doLoginOut() {
        setUserToken("")
        setUserId("")
        RetrofitClient.getInstance().resetRetrofitClient()
        RouterManager.instance.gotoLoginActivity()
    }
}