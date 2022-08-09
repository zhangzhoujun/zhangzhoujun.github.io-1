package com.gos.nodetransfer.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.base.AppManager
import com.dim.library.bus.RxBus
import com.dim.library.bus.RxSubscriptions
import com.dim.library.utils.DLog
import com.dim.library.utils.GsonUtils
import com.dim.library.utils.StringUtils
import com.dim.library.utils.ToastUtils
import com.gos.nodetransfer.BR
import com.gos.nodetransfer.R
import com.gos.nodetransfer.databinding.MainActMianBinding
import com.gos.nodetransfer.oaid.IMEIUtil
import com.gos.nodetransfer.oaid.OAIDHelper
import com.gos.nodetransfer.widget.JYNormalItemView
import com.gos.nodetransfer.widget.QmNormalItemView
import com.qm.lib.base.JYActivity
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.CVideoLookResoultBean
import com.qm.lib.entity.DownloadAppMessage
import com.qm.lib.entity.MAppConfigBean
import com.qm.lib.entity.ShowVideoEvent
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.*
import com.qy.qysdk.ad.QyAdListener
import com.qy.qysdk.manager.QYManager
import com.qy.qysdk.manager.QYWxConstants
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import me.majiajie.pagerbottomtabstrip.NavigationController
import me.majiajie.pagerbottomtabstrip.PageNavigationView
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener

/**
 * @ClassName MainActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 5:50 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Main.MAIN_INDEX)
class MainActivity : JYActivity<MainActMianBinding, MainViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.HOME_CHOSE_TAB)
    var choseKey: String = JYComConst.HOME_CHOSE_TAB_HOME

    private var mFragments: ArrayList<Fragment>? = null
    private var navigationController: NavigationController? = null

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.main_act_mian
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        choseKey = intent!!.extras!!.get(JYComConst.HOME_CHOSE_TAB) as String
        setChoseItem()
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)

        showFullScreen(true)

        syncUserForScene()
        initHomeUrlClickBroadcast()
    }

    private fun syncUserForScene() {
        var channelID = JYUtils.instance.getMetaDatByName(this, "HD_CHANNEL_ID")
        DLog.d("MainActivity", "syncUserForScene channelID -> $channelID")
        if (channelID == null || TextUtils.isEmpty(channelID)) {
            channelID = "20020"
        }
//        if (
//            QYManager.getInstance().login(LocalUserManager.instance.getUserId())) {
//            QYManager.getInstance().login("8")) {
        val appList = getBottomConfig()
        //初始化Fragment
        initFragment(appList)
        //初始化底部Button
        initBottomTab(appList)
        AppManager.getAppManager().finishAllOtherActivity()

        initThirdSDK()
        initEvent()

        getVm().getAdvertisingDayNum()

        registerMessageReceiver()

        syncUserAccountForThird()
//        }
    }

    private fun doDownloadApp(packageName: String) {
        if (SystemUtil.isAppInstalled(this@MainActivity, packageName)) {
            // open
            SystemUtil.openApp(this@MainActivity, packageName)
        } else {
            // download
            getDownloadAppInfoByServer(packageName)
        }
    }

    private fun getDownloadAppInfoByServer(packageName: String) {
        getVm().getDownloadInfoByPackage(packageName)
    }

    private fun syncUserAccountForThird() {
//        var registrationID = JPushInterface.getRegistrationID(this)
//        DLog.d("AAAAAA", "initPushDevice = $registrationID")
//
//        RuntimeData.getInstance().settPushToken(registrationID)
//        RxBus.getDefault().postSticky(CSendPushTokenMessage())
    }

    private fun getVm(): MainViewModel {
        return viewModel as MainViewModel
    }

    private fun getBind(): MainActMianBinding {
        return binding as MainActMianBinding
    }

    private fun initFragment(appList: ArrayList<MAppConfigBean.MAppConfigitem>?) {
        mFragments = ArrayList()
        if (appList != null && appList!!.isNotEmpty()) {
            for (index in 0 until appList.size) {
//                if (index == 0) {
//                    mFragments!!.add(QYManager.getInstance().homeFragment)
//                } else {
                val item = appList[index]
                if (item.page_path.isNotEmpty() && item.icon.isNotEmpty() && item.activation_icon.isNotEmpty()) {
                    val postcard =
                        RouterManager.instance.getNativeFragmentPostcard(item.page_path)
                    if (postcard != null) {
                        postcard.withSerializable(JYComConst.OPEN_CUSTOM_ITEM, item)
                            .withString("tabTextAlign", item.text_align)
                            .withBoolean(JYComConst.OPEN_CUSTOM_NOBACK, true)
                        val fragment = postcard.navigation()
                        if (fragment != null) {
                            mFragments!!.add(fragment as Fragment)
                        }
                    } else {
                        val fragment =
                            ARouter.getInstance()
                                .build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_INDEX)
                                .withString(JYComConst.OPEN_CUSTOM_PAGE, item.page_path)
                                .withBoolean(JYComConst.OPEN_CUSTOM_NOBACK, true)
                                .navigation()
                        mFragments!!.add(fragment as Fragment)
                    }
                }
//                }
            }
        }
    }

    private fun getBottomConfig(): ArrayList<MAppConfigBean.MAppConfigitem>? {
        val appConfig = JYMMKVManager.instance.getAppConfigData()
        if (appConfig != null && !StringUtils.isEmpty(appConfig)) {
            val config = GsonUtils.fromJson(appConfig, MAppConfigBean::class.java)
            if (config?.tabs != null && config.tabs.isNotEmpty()) {
                return filterData(config)
            }
        }
        return null
    }

    private fun filterData(bean: MAppConfigBean): ArrayList<MAppConfigBean.MAppConfigitem> {
        for (index in bean.tabs.size - 1 downTo 0) {
            var item = bean.tabs[index]
            // 黑白名单 用户身份级别
            var mAppType = JYUtils.instance.getMetaDatByName(this, "QY_APP_NAME")
            var mUserType = LocalUserManager.instance.getUserIdentity()
            var inWhiteLevel = false
            var hasWhiteLevel = false
            var inBlackLevel = false

            if (item.white_level != null && item.white_level.isNotEmpty()) {
                val whiteLevel = item.white_level

                if (whiteLevel != null && whiteLevel.isNotEmpty()) {
                    hasWhiteLevel = true
                    for (index in whiteLevel.indices) {
                        if (whiteLevel[index] == mUserType) {
                            inWhiteLevel = true
                            break
                        }
                    }
                }
            }

            // 黑名单
            if (item.black_level != null && item.black_level.isNotEmpty()) {
                val blackLevel = item.black_level

                if (blackLevel != null && blackLevel.isNotEmpty()) {
                    for (index in blackLevel.indices) {
                        if (blackLevel[index] == mUserType) {
                            inBlackLevel = true
                            break
                        }
                    }
                }
            }

            // 白名单 黑名单 判断
            // 如果白名单为空 直接判断黑名单
            // 如果用户在白名单，在判断在不在黑名单
            // 如果 白名单不为空 且 用户不在白名单 不给看
            if (hasWhiteLevel) {
                if (inWhiteLevel) {
                    if (inBlackLevel) {
                        bean.tabs.removeAt(index)
                        continue
                    }
                } else {
                    bean.tabs.removeAt(index)
                    continue
                }
            } else {
                if (inBlackLevel) {
                    bean.tabs.removeAt(index)
                    continue
                }
            }

            // 黑白名单 用户级别
            var mUserMobile = LocalUserManager.instance.getUserMobile()
            var inWhiteUsers = false
            var hasWhiteUsers = false
            var inBlackUsers = false

            // 白名单
            var whiteUsers = item.white_users
            if (whiteUsers != null && !com.qm.lib.utils.StringUtils.instance.isEmpty(whiteUsers)) {
                hasWhiteUsers = true
                var idArrays = whiteUsers.split(",")
                for (pos in idArrays.indices) {
                    if (mUserMobile == idArrays[pos]) {
                        inWhiteUsers = true
                        break
                    }
                }
            }

            // 黑名单
            var blackUsers = item.black_users
            if (blackUsers != null && !com.qm.lib.utils.StringUtils.instance.isEmpty(blackUsers)) {
                var idArrays = blackUsers.split(",")
                for (pos in idArrays.indices) {
                    if (mUserMobile == idArrays[pos]) {
                        inBlackUsers = true
                        break
                    }
                }
            }
            // 白名单 黑名单 判断
            // 如果白名单为空 直接判断黑名单
            // 如果用户在白名单，在判断在不在黑名单
            // 如果 白名单不为空 且 用户不在白名单 不给看
            if (hasWhiteUsers) {
                if (inWhiteUsers) {
                    if (inBlackUsers) {
                        bean.tabs.removeAt(index)
                    }
                } else {
                    bean.tabs.removeAt(index)
                }
            } else {
                if (inBlackUsers) {
                    bean.tabs.removeAt(index)
                }
            }
        }
        return bean.tabs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        TopFlowSDK.getInstance().bindLifecycle(application)
    }

    private fun initBottomTab(appList: ArrayList<MAppConfigBean.MAppConfigitem>?) {
        val builder: PageNavigationView.CustomBuilder =
            getBind().pagerBottomTab.custom()

        if (appList != null && appList!!.isNotEmpty()) {
            for (index in 0 until appList.size) {
                val item = appList[index]
                if (item.page_path.isNotEmpty() && item.icon.isNotEmpty() && item.activation_icon.isNotEmpty()) {
                    builder.addItem(
                        newQmItem(item.icon, item.activation_icon)
                    )
                }
            }
            navigationController = builder.build()
            initBottomAfter()
            return
        }
    }

    private fun initBottomAfter() {
        //底部按钮的点击事件监听
        navigationController!!.addTabItemSelectedListener(object : OnTabItemSelectedListener {
            override fun onSelected(index: Int, old: Int) {
                DLog.d("index: $index,old : $old")
                val currentFragment = mFragments!![index]
                if (currentFragment != null) {
                    val transaction =
                        supportFragmentManager.beginTransaction()
                    if (currentFragment.isAdded) {
                        transaction.show(currentFragment)
                        currentFragment.onResume()
                    } else {
                        transaction.add(R.id.frameLayout, currentFragment)
                        transaction.show(currentFragment)
                        currentFragment.onResume()
                    }
                    mFragments!![old].onPause()
                    transaction.hide(mFragments!![old])
                    transaction.commitAllowingStateLoss()
                }
                if (index == 3) {
//                    RouterManager.instance.gotoQRActivity()
                }

                SLSLogUtils.instance.sendLogClick(
                    javaClass.simpleName,
                    "MAIN_ACTIVITY",
                    "BOTTOM_TAB",
                    posString = "$old-$index"
                )
            }

            override fun onRepeat(index: Int) {}
        })
        setChoseItemFirst()
    }

    private fun setChoseItem(pos: Int = -1) {
        var index = pos
        if (index == -1) {
            index = getPositionByKey()
        }
        if (pos == -1) {
            navigationController!!.setSelect(index)
        }
    }

    private fun getPositionByKey(): Int {
        when (choseKey) {
            JYComConst.HOME_CHOSE_TAB_HOME -> {
                return 0
            }
            JYComConst.HOME_CHOSE_TAB_KS -> {
                return 1
            }
            JYComConst.HOME_CHOSE_TAB_TOKENCENTER -> {
                return 2
            }
            JYComConst.HOME_CHOSE_TAB_SCHOOL -> {
                return 3
            }
            JYComConst.HOME_CHOSE_TAB_MINE -> {
                return 4
            }
            else -> return 0
        }
    }

    private fun setChoseItemFirst() {
        var choseTab = getPositionByKey()
        if (mFragments != null && mFragments!![choseTab] != null && choseTab == 0
        ) {
            //默认选中第一个
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.add(R.id.frameLayout, mFragments!![choseTab])
            transaction.commitAllowingStateLoss()
        } else {
            navigationController!!.setSelect(choseTab)
        }
    }

    //创建一个Item
    private fun newItem(
        drawable: Int,
        checkedDrawable: Int,
        text: String
    ): JYNormalItemView? {
        val normalItemView = JYNormalItemView(this)
        normalItemView.initialize(drawable, checkedDrawable, text)
        normalItemView.setTextDefaultColor(resources.getColor(R.color.c_999999))
        normalItemView.setTextCheckedColor(resources.getColor(R.color.c_333333))
        return normalItemView
    }

    private fun newQmItem(
        drawable: String,
        checkedDrawable: String
    ): QmNormalItemView? {
        val normalItemView = QmNormalItemView(this)
        normalItemView.initialize(drawable, checkedDrawable)
        return normalItemView
    }

    // 激励视屏 start
    private fun showJiliVideo(event: ShowVideoEvent) {
        if (!getVm().doesCanLookAdv.get()) {
            SLSLogUtils.instance.sendLogAdStatus(
                "LoadingActivity", "REWARD_VIDEO", "AD_MUM_MAX", -1, ""
            )
            return
        }
        if (event.isShowDialog) {
            getVm().showDialog()
        }
        showJiliJuhe(event)
    }

    private var isSendVideoResoult = false

    private fun showJiliJuhe(event: ShowVideoEvent) {
        DLog.d("VIDEOVIDEOVIDEOVIDEOVIDEO", "showJiliJuhe -> ")
        SLSLogUtils.instance.sendLogAdStatus(
            "LoadingActivity", "REWARD_VIDEO", "AD_START_READY", -1, ""
        )

        isSendVideoResoult = false

        QYManager.getInstance()
            .showRewardAd(this, "DEFAULT_DIALOG", object : QyAdListener.QyAdRewardvideoListener {
                override fun onAdShow() {

                }

                override fun onAdClose() {
                    Handler().postDelayed(Runnable {
                        RxBus.getDefault().post(CVideoLookResoultBean(isSendVideoResoult))
                        isSendVideoResoult = false
                    }, 500)
                }

                override fun onVideoComplete() {

                }

                override fun onVideoError() {

                }

                override fun onRewardVerify(
                    p0: Boolean,
                    p1: Int,
                    p2: String?,
                    p3: Int,
                    p4: String?
                ) {
                    isSendVideoResoult = true
                }

                override fun onSkippedVideo() {

                }

            })

    }

    // 激励视屏 end
    private var downloadAppMessaage: Disposable? = null
    private var showVideo: Disposable? = null

    private fun initEvent() {
        downloadAppMessaage = RxBus.getDefault().toObservable(DownloadAppMessage::class.java)
            .subscribe { message ->
                DLog.d("AAAAAAAA", "start download app")
                doDownloadApp(message.packageName)
            }
        RxSubscriptions.add(downloadAppMessaage)

        showVideo = RxBus.getDefault().toObservable(ShowVideoEvent::class.java)
            .subscribe(Consumer<ShowVideoEvent> { dialogPop ->
                showJiliVideo(dialogPop)
            })
        RxSubscriptions.add(showVideo)
    }

    private fun removeEnent() {
        if (downloadAppMessaage != null) {
            RxSubscriptions.remove(downloadAppMessaage)
            downloadAppMessaage = null
        }
        if (showVideo != null) {
            RxSubscriptions.remove(showVideo)
            showVideo = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeEnent()
//        if (mMessageReceiver != null) {
//            LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver)
//        }

        if (locationBroadcast != null) {
            unregisterReceiver(locationBroadcast)
        }
    }

    private fun initThirdSDK() {
        var deviceId = JYMMKVManager.instance.getDeviceId()
        if (deviceId == null || StringUtils.isEmpty(deviceId)) {
            getDeviceId()
        } else {
            setThirdSDK(deviceId)
        }
    }

    private fun getMetaDatByName(name: String): String {
        return JYUtils.instance.getMetaDatByName(this, name)
    }

    private fun setThirdSDK(deviceId: String) {

    }

    private fun getDeviceId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            var mDeviceInfo = IMEIUtil.getIMEI(application)
            JYMMKVManager.instance.setDeviceId(mDeviceInfo)
            setThirdSDK(mDeviceInfo)
        } else {
            try {
                OAIDHelper { isSupport, oaid, aaid, vaid ->
                    if (isSupport) {
                        JYMMKVManager.instance.setDeviceId(oaid)
                        setThirdSDK(oaid)
                    }
                }.getDeviceIds(this)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    // 两次返回退出登录
    private var exitTime: Long = 0

    override fun onBackPressed() {
        exit()
    }

    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtils.showShort("再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            AppManager.getAppManager().AppExit()
        }
    }

    // push start
    // private var mMessageReceiver: MessageReceiver? = null

    companion object Main {
        val MESSAGE_RECEIVED_ACTION = "com.gos.nodetransfer.jpush.MESSAGE_RECEIVED_ACTION"
        val KEY_TITLE = "title"
        val KEY_MESSAGE = "message"
        val KEY_EXTRAS = "extras"
    }

    fun registerMessageReceiver() {
//        mMessageReceiver = MessageReceiver()
//        val filter = IntentFilter()
//        filter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
//        filter.addAction(MESSAGE_RECEIVED_ACTION)
//        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter)
    }

    class MessageReceiver : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
//            try {
//                if (MESSAGE_RECEIVED_ACTION == intent.action) {
//                    val messge = intent.getStringExtra(KEY_MESSAGE)
//                    val extras = intent.getStringExtra(KEY_EXTRAS)
//                    val showMsg = StringBuilder()
//                    showMsg.append("$KEY_MESSAGE : $messge\n")
//                    if (!ExampleUtil.isEmpty(extras)) {
//                        showMsg.append("$KEY_EXTRAS : $extras\n")
//                    }
//                    DLog.d("AAAA", "MessageReceiver -> $showMsg")
//                }
//            } catch (e: java.lang.Exception) {
//            }
        }
    }
    // push end

    private var locationBroadcast: LocationBroadcast? = null

    private fun initHomeUrlClickBroadcast() {
        locationBroadcast = LocationBroadcast()
        val intentFilter = IntentFilter()
        intentFilter.addAction(QYWxConstants.QY_SDK_OPEN_URL_BROADCAST)
        registerReceiver(locationBroadcast, intentFilter)
    }

    //广播接收者
    class LocationBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //收到广播后的操作
            val url = intent.getStringExtra("url")
            DLog.d("LocationBroadcast", "接受首页的点击 url = $url")
        }
    }

}