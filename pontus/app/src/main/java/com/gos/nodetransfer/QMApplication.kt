package com.gos.nodetransfer

import android.app.ActivityManager
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.os.Build
import android.os.Process
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import com.dim.library.base.BaseApplication
import com.dim.library.bus.RxBus
import com.dim.library.utils.DLog
import com.dim.library.utils.GsonUtils
import com.dim.library.utils.StringUtils
import com.google.gson.Gson
import com.gos.nodetransfer.entity.CSendPushTokenMessage
import com.gos.nodetransfer.oaid.IMEIUtil
import com.gos.nodetransfer.oaid.OAIDHelper
import com.qm.lib.base.BaseBean
import com.qm.lib.base.LocalUserManager
import com.qm.lib.config.ModuleLifecycleConfig
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.entity.CUserInfoBean
import com.qm.lib.http.BaseObserver
import com.qm.lib.http.BaseService
import com.qm.lib.http.PostJsonBody
import com.qm.lib.http.RetrofitClient
import com.qm.lib.utils.JYMMKVManager
import com.qm.lib.utils.JYUtils
import com.qm.lib.utils.RuntimeData
import com.qm.lib.widget.statusview.Gloading
import com.qm.lib.widget.statusview.GlobalAdapter
import com.qy.qysdk.bean.QyAdConfig
import com.qy.qysdk.manager.QYManager
import com.tencent.mmkv.MMKV
import com.tencent.smtt.sdk.QbSdk
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig
import com.umeng.umcrash.UMCrash
import io.reactivex.disposables.Disposable
import io.reactivex.plugins.RxJavaPlugins

/**
 * @ClassName QMApplication
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/22 10:40 AM
 * @Version 1.0
 */
class QMApplication : BaseApplication() {

    private var assetManager: AssetManager? = null
    private var newResource: Resources? = null

    private fun getMetaDatByName(name: String): String {
        return JYUtils.instance.getMetaDatByName(this, name)
    }

    override fun onCreate() {
        super.onCreate()
        ininX5Web()
        initWebView()
        MMKV.initialize(this)
        // 获取设备ID
        getDeviceId()
        //是否开启打印日志
        DLog.init(!BuildConfig.online)

        RuntimeData.getInstance().httpUrl = getMetaDatByName("BASE_SERVER_URL")
        RuntimeData.getInstance().webHost = getMetaDatByName("BASE_WEB_URL")
        RuntimeData.getInstance().applicationId = BuildConfig.APPLICATION_ID
        RuntimeData.getInstance().wxAppId = getMetaDatByName("WX_APP_ID")
        if (!BuildConfig.online) {
            UMConfigure.init(this, null, null, UMConfigure.DEVICE_TYPE_PHONE, "")
            UMConfigure.setLogEnabled(!BuildConfig.online)
        }
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL)

        //初始化组件(靠前)
        ModuleLifecycleConfig.getInstance().initModuleAhead(this)

        Gloading.debug(BuildConfig.DEBUG)
        Gloading.initDefault(GlobalAdapter())
        // 友盟
        PlatformConfig.setWeixin(
            getMetaDatByName("WX_APP_ID"),
            getMetaDatByName("WX_APP_SECRIT")
        )
        PlatformConfig.setWXFileProvider("${BuildConfig.APPLICATION_ID}.fileProvider")
        //初始化组件(靠后)
        ModuleLifecycleConfig.getInstance().initModuleLow(this)

        //jpush_init_start
        // initPush()
        //jpush_init_end
        // 阿里EMAS
        // initEmas()

        // initPushDevice()

        initCrash()

        RxJavaPlugins.setErrorHandler {
            //异常处理
        }

        initGG()
    }

    private fun initGG() {
        QYManager.getInstance().setLogEnable(true)
        Log.d("DemoApplication", "onCreate")
        QYManager.getInstance()
            .init(
                this,
                QyAdConfig(getMetaDatByName("AD_APPID"),
                    getMetaDatByName("CSJ_APPID"),
                    getMetaDatByName("GDT_APPID"),
                    getMetaDatByName("KS_APPID"),
                    "")
            )

        QYManager.getInstance().setWxAppId(getMetaDatByName("WX_APP_ID"))
    }

    private fun initCrash() {
        UMCrash.registerUMCrashCallback { getCrashUserJson() }
    }

    private fun getCrashUserJson(): String {
        if (LocalUserManager.instance.getUser() != null) {
            return GsonUtils.objectToJsonStr(LocalUserManager.instance.getUser())
        }
        return ""
    }

    private fun initPushDevice() {
//        var registrationID = JPushInterface.getRegistrationID(this)
//        DLog.d("AAAAAA", "initPushDevice = $registrationID")
//        sendPushToken(registrationID)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        initWebView()
    }

    fun getAssetManager(): AssetManager? {
        return assetManager ?: super.getAssets()
    }

    override fun getResources(): Resources? {
        return newResource ?: super.getResources()
    }

    private fun sendPushToken(pushId: String) {
        if (StringUtils.isEmpty(JYMMKVManager.instance.getToken())) {
            RuntimeData.getInstance().settPushToken(pushId)
            RxBus.getDefault().postSticky(CSendPushTokenMessage())
            return
        }
        val bean: BaseBean<*> = BaseBean<Any?>(CUserInfoBean(tpushId = pushId))
        val requestBody = PostJsonBody.create(Gson().toJson(bean))

        val service = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            service.sendSaveUserInfo(requestBody),
            object : ObserverNoDialog<BaseResultBean<Any>>() {
                override fun onSuccess(picCodeBean: BaseResultBean<Any>) {

                }
            })
    }

    open class ObserverNoDialog<T> : BaseObserver<T>() {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
        }

        override fun onError(throwable: Throwable) {
            super.onError(throwable)
        }

        override fun onComplete() {
            super.onComplete()
        }

        override fun onSuccess(o: T) {}
        override fun onFailed(t: T) {
            super.onFailed(t)
        }

        override fun onNext(t: T) {
            super.onNext(t)
        }
    }

    private fun getDeviceId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            var mDeviceInfo = IMEIUtil.getIMEI(this)
            JYMMKVManager.instance.setDeviceId(mDeviceInfo)
        } else {
            try {
                OAIDHelper { isSupport, oaid, aaid, vaid ->
                    if (isSupport) {
                        JYMMKVManager.instance.setDeviceId(oaid)
                    }
                }.getDeviceIds(this)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun ininX5Web() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                DLog.d("app", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)
    }

    private fun initWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val PROCESSNAME = BuildConfig.APPLICATION_ID
            var processName = getProcessName(this)
            if (PROCESSNAME != processName) {
                WebView.setDataDirectorySuffix(getString(processName, "qm"));
            }
        }
    }

    private fun getProcessName(context: Context?): String? {
        if (context == null) return null
        val manager: ActivityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid === Process.myPid()) {
                return processInfo.processName
            }
        }
        return null
    }

    private fun getString(s: String?, defValue: String?): String? {
        return if (TextUtils.isEmpty(s)) defValue else s
    }

}