package com.gos.nodetransfer.ui

import android.app.Application
import androidx.databinding.ObservableBoolean
import com.dim.library.bus.RxBus
import com.dim.library.bus.RxSubscriptions
import com.dim.library.utils.DLog
import com.dim.library.utils.StringUtils
import com.dim.library.utils.ToastUtils
import com.gos.nodetransfer.entity.CGetDownloadBean
import com.gos.nodetransfer.entity.CSendPushTokenMessage
import com.gos.nodetransfer.entity.MAdvertisingDayNum
import com.gos.nodetransfer.entity.MGetDownloadBean
import com.gos.nodetransfer.http.MainService
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.BaseBean
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.*
import com.qm.lib.http.BaseService
import com.qm.lib.http.RetrofitClient
import com.qm.lib.message.RefreshUserByServer
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.RuntimeData
import io.reactivex.disposables.Disposable

/**
 * @ClassName MainViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 5:47 PM
 * @Version 1.0
 */
class MainViewModel(application: Application) : BaseAppViewModel(application) {

    var doesCanLookAdv: ObservableBoolean = ObservableBoolean(true)

    override fun onCreate() {
        super.onCreate()
        initEvent()
    }

    private fun doVersionCheck() {
        val api: BaseService = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            api.sendVersionCheck(),
            object : AppObserver<BaseResultBean<MVersionBean>>() {
                override fun onSuccess(o: BaseResultBean<MVersionBean>) {
                    super.onSuccess(o)
                    if (o.doesSuccess()) {
                        if (o.data.update) {
                            RouterManager.instance.gotoVersionUpdateActivity(
                                o.data.version,
                                o.data.url,
                                o.data.comment,
                                o.data.forceUpdate
                            )
                        }
                    }
                }
            })
    }

    private var getServerLink: Disposable? = null
    private var sendSavePushId: Disposable? = null
    private var refreshUser: Disposable? = null

    private fun initEvent() {
        getServerLink = RxBus.getDefault().toObservable(CGetUrlForServerBean::class.java)
            .subscribe {
                DLog.d("获取服务端接口")
                getUrlForServer(
                    it.url,
                    it.linkUrl,
                    it.needColse,
                    it.pageTag,
                    it.hasReload,
                    it.lastPageTag
                )
            }
        RxSubscriptions.add(getServerLink)

        sendSavePushId = RxBus.getDefault().toObservableSticky(CSendPushTokenMessage::class.java)
            .subscribe {
                DLog.d("设置pushID")
                sendPushToken()
            }
        RxSubscriptions.add(sendSavePushId)

        refreshUser = RxBus.getDefault().toObservable(RefreshUserByServer::class.java)
            .subscribe {
                DLog.d("刷新用户信息")
                getUserInfo()
            }
        RxSubscriptions.add(refreshUser)
    }

    private fun sendPushToken() {
        if (StringUtils.isEmpty(RuntimeData.getInstance().gettPushToken())) {
            return
        }
        val service = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance()
            .execute(service.sendSaveUserInfo(
                getRequestBody(
                    BaseBean(
                        CUserInfoBean(
                            tpushId = RuntimeData.getInstance().gettPushToken()
                        )
                    )
                )
            ),
                object : AppObserverNoDialog<BaseResultBean<Any>>() {
                    override fun onSuccess(o: BaseResultBean<Any>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            removePushEvent()
                        }
                    }
                })
    }

    private fun removeEnent() {
        if (getServerLink != null) {
            RxSubscriptions.remove(getServerLink)
            getServerLink = null
        }
        removePushEvent()
    }

    private fun removePushEvent() {
        RxBus.getDefault().removeStickyEvent(CSendPushTokenMessage::class.java)
        if (sendSavePushId != null) {
            RxSubscriptions.remove(sendSavePushId)
            sendSavePushId = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeEnent()
    }

    private fun getUrlForServer(
        url: String, linkUrl: String,
        needColse: Boolean,
        pageTag: String,
        hasReload: Boolean,
        lastPageTag: String
    ) {
        val api: BaseService = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            api.getUrlByServer(
                RuntimeData.getInstance().httpUrl + url,
                getRequestBody(BaseBean<MGetUrlForServerBean>())
            ),
            object : AppObserver<BaseResultBean<MGetUrlForServerBean>>() {
                override fun onSuccess(o: BaseResultBean<MGetUrlForServerBean>) {
                    super.onSuccess(o)
                    if (o.doesSuccess()) {
                        if (o.data.url == null || o.data.url.isEmpty()) {
                            if (linkUrl != null && linkUrl.isNotEmpty()) {
                                RouterManager.instance.gotoWebviewActivity(linkUrl)
                            }
                        } else {
                            RouterManager.instance.gotoWebviewActivity(o.data.url)
                        }
                        if (o.data.toast != null && !StringUtils.isEmpty(o.data.toast)) {
                            ToastUtils.showShort(o.data.toast)
                        }
                    } else {
                        ToastUtils.showShort(o.errMsg)
                    }

                    if (needColse) {
                        if (hasReload) {
                            // 关闭当前页面并刷新上一个页面
                            RxBus.getDefault().post(MClosePageBean(pageTag))
                            RxBus.getDefault().post(MReloadPageBean(lastPageTag))
                        } else {
                            // 关闭当前页面
                            RxBus.getDefault().post(MClosePageBean(pageTag))
                        }
                    } else {
                        // 刷新当前页面
                        if (hasReload) {
                            RxBus.getDefault().post(MReloadPageBean(pageTag))
                        }
                    }
                }
            })
    }

    fun getAdvertisingDayNum() {
//        val service = RetrofitClient.getInstance().create(MainService::class.java)
//
//        RetrofitClient.getInstance()
//            .execute(service.getAdvCount(getRequestBody(BaseBean<Any>())),
//                object : AppObserverNoDialog<BaseResultBean<MAdvertisingDayNum>>() {
//                    override fun onSuccess(o: BaseResultBean<MAdvertisingDayNum>) {
//                        super.onSuccess(o)
//                        if (o.doesSuccess()) {
//                            doesCanLookAdv.set(o.data.viewNum < o.data.maxNumber)
//                        }
//                    }
//                })
    }

    fun saveAdvertisingDayNum() {
        val service = RetrofitClient.getInstance().create(MainService::class.java)

        RetrofitClient.getInstance()
            .execute(service.sendSaveAdvView(getRequestBody(BaseBean<Any>())),
                object : AppObserverNoDialog<BaseResultBean<MAdvertisingDayNum>>() {
                    override fun onSuccess(o: BaseResultBean<MAdvertisingDayNum>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            doesCanLookAdv.set(o.data.viewNum < o.data.maxNumber)
                            RxBus.getDefault().post(MRefreshWeb())
                        }
                    }
                })
    }


    fun getDownloadInfoByPackage(packageName: String) {
        val service = RetrofitClient.getInstance().create(MainService::class.java)

        RetrofitClient.getInstance()
            .execute(service.getAppDownload(getRequestBody(BaseBean(CGetDownloadBean(packgeName = packageName)))),
                object : AppObserverNoDialog<BaseResultBean<MGetDownloadBean>>() {
                    override fun onSuccess(o: BaseResultBean<MGetDownloadBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            RouterManager.instance.gotoDownloadActivity(
                                o.data.icon,
                                o.data.downloadUrl,
                                o.data.description
                            )
                        }
                    }
                })
    }
}