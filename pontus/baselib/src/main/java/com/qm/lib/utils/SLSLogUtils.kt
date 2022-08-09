package com.qm.lib.utils

import android.text.TextUtils
import android.util.Log
import com.aliyun.sls.android.producer.LogProducerCallback
import com.aliyun.sls.android.producer.LogProducerClient
import com.aliyun.sls.android.producer.LogProducerConfig
import com.aliyun.sls.android.producer.LogProducerResult
import com.dim.library.utils.GsonUtils
import com.dim.library.utils.Utils
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.CSlsLogBean
import com.umeng.commonsdk.utils.UMUtils


/**
 * @ClassName JYMMKVManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 3:48 PM
 * @Version 1.0
 */
class SLSLogUtils private constructor() {
    companion object {
        val instance: SLSLogUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SLSLogUtils()
        }
    }

    private var client: LogProducerClient? = null
    private var umId: String? = ""

    fun sendLogHttp(
        pageName: String,
        pageKey: String,
        type: String,
        pos: Int = -1,
        posString: String = "",
        extra: String = ""
    ) {
        sendLogToServer(pageName, pageKey, type, pos, posString, event = "HTTP", extra = extra)
    }

    fun sendLogLoad(
        pageName: String,
        pageKey: String,
        type: String,
        pos: Int = -1,
        posString: String = ""
    ) {
        sendLogToServer(pageName, pageKey, type, pos, posString, event = "LOAD")
    }

    fun sendLogError(
        pageName: String,
        pageKey: String,
        type: String,
        pos: Int = -1,
        posString: String = "",
        extra: String = ""
    ) {
        sendLogToServer(pageName, pageKey, type, pos, posString, event = "ERROR", extra = extra)
    }

    fun sendLogSync(
        pageName: String,
        pageKey: String,
        type: String,
        pos: Int = -1,
        posString: String = "",
        extra: String = ""
    ) {
        sendLogToServer(pageName, pageKey, type, pos, posString, event = "SYNC", extra = extra)
    }

    fun sendLogClick(
        pageName: String,
        pageKey: String,
        type: String,
        pos: Int = -1,
        posString: String = "",
        extra: String = "",
        viewId: String = ""
    ) {
        var viewFinId = viewId
        if (TextUtils.isEmpty(viewFinId)) {
            viewFinId = type
        }
        sendLogToServer(
            pageName, pageKey, type, pos, posString, event = "CLICK", extra = extra,
            viewId = viewFinId
        )
    }

    fun sendLogAdStatus(
        pageName: String,
        pageKey: String,
        type: String,
        pos: Int = -1,
        posString: String = "",
        extra: String = ""
    ) {
        sendLogToServer(
            pageName,
            pageKey,
            type,
            pos,
            posString,
            event = "AD_STATUS",
            extra = extra
        )
    }

    fun sendLogJuggleClick(
        pageKeyInput: String,
        type: String = "",
        pos: Int = -1,
        posString: String = "",
        viewId: String = "",
        imgUrl: String = "",
        event: String = "CLICK"
    ) {
        sendLogToServer("JUGGLE", pageKeyInput, type, pos, posString, viewId, imgUrl, event)
    }

    private fun sendLogToServer(
        pageName: String,
        pageKeyInput: String,
        type: String = "",
        pos: Int = -1,
        posString: String = "",
        viewId: String = "",
        imgUrl: String = "",
        event: String = "CLICK",
        extra: String = ""
    ) {
//        if (TextUtils.isEmpty(umId)) {
//            umId = UMUtils.getUMId(Utils.getContext())
//        }
//        if (TextUtils.isEmpty(umId)) {
//            umId = "-1"
//        }
//
//        var pageKey = pageKeyInput
//        if (pageKeyInput == null) {
//            pageKey = ""
//        }
//        var appName = JYUtils.instance.getMetaDatByName(
//            Utils.getContext(),
//            "QY_APP_NAME"
//        )
//
//        var viewFinId = viewId
//        if (TextUtils.isEmpty(viewFinId)) {
//            viewFinId = type
//        }
//        if (client == null) {
//            // endpoint前需要加 https://
//            val endpoint = JYUtils.instance.getMetaDatByName(Utils.getContext(), "SLS_LOG_endpoint")
//            val project = JYUtils.instance.getMetaDatByName(Utils.getContext(), "SLS_LOG_project")
//            val logstore = JYUtils.instance.getMetaDatByName(Utils.getContext(), "SLS_LOG_logstore")
//            val accesskeyid =
//                JYUtils.instance.getMetaDatByName(Utils.getContext(), "SLS_LOG_accesskeyid")
//            val accesskeysecret =
//                JYUtils.instance.getMetaDatByName(Utils.getContext(), "SLS_LOG_accesskeysecret")
//            var config =
//                LogProducerConfig(endpoint, project, logstore, accesskeyid, accesskeysecret)
//
//            // 设置主题
//            config.setTopic(appName)
//
//            // 回调函数不填，默认无回调
//            client = LogProducerClient(config,
//                LogProducerCallback { resultCode, reqId, errorMessage, logBytes, compressedBytes -> // 回调
//                    // resultCode       返回结果代码
//                    // reqId            请求id
//                    // errorMessage     错误信息，没有为null
//                    // logBytes         日志大小
//                    // compressedBytes  压缩后日志大小
//                    Log.d(
//                        "LogProducerCallback", String.format(
//                            "%s %s %s %s %s",
//                            LogProducerResult.fromInt(resultCode),
//                            reqId,
//                            errorMessage,
//                            logBytes,
//                            compressedBytes
//                        )
//                    )
//                })
//        }
//
//        val log = com.aliyun.sls.android.producer.Log()
//
//        var posSend = pos.toString()
//        if (posString != null && !TextUtils.isEmpty(posString)) {
//            posSend = posString
//        }
//
//        //添加统一参数
//        val deviceId = JYMMKVManager.instance.getDeviceId()
//        val appId = JYUtils.instance.getMetaDatByName(
//            Utils.getContext(),
//            "QY_APP_ID"
//        )
//        val token = JYMMKVManager.instance.getToken()
//        val userId = LocalUserManager.instance.getUserId()
//        val mobile = LocalUserManager.instance.getUserMobile()
//        val timeStamp = System.currentTimeMillis().toString()
//        val version = SystemUtil.getVersionName(Utils.getContext())
//
//        val phoneModel = SystemUtil.getSystemModel()
//        val phoneBrand = SystemUtil.getDeviceBrand()
//        val phoneVersion = SystemUtil.getSystemVersion()
//
//        var userBean = CSlsLogBean(
//            userId,
//            token,
//            "Android",
//            mobile,
//            deviceId,
//            pageKey,
//            type,
//            posSend,
//            version,
//            appId,
//            appName,
//            timeStamp,
//            viewFinId,
//            imgUrl,
//            RuntimeData.getInstance().httpUrl,
//            event,
//            pageName,
//            umId!!,
//            phoneModel,
//            phoneBrand,
//            phoneVersion,
//            extra
//        )
//
//        log.putContent("pageName", pageName)
//        log.putContent("userId", userId)
//        log.putContent("userToken", token)
//        log.putContent("appType", "Android")
//        log.putContent("mobile", mobile)
//        log.putContent("deviceId", deviceId)
//        log.putContent("pageKey", pageKey)
//        log.putContent("type", type)
//        log.putContent("pos", posSend)
//        log.putContent("version", version)
//        log.putContent("appId", appId)
//        log.putContent("appName", appName)
//        log.putContent("timeStamp", timeStamp)
//        log.putContent("viewID", viewFinId)
//        log.putContent("imgUrl", imgUrl)
//        log.putContent("event", event)
//        log.putContent("extra", extra)
//        log.putContent("umId", umId!!)
//        log.putContent("phoneModel", phoneModel)
//        log.putContent("phoneBrand", phoneBrand)
//        log.putContent("phoneVersion", phoneVersion)
//        log.putContent("host", RuntimeData.getInstance().httpUrl)
//        log.putContent("data", GsonUtils.objectToJsonStr(userBean))
//        // addLog第二个参数flush，是否立即发送，1代表立即发送，不设置时默认为0
//        val res = client!!.addLog(log, 1)
    }
}