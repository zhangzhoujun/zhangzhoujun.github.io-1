package com.qm.module_juggle.ui

import android.app.Activity
import android.app.Application
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.dim.library.bus.RxBus
import com.dim.library.bus.RxSubscriptions
import com.dim.library.utils.DLog
import com.dim.library.utils.GsonUtils
import com.dim.library.utils.ToastUtils
import com.google.gson.JsonObject
import com.qm.lib.base.BaseBean
import com.qm.lib.base.ListViewModel
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.entity.MClosePageBean
import com.qm.lib.entity.MGoodsBean
import com.qm.lib.entity.MReloadPageBean
import com.qm.lib.entity.MUserBean
import com.qm.lib.http.RetrofitClient
import com.qm.lib.message.RefreshUserBtAmount
import com.qm.lib.message.RefreshUserInfo
import com.qm.lib.message.SubmitFromBean
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.ColorUtils
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.JYMMKVManager
import com.qm.lib.utils.JYUtils
import com.qm.lib.utils.MD5Utils
import com.qm.lib.utils.PlayVoiceUtil
import com.qm.lib.utils.RuntimeData
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.utils.StringUtils
import com.qm.lib.utils.SystemUtil
import com.qm.lib.utils.TimeUtils
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.module_juggle.R
import com.qm.module_juggle.entity.CGetGoodsBean
import com.qm.module_juggle.entity.CLoginBeanBean
import com.qm.module_juggle.entity.MHomeBtList
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.entity.MShowDialogServerBean
import com.qm.module_juggle.http.HomeService
import com.qm.module_juggle.ui.adapter.HomeBannerAdapter
import com.qm.module_juggle.ui.adapter.HomeBannerGalleryAdapter
import com.qm.module_juggle.ui.adapter.HomeEquitiesPoolAdapter
import com.qm.module_juggle.ui.adapter.HomeGoodsAdapter
import com.qm.module_juggle.ui.adapter.HomeHScrollAdapter
import com.qm.module_juggle.ui.adapter.HomeLDAdapter
import com.qm.module_juggle.ui.adapter.HomeLDVAdapter
import com.qm.module_juggle.ui.adapter.HomeLineAdapter
import com.qm.module_juggle.ui.adapter.HomeLinePercentAdapter
import com.qm.module_juggle.ui.adapter.HomeMineAdapter
import com.qm.module_juggle.ui.adapter.HomeStickLineAdapter
import com.qm.module_juggle.ui.adapter.HomeTongzhengAdapter
import com.qm.module_juggle.ui.adapter.HomeZichanAdapter
import com.qm.module_juggle.ui.adapter.HomedengfenAdapter
import com.qm.module_juggle.ui.adapter.JuggleDiyComponentsAdapter
import com.qm.module_juggle.ui.adapter.JuggleDiyComponentsListAdapter
import com.qm.module_juggle.ui.adapter.JuggleGoodsAdapter
import com.qm.module_juggle.ui.adapter.JuggleGoodsGridAdapter
import com.qm.module_juggle.ui.adapter.JuggleLoginAdapter
import com.qm.module_juggle.ui.adapter.JuggleShellAdapter
import com.qm.module_juggle.utils.JugglePathUtils
import com.qm.module_juggle.widget.pool.EquitiesPollItem
import com.qm.module_juggle.widget.pool.EquitiesPoolView
import com.qm.module_juggle.widget.pool.HomeEquitiesPoolBean
import io.reactivex.disposables.Disposable

/**
 * @ClassName HomeViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 7:26 PM
 * @Version 1.0
 */
class JuggleFraViewModel(application: Application) : ListViewModel(application) {

    private var mPageTag = ""

    var mTitleInfo: ObservableField<MHomeDataBean.MHomeDataPage> = ObservableField()

    var scrollToBottom = false

    var stickBottomPos = -1

    private val TAG = "HomeViewModel"

    private lateinit var recyclerView: RecyclerView
    lateinit var context: Activity
    private lateinit var mFragment: JuggleFragment
    private lateinit var delegateAdapter: DelegateAdapter
    private var mTopItem: MHomeDataBean.MHomeDataItem? = null

    private var page_key = ""
    private var lastPageTag = ""

    private var page_data: MHomeDataBean? = null

    private var page_no_back = false

    private var mAdapterAssert = ArrayList<HomeZichanAdapter>()

    var liandongVAdapter: HomeLDVAdapter? = null

    // 刷新的时候，只有配置的数据发生改变 才会刷新UI
    private var mHomeDadaMD5 = ""

    // 弹窗相关逻辑
    private var dialogs: ArrayList<MHomeDataBean.MHomeDataPageDialog>? = null
    private var mCurrenrDialogIndex = 0
    private var mDiyNum = 0
    private var mShellNum = 0
    private var mStartDialog = false

    // 登录表单
    private var loginFromAdapter: JuggleLoginAdapter? = null

    private var diyListMap: HashMap<String, JuggleDiyComponentsListAdapter>? = null

    fun initTitle() {
        if (mTitleInfo.get() == null) {
            return
        }
        val option = JYToolbarOptions()
        option.titleString = mTitleInfo.get()!!.title
        option.isNeedNavigate = !page_no_back
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    fun setGroupScrollToBottom(bottom: Boolean) {
        scrollToBottom = bottom
        if (liandongVAdapter != null) {
            liandongVAdapter!!.setGroupScrollToBottom(scrollToBottom)
        }
        if (scrollToBottom) {
            if (!StringUtils.instance.isEmpty(mGoodsUrlLink)) {
                onLoadMore()
            }
        }
    }

    fun scrollToBottom() {
        if (!StringUtils.instance.isEmpty(mGoodsUrlLink)) {
            onLoadMore()
        }
    }

    fun isHasLDV(): Boolean {
        return liandongVAdapter != null
    }

    override fun onCreate() {
        super.onCreate()
        mTotalPage = 1
    }

    fun initHome(
        recyclerView: RecyclerView,
        fragment: JuggleFragment,
        context: Activity,
        page_key: String,
        page_no_back: Boolean,
        page_data: MHomeDataBean?,
        lastPageTag: String
    ) {
        this.recyclerView = recyclerView
        this.mFragment = fragment
        this.context = context
        this.page_data = page_data
        this.lastPageTag = lastPageTag
        this.page_no_back = page_no_back

        mPageTag = "${System.currentTimeMillis()}_${((Math.random() * 9 + 1) * (1000))}"

        if (page_key.contains("=")) {
            var keyList = page_key.split("=")
            this.page_key = keyList[keyList.size - 1]
        } else {
            this.page_key = page_key
        }

        initRecyclerView()
        initEvent()
        getHomeData()
    }

    private fun getHomeData() {
        mCurrenrDialogIndex = 0
        mDiyNum = 0
        pageLinkerServerMap.clear()
        if (page_data == null) {
            val api: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

            RetrofitClient.getInstance()
                .execute(api.getPageConfig(page_key),
                    object : AppObserver<BaseResultBean<MHomeDataBean>>() {
                        override fun onSuccess(o: BaseResultBean<MHomeDataBean>) {
                            super.onSuccess(o)
                            if (o.doesSuccess()) {
                                mCurrentPage = 1
                                initDataBack(o.data)
                            }
                            onLoadFinish()
                        }

                        override fun onError(throwable: Throwable) {
                            super.onError(throwable)
                            onLoadFinish()
                        }
                    })
        } else {
            mCurrentPage = 1
            initDataBack(page_data!!)
        }
    }

    private fun getDiyListByServer(method: String, dataUrl: String, key: String) {
        DLog.d(TAG, "循环组件 获取数据 key = -> $key")
        val api: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

        if (method == "post") {
            RetrofitClient.getInstance()
                .execute(api.postDiyInfo("/api$dataUrl"),
                    object : AppObserver<BaseResultBean<ArrayList<JsonObject>>>() {
                        override fun onSuccess(o: BaseResultBean<ArrayList<JsonObject>>) {
                            super.onSuccess(o)
                            if (o.doesSuccess()) {
                                onDiyGetInfoBack(key, o.data)
                            }
                        }

                        override fun onError(throwable: Throwable) {
                            super.onError(throwable)
                        }
                    })
        } else {
            RetrofitClient.getInstance()
                .execute(api.getDiyInfo("/api$dataUrl"),
                    object : AppObserver<BaseResultBean<ArrayList<JsonObject>>>() {
                        override fun onSuccess(o: BaseResultBean<ArrayList<JsonObject>>) {
                            super.onSuccess(o)
                            if (o.doesSuccess()) {
                                onDiyGetInfoBack(key, o.data)
                            }
                        }

                        override fun onError(throwable: Throwable) {
                            super.onError(throwable)
                        }
                    })
        }
    }

    private fun onDiyGetInfoBack(key: String, list: ArrayList<JsonObject>) {
        if (diyListMap != null && diyListMap!!.containsKey(key)) {
            diyListMap!![key]!!.setPageData(key, list)

            diyListMap!![key]!!.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        // 列表
        var layoutManager = VirtualLayoutManager(context!!);
        recyclerView.layoutManager = layoutManager

        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        val viewPool = RecyclerView.RecycledViewPool()
        viewPool.setMaxRecycledViews(0, 0)
        recyclerView.setRecycledViewPool(viewPool)

        delegateAdapter = DelegateAdapter(layoutManager, false)

    }

    private fun isNeedDeleteUsers(
        mAppType: String,
        mUserType: String,
        bean: MHomeDataBean.MHomeDataItemData,
        model_type: String
    ): Boolean {
        // 先处理时间
        if (bean.limit_time != null
            && bean.limit_time.size == 2
            && bean.limit_time[0] != null
            && bean.limit_time[1] != null
            && bean.limit_time[0].isNotEmpty()
            && bean.limit_time[1].isNotEmpty()
            && bean.limit_time[0].length == 13
            && bean.limit_time[1].length == 13
        ) {
            val currentTime = System.currentTimeMillis()
            val startTime = bean.limit_time[0].toLong()
            val endTime = bean.limit_time[1].toLong()
            if (currentTime in startTime..endTime) {

            } else {
                return if (model_type == "10") {
                    bean.seldHide = true
                    false
                } else {
                    true
                }
            }
        }

        var inWhiteLevel = false
        var hasWhiteLevel = false
        var inBlackLevel = false

        var whiteLevel = bean.white_level
        if (whiteLevel != null && whiteLevel.isNotEmpty()) {
            hasWhiteLevel = true
            for (index in whiteLevel.indices) {
                if (whiteLevel[index][0] == mAppType && whiteLevel[index][1] == mUserType) {
                    inWhiteLevel = true
                    break
                }
            }
        }

        // 黑名单
        var blackLevel = bean.black_level
        if (blackLevel != null && blackLevel.isNotEmpty()) {
            for (index in blackLevel.indices) {
                if (blackLevel[index][0] == mAppType && blackLevel[index][1] == mUserType) {
                    inBlackLevel = true
                    break
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
                    if (model_type == "10") {
                        bean.seldHide = true
                    } else {
                        return true
                    }
                }
            } else {
                if (model_type == "10") {
                    bean.seldHide = true
                } else {
                    return true
                }
            }
        } else {
            if (inBlackLevel) {
                if (model_type == "10") {
                    bean.seldHide = true
                } else {
                    return true
                }
            }
        }
        return false
    }

    private fun isNeedDeleteLevel(
        mUserMobile: String,
        bean: MHomeDataBean.MHomeDataItemData,
        beanList: ArrayList<MHomeDataBean.MHomeDataItemData>,
        model_type: String
    ): Boolean {
        var inWhiteUsers = false
        var hasWhiteUsers = false
        var inBlackUsers = false

        // 白名单
        var whiteUsers = bean.white_users
        if (whiteUsers != null && !StringUtils.instance.isEmpty(whiteUsers)) {
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
        var blackUsers = bean.black_users
        if (blackUsers != null && !StringUtils.instance.isEmpty(blackUsers) && beanList.size > 0) {
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
                    if (model_type == "10") {
                        bean.seldHide = true
                    } else {
                        return true
                    }
                }
            } else {
                if (model_type == "10") {
                    bean.seldHide = true
                } else {
                    return true
                }
            }
        } else {
            if (inBlackUsers) {
                if (model_type == "10") {
                    bean.seldHide = true
                } else {
                    return true
                }
            }
        }
        return false
    }

    private fun filterData(bean: MHomeDataBean) {
        for (i in bean.rows.size - 1 downTo 0) {
            var item = bean.rows[i]
            // 黑白名单 用户身份级别
            if (item.state && item.data.isNotEmpty()) {
                var mAppType = JYUtils.instance.getMetaDatByName(context, "QY_APP_NAME")
                var mUserType = LocalUserManager.instance.getUserIdentity()
                for (index in item.data.size - 1 downTo 0) {
                    if (item.data[index].data != null && item.data[index].data.isNotEmpty()) {
                        for (y in item.data[index].data.size - 1 downTo 0) {
                            if (isNeedDeleteUsers(
                                    mAppType,
                                    mUserType,
                                    item.data[index].data[y],
                                    item.model_type
                                )
                            ) {
                                item.data[index].data.removeAt(y)
                            }
                        }
                    } else {
                        if (isNeedDeleteUsers(
                                mAppType,
                                mUserType,
                                item.data[index],
                                item.model_type
                            )
                        ) {
                            item.data.removeAt(index)
                        }
                    }
                }
            }

            // 黑白名单 用户级别
            if (item.state && item.data.isNotEmpty()) {
                var mUserMobile = LocalUserManager.instance.getUserMobile()
                for (index in item.data.size - 1 downTo 0) {
                    if (item.data[index].data != null && item.data[index].data.isNotEmpty()) {
                        for (y in item.data[index].data.size - 1 downTo 0) {
                            if (isNeedDeleteLevel(
                                    mUserMobile,
                                    item.data[index].data[y],
                                    item.data[index].data,
                                    item.model_type
                                )
                            ) {
                                item.data[index].data.removeAt(y)
                            }
                        }
                    } else {
                        if (isNeedDeleteLevel(
                                mUserMobile,
                                item.data[index],
                                item.data,
                                item.model_type
                            )
                        ) {
                            item.data.removeAt(index)
                        }
                    }
                }
            }
        }
    }

    private fun filterDataServer(bean: MHomeDataBean) {
        for (j in bean.rows.size - 1 downTo 0) {
            var item = bean.rows[j]
            // 根据服务端返回的判断要不要显示
            if (item.state && item.data.isNotEmpty()) {
                for (i in item.data.size - 1 downTo 0) {
                    if (item.data[i].data != null && item.data[i].data.isNotEmpty()) {
                        for (y in item.data[i].data.size - 1 downTo 0) {
                            if (item.data[i].data[y].is_show_to_server_link != null && !StringUtils.instance.isEmpty(
                                    item.data[i].data[y].is_show_to_server_link
                                )
                            ) {
                                if (pageLinkerServerMap[item.data[i].data[y].is_show_to_server_link] == null || !pageLinkerServerMap[item.data[i].data[y].is_show_to_server_link]!!) {
                                    item.data.removeAt(i)
                                }
                            }
                        }
                    } else {
                        if (item.data[i].is_show_to_server_link != null && !StringUtils.instance.isEmpty(
                                item.data[i].is_show_to_server_link
                            )
                        ) {
                            if (pageLinkerServerMap[item.data[i].is_show_to_server_link] == null || !pageLinkerServerMap[item.data[i].is_show_to_server_link]!!) {
                                item.data.removeAt(i)
                            }
                        }
                    }
                }
            }

        }
    }

    private fun showOperateDialog(page_base: MHomeDataBean.MHomeDataPage) {
        if (page_base.dialogs == null || page_base.dialogs.isEmpty()) {
            return
        }
        dialogs = page_base.dialogs
        mCurrenrDialogIndex = 0
        startShowOperateDialog()
    }

    private fun startShowOperateDialog() {
        if (dialogs == null || dialogs!!.isEmpty() || mCurrenrDialogIndex >= dialogs!!.size) {
            return
        }

        // 判断是否需要弹当前弹窗
        needPopOperateDialog(dialogs!![mCurrenrDialogIndex])
    }

    private fun dialogToShow() {
        val api: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

        RetrofitClient.getInstance()
            .execute(api.getPageConfig(page_key),
                object : AppObserver<BaseResultBean<MHomeDataBean>>() {
                    override fun onSuccess(o: BaseResultBean<MHomeDataBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            gotoDialog(
                                o.data,
                                dialogs!![mCurrenrDialogIndex].server_link,
                                dialogs!![mCurrenrDialogIndex].value
                            )
                            dialogCountChange(false)
                        } else {
                            dialogCountChange(false)
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        dialogCountChange(false)
                    }
                })
    }

    private fun dialogCountChange(needNext: Boolean) {
        mCurrenrDialogIndex++
        mStartDialog = mCurrenrDialogIndex < dialogs!!.size
        if (needNext) {
            if (mStartDialog) {
                startShowOperateDialog()
            }
        }
    }

    /**
     * 判断当前弹窗是否需要弹出
     */
    private fun needPopOperateDialog(dialog: MHomeDataBean.MHomeDataPageDialog) {
        if (dialog.type == "interval") { // 设置的评率
            if (dialog.interval_type == "day") { // 天为单位
                var currentTime = System.currentTimeMillis()
                var lastTime = JYMMKVManager.instance.getDialogTime(dialog.value)
                if (lastTime == 0L || TimeUtils.instance.getTimeExpendDay(
                        currentTime,
                        lastTime
                    ) >= dialog.interval_time
                ) {
                    JYMMKVManager.instance.setDialogTime(currentTime, dialog.value)
                    dialogToShow()
                    return
                } else {
                    dialogCountChange(true)
                    return
                }
            } else if (dialog.interval_type == "hour") { // 小时为单位
                var currentTime = System.currentTimeMillis()
                var lastTime = JYMMKVManager.instance.getDialogTime(dialog.value)
                if (lastTime == 0L || TimeUtils.instance.getTimeExpendHour(
                        currentTime,
                        lastTime
                    ) >= dialog.interval_time
                ) {
                    JYMMKVManager.instance.setDialogTime(currentTime, dialog.value)
                    dialogToShow()
                    return
                } else {
                    dialogCountChange(true)
                    return
                }
            }
        } else if (dialog.type == "service") { // 请求服务端控制频率
            val api: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

            RetrofitClient.getInstance().execute(
                api.getDialogByServer(RuntimeData.getInstance().httpUrl + dialog.server_link),
                object : AppObserver<BaseResultBean<MShowDialogServerBean>>() {
                    override fun onSuccess(o: BaseResultBean<MShowDialogServerBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess() && o.data.is_show) {
                            dialogToShow()
                            return
                        } else {
                            dialogCountChange(true)
                            return
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        dialogCountChange(true)
                        return
                    }
                })
        } else {
            dialogCountChange(true)
        }
    }

    private fun gotoDialog(data: MHomeDataBean, serverLink: String? = "", pageKey: String) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.JuggleAct.JUGGLE_DIALOG_SHOW)
        postcard.withSerializable(JYComConst.OPEN_CUSTOM_PAGE_DATA, data)
        postcard.withSerializable(JYComConst.OPEN_CUSTOM_PAGE, pageKey)
        postcard.withSerializable(JYComConst.OPEN_CUSTOM_PAGE_SERVERLINK, serverLink)
        postcard.withTransition(0, 0)
        postcard.navigation(context)
    }

    override fun onResume() {
        super.onResume()
        if (mStartDialog) {
            startShowOperateDialog()
        }
    }

    private var pageLinkerServerMap = HashMap<String, Boolean>()

    private fun getPagelinkServer(bean: MHomeDataBean) {
        pageLinkerServerMap.clear()
        val api: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

        for (index in bean.page_base.server_links) {
            RetrofitClient.getInstance().execute(
                api.getStatusByServer(RuntimeData.getInstance().httpUrl + index),
                object : AppObserver<BaseResultBean<MShowDialogServerBean>>() {
                    override fun onSuccess(o: BaseResultBean<MShowDialogServerBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            try {
                                var resoult = o.data.is_show
                                pageLinkerServerMap[index] = resoult
                            } catch (e: Exception) {
                                pageLinkerServerMap[index] = false
                            }
                        } else {
                            pageLinkerServerMap[index] = false
                        }

                        onGetPagelinkServerBack(bean)
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        pageLinkerServerMap[index] = false
                        onGetPagelinkServerBack(bean)
                    }
                })
        }
    }

    private fun onGetPagelinkServerBack(bean: MHomeDataBean) {
        if (pageLinkerServerMap != null && pageLinkerServerMap.size == bean.page_base.server_links.size) {
            initDataBack(bean)
        }
    }

    private fun initDataBack(bean: MHomeDataBean) {
        if (pageLinkerServerMap.isEmpty() && bean.page_base.server_links != null && bean.page_base.server_links.isNotEmpty()) {
            getPagelinkServer(bean)
            return
        }
        if (StringUtils.instance.isEmpty(mHomeDadaMD5)) {
            mHomeDadaMD5 = MD5Utils.digest(GsonUtils.objectToJsonStr(bean).toString())
            DLog.d(TAG, "第一次获取数据 mHomeDadaMD5 = $mHomeDadaMD5")
        } else {
            if (!bean.page_base.no_cache && mHomeDadaMD5 == MD5Utils.digest(
                    GsonUtils.objectToJsonStr(
                        bean
                    ).toString()
                )
            ) {
                DLog.d(TAG, "刷新数据一样，不用更新UI")
                if (mTopItem != null) {
                    getNtList(mTopItem!!);
                }
                if ((mAdapterAssert == null || mAdapterAssert.isEmpty()) && (pageLinkerServerMap == null || pageLinkerServerMap.isEmpty())) {
                    return
                }
            }
        }
        // 过滤数据
        try {
            filterData(bean)
            filterDataServer(bean)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        delegateAdapter.clear()
        mAdapterAssert.clear()
        // 头部
        mTitleInfo.set(bean.page_base)

        showOperateDialog(bean.page_base)
        if (bean.page_base.bg_color != null && !StringUtils.instance.isEmpty(bean.page_base.bg_color)) {
            recyclerView.setBackgroundColor(ColorUtils.instance.getColorForString(bean.page_base.bg_color))
        }

        for (index in 0 until bean.rows.size) {
            var item = bean.rows[index]
            item.setPageTag(mPageTag)
            item.setPageKey(page_key)
            item.setLastPageTag(lastPageTag)
            // 自定义组件
            if (item.is_diy_components) {
                DLog.d(TAG, "自定义组件")
                mDiyNum += 1

                var bannerManager = LinearLayoutHelper()
                var adapter =
                    JuggleDiyComponentsAdapter(
                        mFragment,
                        bannerManager,
                        item,
                        mPageTag,
                        lastPageTag,
                        mDiyNum
                    )
                delegateAdapter.addAdapter(adapter)
            } else {
                // banner
                if (item.model_type == "1" && item.state && item.data.isNotEmpty()) {
                    DLog.d(TAG, "轮播轮播")

                    var bannerManager = LinearLayoutHelper()
                    var topAdapter = HomeBannerAdapter(context, bannerManager, item)
                    delegateAdapter.addAdapter(topAdapter)
                }
                if (item.model_type == "2" && item.state && item.data.isNotEmpty()) {
                    DLog.d(TAG, "Gallery 轮播轮播")

                    var bannerManager = LinearLayoutHelper()
                    var topAdapter = HomeBannerGalleryAdapter(context, bannerManager, item)
                    delegateAdapter.addAdapter(topAdapter)
                }
                // line
                if (item.model_type == "3" && item.state && item.data.isNotEmpty()) {
                    if (item.is_fixed_bottom) {
                        DLog.d(TAG, "通栏 --- > 吸底")
                        var bannerStickManager = StickyLayoutHelper(false)

                        var stickLineAdapter =
                            HomeStickLineAdapter(context, bannerStickManager, item)
                        delegateAdapter.addAdapter(stickLineAdapter)

                        stickBottomPos = delegateAdapter.adaptersCount
                    } else {
                        DLog.d(TAG, "通栏 --- > 正常")
                        var bannerManager = LinearLayoutHelper()
                        var lineAdapter = HomeLineAdapter(context, bannerManager, item)
                        delegateAdapter.addAdapter(lineAdapter)
                    }
                }
                // 横向滚动
                if (item.model_type == "4" && item.state && item.data.isNotEmpty()) {
                    DLog.d(TAG, "横向滚动")

                    var bannerManager = LinearLayoutHelper()
                    var topAdapter = HomeHScrollAdapter(context, bannerManager, item)
                    delegateAdapter.addAdapter(topAdapter)
                }
                // 等宽排列-堆图片
                if (item.model_type == "5" && item.state && item.data.isNotEmpty()) {
                    DLog.d(TAG, "等分")

                    var bannerManager = GridLayoutHelper(item.data.size)
                    var lineAdapter = HomedengfenAdapter(context, bannerManager, item)
                    delegateAdapter.addAdapter(lineAdapter)
                }
                // 自定义宽度
                if (item.model_type == "9" && item.state && item.data.isNotEmpty()) {
                    DLog.d(TAG, "自定义宽度")

                    var bannerManager = LinearLayoutHelper()
                    var lineAdapter = HomeLinePercentAdapter(context, bannerManager, item)
                    delegateAdapter.addAdapter(lineAdapter)
                }
                // 能量球
                if (item.model_type == "10") {
                    DLog.d(TAG, "能量球")
                    if (LocalUserManager.instance.getUser() == null) {
                        getUserInfo()
                    }

                    var bean = HomeEquitiesPoolBean()
                    var bannerManager = LinearLayoutHelper()
                    homePoolAdapter =
                        HomeEquitiesPoolAdapter(context, bannerManager, bean, item, 15,
                            HomeEquitiesPoolAdapter.BallClickListener { mPoolView, view, bean, position ->
                                onBcllClick(mPoolView, view, bean, position)
                            })
                    delegateAdapter.addAdapter(homePoolAdapter)
                    mTopItem = item
                    getNtList(item)
                }
                // 联动组件
                if (item.model_type == "11" && item.state && item.data.isNotEmpty()) {
                    DLog.d(TAG, "联动")
                    if (item.skin == "horizontal" && !RuntimeData.getInstance().hideBlock) {
                        var liandongManager = LinearLayoutHelper()
                        var liandongAdapter =
                            HomeLDAdapter(context, liandongManager, mFragment, item)
                        delegateAdapter.addAdapter(liandongAdapter)
                    } else if (item.skin == "vertical") { // 纵向左右联动
                        var ldRecyclerLayout = LinearLayoutHelper()
                        liandongVAdapter = HomeLDVAdapter(
                            context,
                            ldRecyclerLayout,
                            item,
                            scrollToBottom,
                            page_no_back
                        )
                        delegateAdapter.addAdapter(liandongVAdapter)
                    }
                }
                // 商品组件
                if (item.model_type == "12" && item.state) {
                    DLog.d(TAG, "商品组件")
                    mTotalPage = 9999

                    var goodsManager = GridLayoutHelper(2)
                    goodsManager.hGap = context.resources.getDimension(R.dimen.dp_10).toInt()
                    goodsManager.vGap = context.resources.getDimension(R.dimen.dp_10).toInt()
                    goodsManager.marginLeft = context.resources.getDimension(R.dimen.dp_15).toInt()
                    goodsManager.marginRight = context.resources.getDimension(R.dimen.dp_15).toInt()
                    goodsManager.setAutoExpand(false)
                    goodsAdapter = HomeGoodsAdapter(context, goodsManager, item)
                    delegateAdapter.addAdapter(goodsAdapter)

                    mGoodsUrlLink = item.server_link
                    getGoodsData()
                }
                // 个人中心
                if (item.model_type == "13" && item.state) {
                    DLog.d(TAG, "个人信息展示")

                    var mineLayoutHelper = LinearLayoutHelper()
                    mineAdapter = HomeMineAdapter(context, mineLayoutHelper, item)
                    delegateAdapter.addAdapter(mineAdapter)
                    if (LocalUserManager.instance.getUser() == null) {
                        getUserInfo()
                    }
                }
                // 个人中心数据豆腐块
                if (item.model_type == "16" && item.state) {
                    DLog.d(TAG, "个人信息资产展示")

                    var count = if (item.skin == "1") 2 else 3

                    var zichanLayoutHelper = GridLayoutHelper(count)
                    zichanLayoutHelper.marginLeft =
                        context.resources.getDimension(R.dimen.dp_10).toInt()
                    zichanLayoutHelper.marginRight =
                        context.resources.getDimension(R.dimen.dp_10).toInt()
                    var zichanAdapter =
                        HomeZichanAdapter(context, zichanLayoutHelper, item, "")

                    delegateAdapter.addAdapter(zichanAdapter)
                    mAdapterAssert.add(zichanAdapter)

                    getUserAssets(zichanAdapter, item.data_url)
                }
                // 通证中心
                if (item.model_type == "15" && item.state) {
                    DLog.d(TAG, "通证中心展示")

                    item.title = bean.page_base.title
                    var mineLayoutHelper = LinearLayoutHelper()
                    tzAdapter = HomeTongzhengAdapter(context, mineLayoutHelper, item)
                    delegateAdapter.addAdapter(tzAdapter)
                    if (LocalUserManager.instance.getUser() == null) {
                        getUserInfo()
                    }
                }
                if (item.model_type == "20" && item.state) {
                    DLog.d(TAG, "俄罗斯套娃 三方任务页")
                    mShellNum += 1
                    var bannerManager = LinearLayoutHelper()
                    var adapter =
                        JuggleShellAdapter(
                            mFragment,
                            bannerManager,
                            item,
                            mPageTag,
                            lastPageTag,
                            mShellNum
                        )
                    delegateAdapter.addAdapter(adapter)
                }
                //
                if (item.model_type == "17" && item.state) {
                    DLog.d(TAG, "循环组件")
                    if (item.type == "data_skin") {
                        DLog.d(TAG, "循环组件 -> data_skin")
                        if (item.skin == "2") {
                            var goodsManager = GridLayoutHelper(2)
                            goodsManager.vGap =
                                context.resources.getDimension(R.dimen.dp_10).toInt()
                            goodsManager.hGap =
                                context.resources.getDimension(R.dimen.dp_10).toInt()
                            goodsManager.setAutoExpand(false)
                            var goodsSkinAdapter = JuggleGoodsGridAdapter(context, goodsManager, item)
                            delegateAdapter.addAdapter(goodsSkinAdapter)
                        } else {
                            var goodsManager = GridLayoutHelper(1)
                            goodsManager.vGap =
                                context.resources.getDimension(R.dimen.dp_10).toInt()
                            goodsManager.setAutoExpand(false)
                            var goodsSkinAdapter = JuggleGoodsAdapter(context, goodsManager, item)
                            delegateAdapter.addAdapter(goodsSkinAdapter)
                        }
                    } else if (item.type == "ajax_diy_components") {
                        DLog.d(TAG, "循环组件 -> ajax_diy_components")
                        var goodsManager = GridLayoutHelper(item.skin.toInt())
                        goodsManager.setAutoExpand(false)
                        var adapter =
                            JuggleDiyComponentsListAdapter(
                                mFragment,
                                goodsManager,
                                item.skin.toInt(),
                                mPageTag,
                                lastPageTag
                            )

                        if (diyListMap == null) {
                            diyListMap = hashMapOf()
                        }
                        diyListMap!![item.components_data.key] = adapter

                        delegateAdapter.addAdapter(adapter)

                        getDiyListByServer(item.method, item.data_url, item.components_data.key)
                    }
                }

                if (item.model_type == "30" && item.state) {
                    DLog.d(TAG, "登录表单")

                    submitFromBean = RxBus.getDefault().toObservable(SubmitFromBean::class.java)
                        .subscribe {
                            DLog.d("登录表单提交")
                            if (it.pageKey == page_key) {
                                doLogin()
                            }
                        }
                    RxSubscriptions.add(submitFromBean)

                    var bannerManager = LinearLayoutHelper()
                    loginFromAdapter = JuggleLoginAdapter(mFragment, bannerManager, item)
                    delegateAdapter.addAdapter(loginFromAdapter)
                }
            }

            recyclerView.adapter = delegateAdapter
        }

        if (stickBottomPos != -1) {
            Handler().postDelayed({
                doStickRefresh();
            }, 500)
        }
    }

    private fun doStickRefresh() {
        if (stickBottomPos == -1) {
            return
        }
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun onBcllClick(
        mPoolView: EquitiesPoolView,
        view: View,
        bean: EquitiesPollItem,
        position: Int
    ) {
        if (bean.isDefault) {
            JugglePathUtils.instance.onJuggleItemClick(bean.item, "HOME_BALL", position)
        } else {
            DLog.d(TAG, "收 NT ... ")
            if (TextUtils.isEmpty(bean.nt_amount)) {
                return
            }
            SLSLogUtils.instance.sendLogClick("JuggleFragment", "", "NT_BALL")
            // 先进行行为反馈，然后在请求
            mPoolView.startballDismiss(view, bean, position)
            PlayVoiceUtil.playAssetsAudio("get_token_voice.mp3", context)
            SystemUtil.mobileVibrator(context)

            sendGetBt(bean)
        }
    }

    private fun sendGetBt(bean: EquitiesPollItem) {}

    private fun getUserAssets(adapter: HomeZichanAdapter, url: String) {
        val service = RetrofitClient.getInstance().create(HomeService::class.java)

        RetrofitClient.getInstance()
            .execute(service.getIndoAssets(
                RuntimeData.getInstance().httpUrl + "/api" + url
            ),
                object : AppObserverNoDialog<BaseResultBean<JsonObject>>() {
                    override fun onSuccess(o: BaseResultBean<JsonObject>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            adapter.setDataBena(o.data.toString())
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                    }
                })
    }

    private fun getGoodsData() {
        if (StringUtils.instance.isEmpty(mGoodsUrlLink)) {
            return
        }
        DLog.d(TAG, "获取首页 商品数据")
        val service = RetrofitClient.getInstance().create(HomeService::class.java)

        RetrofitClient.getInstance()
            .execute(service.getGoodsList(
                RuntimeData.getInstance().httpUrl + mGoodsUrlLink,
                getRequestBody(BaseBean(CGetGoodsBean(20, mCurrentPage)))
            ),
                object : AppObserverNoDialog<BaseResultBean<MGoodsBean>>() {
                    override fun onSuccess(o: BaseResultBean<MGoodsBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            if (o.data.goodsList == null || o.data.goodsList.size == 0) {
                                mTotalPage = mCurrentPage
                            }
                            if (goodsAdapter != null) {
                                if (mCurrentPage == 1) {
                                    goodsAdapter!!.setGoodData(o.data.goodsList)
                                } else {
                                    goodsAdapter!!.setGoodDataAdd(o.data.goodsList)
                                    onLoadFinish()
                                }
                            }
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        onLoadFinish()
                    }
                })
    }

    private fun getNtList(item: MHomeDataBean.MHomeDataItem) {
        val service = RetrofitClient.getInstance().create(HomeService::class.java)

        RetrofitClient.getInstance()
            .execute(service.getHomeNtList(getRequestBody(BaseBean<Any>())),
                object : AppObserverNoDialog<BaseResultBean<MHomeBtList>>() {
                    override fun onSuccess(o: BaseResultBean<MHomeBtList>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            initHomePoolData(item, o.data)
                        }
                    }
                })
    }

    private fun doLogin() {
        DLog.d("登录登录登录")
        if (loginFromAdapter == null) {
            return
        }
        var account = loginFromAdapter!!.getAccount()
        var password = loginFromAdapter!!.getPassword()
        if (TextUtils.isEmpty(account)) {
            ToastUtils.showShort("${loginFromAdapter!!.item.login_name_text}不能为空")
            return
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showShort("${loginFromAdapter!!.item.login_password_text}不能为空")
            return
        }
        if (!loginFromAdapter!!.getIsChose()) {
            ToastUtils.showShort("请先同意隐私政策与用户协议")
            return
        }

        val service = RetrofitClient.getInstance().create(HomeService::class.java)

        RetrofitClient.getInstance()
            .execute(service.sendLogin(
                getRequestBody(CLoginBeanBean(account, password))
            ),
                object : AppObserverNoDialog<BaseResultBean<MUserBean>>() {
                    override fun onSuccess(o: BaseResultBean<MUserBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            LocalUserManager.instance.login(o.data)
                            RouterManager.instance.gotoMainActivity(JYComConst.HOME_CHOSE_TAB_HOME)
                            finish()
                        }
                    }
                })
    }

    private fun initHomePoolData(item: MHomeDataBean.MHomeDataItem, ntList: MHomeBtList) {
        var list = ArrayList<EquitiesPollItem>()
        // BT
        var bean = HomeEquitiesPoolBean()
        // 1: 左上, 1:左下  3：右上球1，4：1: 右上球2, 5：1: 右上球3
        if (item.data != null && item.data.size > 1) {
            for (index in 1 until item.data.size) {
                if (item.data[index].img_url != null && !StringUtils.instance.isEmpty(item.data[index].img_url)
                    && !item.data[index].seldHide
                ) {
                    var defaultItem = EquitiesPollItem()
                    defaultItem.isDefault = true
                    defaultItem.type = index
                    defaultItem.item = item.data[index]
                    list.add(defaultItem)
                }
            }
        }
        for (index in 0 until ntList.ntList.size) {
            var bean = EquitiesPollItem()
            bean.name = ntList.ntList[index].ntAmount
            bean.nt_amount = ntList.ntList[index].ntAmount
            bean.stage = ntList.ntList[index].stage.toString()
            list.add(bean)
        }

        bean.nt_list = list
        homePoolAdapter!!.setBean(bean)
    }

    private var mineAdapter: HomeMineAdapter? = null
    private var tzAdapter: HomeTongzhengAdapter? = null
    private var homePoolAdapter: HomeEquitiesPoolAdapter? = null
    private var goodsAdapter: HomeGoodsAdapter? = null
    private var mGoodsUrlLink = ""

    override fun onGetUserInfoBack(userBean: MUserBean?) {
        DLog.d("USERINFO", "onGetUserInfoBack")
        if (userBean == null) {
            return
        }
        if (mineAdapter != null) {
            mineAdapter!!.notifyDataSetChanged()
        }
        if (homePoolAdapter != null) {
            homePoolAdapter!!.notifyDataSetChanged()
        }
        assertsChanged()
        if (tzAdapter != null) {
            tzAdapter!!.notifyDataSetChanged()
        }
    }

    private fun assertsChanged() {
        for (index in 0 until mAdapterAssert.size) {
            mAdapterAssert[index].notifyDataSetChanged()
        }
    }

    override fun onLoadRefresh() {
        getUserInfo()
        getHomeData()
        SLSLogUtils.instance.sendLogLoad(
            "JuggleFragment",
            page_key,
            "REFRESH"
        )
    }

    override fun onLoadMore() {
        mCurrentPage += 1
        getGoodsData()
        SLSLogUtils.instance.sendLogLoad(
            "JuggleFragment",
            page_key,
            "LOADMORE"
        )
    }

    override fun onLoadFisished() {
        showLoadSuccess()
    }

    private var refreshUser: Disposable? = null
    private var refreshBtAmount: Disposable? = null
    private var onClosePage: Disposable? = null
    private var onReloadPage: Disposable? = null
    private var submitFromBean: Disposable? = null

    private fun initEvent() {
        refreshUser = RxBus.getDefault().toObservable(RefreshUserInfo::class.java)
            .subscribe {
                DLog.d("个人中心刷新用户")
                onGetUserInfoBack(LocalUserManager.instance.getUser())
            }
        RxSubscriptions.add(refreshUser)

        refreshBtAmount = RxBus.getDefault().toObservable(RefreshUserBtAmount::class.java)
            .subscribe {
                DLog.d("刷新 BT")

                assertsChanged()
                if (homePoolAdapter != null) {
                    homePoolAdapter!!.setUsetBtChanged()
                }
                if (tzAdapter != null) {
                    tzAdapter!!.notifyDataSetChanged()
                }
            }
        RxSubscriptions.add(refreshBtAmount)

        onClosePage = RxBus.getDefault().toObservable(MClosePageBean::class.java)
            .subscribe {
                if (it.pageTag == mPageTag) {
                    DLog.d("关闭当前页面")
                    finish()
                }
            }
        RxSubscriptions.add(onClosePage)

        onReloadPage = RxBus.getDefault().toObservable(MReloadPageBean::class.java)
            .subscribe {
                if (it.pageTag == mPageTag) {
                    DLog.d("刷新当前页面")
                    mHomeDadaMD5 = ""
                    onLoadRefresh()
                }
            }
        RxSubscriptions.add(onReloadPage)
    }

    private fun removeEnent() {
        if (refreshUser != null) {
            RxSubscriptions.remove(refreshUser)
            refreshUser = null
        }
        if (refreshBtAmount != null) {
            RxSubscriptions.remove(refreshBtAmount)
            refreshBtAmount = null
        }
        if (onClosePage != null) {
            RxSubscriptions.remove(onClosePage)
            onClosePage = null
        }
        if (onReloadPage != null) {
            RxSubscriptions.remove(onReloadPage)
            onReloadPage = null
        }
        if (submitFromBean != null) {
            RxSubscriptions.remove(submitFromBean)
            submitFromBean = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeEnent()
    }
}