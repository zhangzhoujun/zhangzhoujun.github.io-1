package com.gos.nodetransfer.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.FragmentActivity
import com.dim.library.base.AppManager
import com.dim.library.bus.RxBus
import com.dim.library.bus.RxSubscriptions
import com.dim.library.utils.DLog
import com.dim.library.utils.RxUtils
import com.dim.library.utils.ToastUtils
import com.dim.library.utils.Utils
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.LocalUserManager.Companion.instance
import com.qm.lib.entity.*
import com.qm.lib.http.BaseService
import com.qm.lib.http.RetrofitClient
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.*
import com.qy.qysdk.ad.QyAdListener
import com.qy.qysdk.manager.QYManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.umeng.analytics.MobclickAgent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.util.concurrent.TimeUnit

/**
 * @ClassName LoadingViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 4:53 PM
 * @Version 1.0
 */
class LoadingViewModel : BaseAppViewModel {

    var isShowDialog: ObservableBoolean = ObservableBoolean(false)

    private var mActivity: Activity? = null
    private var mRoot: ViewGroup? = null
    private var mAdFra: FrameLayout? = null

    constructor(application: Application) : super(application)

    override fun onCreate() {
        super.onCreate()
        initEvent()
    }

    fun setActivity(
        activity: Activity,
        root: ViewGroup,
        mAdFra: FrameLayout
    ) {
        if (this.mRoot != null) {
            return
        }
        this.mActivity = activity
        this.mRoot = root
        this.mAdFra = mAdFra

        if (lacksPermissions(getApplication(), permissions)) {
            initPermissions()
        } else {
            doVersionCheck()
        }
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
                        } else {
                            getAppConfig()
                        }
                    }
                }

                override fun onError(throwable: Throwable) {
                    super.onError(throwable)
                    getAppConfig()
                }
            })
    }

    private var versionEvent: Disposable? = null

    private fun initEvent() {
        versionEvent = RxBus.getDefault().toObservableSticky(VersionCloseEvent::class.java)
            .subscribe {
                DLog.d("versionEvent")
                getAppConfig()
            }
        RxSubscriptions.add(versionEvent)
    }

    private fun removeEnent() {
        if (versionEvent != null) {
            RxSubscriptions.remove(versionEvent)
            versionEvent = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeEnent()
    }

    fun onCancelClick(view: View) {
        finish()
    }

    fun onSureClick(view: View) {
        JYMMKVManager.instance.setShowLoadingDialog(false)
        isShowDialog.set(false)
        startToMain()
    }

    private fun startToMain() {
        gotoMainPage()
    }

    /**
     * 读写权限  自己可以添加需要判断的权限
     */
    private var permissions = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限  false-表示权限已开启
     */
    private fun lacksPermissions(
        mContexts: Context,
        permissions: Array<String>
    ): Boolean {
        for (permission in permissions) {
            if (lacksPermission(mContexts, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否缺少权限
     */
    private fun lacksPermission(
        mContexts: Context,
        permission: String
    ): Boolean {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
            PackageManager.PERMISSION_DENIED
    }

    private fun initPermissions() {
        // RxPermissions 0.10.2 有个fragment的问题 会导致crash，先 延迟 + 捕获处理下看看情况
        try {
            val rxPermissions =
                RxPermissions((AppManager.getActivityStack()[0] as FragmentActivity))
            Observable.just("")
                .delay(200, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(lifecycleProvider))
                .compose(RxUtils.schedulersTransformer())
                .subscribe {
                    rxPermissions.request(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                        .debounce(1, TimeUnit.SECONDS)
                        .subscribe { aBoolean: Boolean? ->
                            if (!aBoolean!!) {
                                ToastUtils.showShort("请同意权限")
                                JYAppManager.getAppManager().AppExit()
                            } else {
                                doVersionCheck()
                            }
                        }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            gotoMainPage()
        }
    }

    override fun onGetAppConfig(bean: MAppConfigBean?) {
        if (JYMMKVManager.instance.isShowLoadingDialog()) {
            isShowDialog.set(true)
        } else {
            startToMain()
        }
    }

    private fun gotoMainPage() {
        // 兼容结束
        if (JYMMKVManager.instance.isAutoLogin() && JYMMKVManager.instance.getToken()
                .isNotEmpty()
        ) {
            getUserInfo()
        }
        if (JYMMKVManager.instance.getIsOpenScreen()) {
            DLog.d("加载开屏开屏")

            SLSLogUtils.instance.sendLogSync(
                "LoadingActivity",
                "",
                "START_LOAD_OPEN_SCREEN"
            )
            initGG()
        } else {
            openMainActivity(3)
        }
    }

    override fun onGetUserInfoBack(userBean: MUserBean?) {
        super.onGetUserInfoBack(userBean)
        DLog.d("USERINFO", "onGetUserInfoBack")

        MobclickAgent.onProfileSignIn(instance.getUserId())
    }

    private fun initGG() {
        mAdFra!!.visibility = View.VISIBLE

        QYManager.getInstance().showSplashAd(mActivity, mAdFra, object :
            QyAdListener.QyAdSplashListener {
            override fun onAdClicked(p0: View?, p1: Int) {

            }

            override fun onAdShow(p0: View?, p1: Int) {

            }

            override fun onAdSkip() {

            }

            override fun onAdTimeOver() {

            }

            override fun onAdClose() {
                openMainActivity(0)
            }

            override fun onTimeout() {

            }

            override fun onError(p0: Int, p1: String?) {
                openMainActivity(0)
            }

        })
    }

    private fun openMainActivity(secondTime: Long) {
        Observable.just("")
            .delay(secondTime, TimeUnit.SECONDS)
            .compose(RxUtils.bindToLifecycle(lifecycleProvider))
            .compose(RxUtils.schedulersTransformer())
            .subscribe {
                if (JYUtils.instance.getMetaDatByName(Utils.getContext(), "show_guide") == "1" &&
                    JYMMKVManager.instance.isFirstForVersion(SystemUtil.getVersionName(Utils.getContext()))
                ) {
                    SLSLogUtils.instance.sendLogSync(
                        "LoadingActivity",
                        "",
                        "START_TO_SHOW_GUIDE"
                    )
                    RouterManager.instance.gotoMainGuideActivity()
                } else if (RuntimeData.getInstance().mustLogin && (!JYMMKVManager.instance.isAutoLogin() || JYMMKVManager.instance.getToken()
                        .isEmpty())
                ) {
                    SLSLogUtils.instance.sendLogSync(
                        "LoadingActivity",
                        "",
                        "START_TO_LOGIN"
                    )
                    RouterManager.instance.gotoLoginActivity()
                } else {
                    SLSLogUtils.instance.sendLogSync(
                        "LoadingActivity",
                        "",
                        "START_TO_MAIN"
                    )
                    RouterManager.instance.gotoMainActivity(JYComConst.HOME_CHOSE_TAB_HOME)
                }
                finish()
            }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }
}