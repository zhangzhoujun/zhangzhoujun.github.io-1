package com.qm.lib.router

import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.DLog
import com.dim.library.utils.GsonUtils
import com.dim.library.utils.ToastUtils
import com.dim.library.utils.Utils
import com.qm.lib.entity.MAppConfigBean
import com.qm.lib.entity.MGoodsBean
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.JYMMKVManager
import com.qm.lib.utils.JYUtils
import com.qm.lib.utils.RuntimeData
import com.qm.lib.utils.StringUtils

/**
 * @ClassName RouterManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 7:02 PM
 * @Version 1.0
 */
class RouterManager private constructor() {
    companion object {
        val instance: RouterManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RouterManager()
        }
    }

    /**
     * 跳转到原生或者h5页面
     */
    fun gotoOtherPage(url: String) {
        gotoNativeApp(url)
    }

    /**
     * 跳转到原生或者h5页面
     */
    fun gotoOtherPage(appPath: String?, webUrl: String?, gotoNative: Boolean = true) {
        if (gotoNative && appPath != null && !TextUtils.isEmpty(appPath)) {
            gotoNativeApp(appPath!!)
        } else {
            gotoWebviewActivity(webUrl!!)
        }
    }

    /**
     * 跳转到原生的APP页面
     * pathAndParams ： /Juggle/indexpage_key=111&key=value
     * arouter://qm.valuechain.com/JuggleFra/index?page_key=1607330405379_980536048
     */
    fun gotoNativeApp(pathAndParams: String, pageTag: String = "") {
        if (TextUtils.isEmpty(pathAndParams)) {
            return
        }
        if (StringUtils.instance.isHttpUrl(pathAndParams)) {
            gotoWebviewActivity(pathAndParams)
            return
        }

        var pathAndParamsString = pathAndParams
        if (pageTag != null && !StringUtils.instance.isEmpty(pageTag)) {
            if (pathAndParamsString.contains("?")) {
                pathAndParamsString =
                    pathAndParamsString + "&" + JYComConst.OPEN_CUSTOM_PAGE_TAG + pageTag
            } else {
                pathAndParamsString =
                    pathAndParamsString + "?" + JYComConst.OPEN_CUSTOM_PAGE_TAG + pageTag
            }
        }

        try {
            var currentScheme = JYUtils.instance.getMetaDatByName(Utils.getContext(), "scheme_host")

            if (pathAndParamsString.startsWith("arouter")) {
                pathAndParamsString = pathAndParamsString.replace("arouter://$currentScheme", "")
            }
            val postcard: Postcard
            if (pathAndParamsString.contains("?")) {
                var arrayString = StringUtils.instance.getStringSplit(pathAndParamsString, "?")
                var path = arrayString[0]
                postcard = ARouter.getInstance().build(path)
                if (arrayString.size == 2) {
                    var params = StringUtils.instance.getStringSplit(arrayString[1], "&")
                    for (index in params.indices) {
                        var keyAndValue =
                            StringUtils.instance.getStringSplit(params[index], "=")
                        if (keyAndValue.size == 2) {
                            postcard.withString(keyAndValue[0], keyAndValue[1])
                        }
                    }
                }
            } else {
                postcard = ARouter.getInstance().build(pathAndParamsString)
            }
            postcard.navigation()
            return
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getNativeFragment(pathAndParamsString: String): Fragment? {
        if (TextUtils.isEmpty(pathAndParamsString)) {
            return null
        }
        try {
            val postcard: Postcard = getNativeFragmentPostcard(pathAndParamsString) ?: return null

            var fragment = postcard.navigation(null, null)
            return fragment as Fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getNativeFragmentPostcard(pathAndParamsString: String): Postcard? {
        if (TextUtils.isEmpty(pathAndParamsString)) {
            return null
        }
        try {
            if (StringUtils.instance.isHttpUrl(pathAndParamsString)) {
                val postcard: Postcard = ARouter.getInstance()
                    .build(RouterFragmentPath.Webview.WEB_INDEX)
                postcard.withString(JYComConst.WEBVIEW_URL, pathAndParamsString)
                postcard.withString("noToolbar", "true")

                return postcard
            }
            var pathAndParams = pathAndParamsString
            if (pathAndParamsString.startsWith("arouter")) {
                pathAndParams = pathAndParamsString.replace("arouter://qm.valuechain.com", "")
            }
            val postcard: Postcard
            if (pathAndParams.contains("?")) {
                var arrayString = StringUtils.instance.getStringSplit(pathAndParams, "?")
                var path = arrayString[0]
                postcard = ARouter.getInstance().build(path)
                if (arrayString.size == 2) {
                    var params = StringUtils.instance.getStringSplit(arrayString[1], "&")
                    for (index in params.indices) {
                        var keyAndValue =
                            StringUtils.instance.getStringSplit(params[index], "=")
                        if (keyAndValue.size == 2) {
                            postcard.withString(keyAndValue[0], keyAndValue[1])
                        }
                    }
                }
            } else {
                postcard = ARouter.getInstance().build(pathAndParams)
            }
            return postcard
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 跳转到拼图
     */
    fun gotoJuggleActivity(pageKey: String) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.JuggleAct.JUGGLE_INDEX)
        postcard.withString(JYComConst.OPEN_CUSTOM_PAGE, pageKey)
        postcard.navigation()
    }

    fun gotoJuggleDialogActivity(pageKey: String, lastPageTag: String) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.JuggleAct.JUGGLE_DIALOG)
        postcard.withString(JYComConst.OPEN_CUSTOM_PAGE, pageKey)
        postcard.withString(JYComConst.OPEN_CUSTOM_PAGE_TAG, lastPageTag)
        postcard.withTransition(0, 0)
        postcard.navigation()
    }

    fun gotoPlayerActivity(videoPlayerUrl: String) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.JuggleAct.JUGGLE_PLAYER)
        postcard.withString(JYComConst.VIDEO_PLAYER_URL, videoPlayerUrl)
        postcard.navigation()
    }

    /**
     * 跳转到主页
     */
    fun gotoMainActivity(choseKey: String) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Main.MAIN_INDEX)
        postcard.withString(JYComConst.HOME_CHOSE_TAB, choseKey)
        postcard.navigation()
    }

    /**
     * 新手引导页面
     */
    fun gotoMainGuideActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Main.MAIN_GUIDE)
        postcard.navigation()
    }

    fun gotoUseageAccessActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Main.MAIN_USAGE)
        postcard.navigation()
    }

    /**
     * 跳转到扫码页面
     */
    fun gotoQRActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.JuggleAct.JUGGLE_QR)
        postcard.navigation()
    }

    /**
     * 跳转到第一次的弹窗页面页面
     */
    fun gotoOneDialogActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.UMShare.SHARE_ONE_DIALOG)
        postcard.navigation()
    }

    /**
     * 跳转到登陆
     */
    fun gotoLoginActivity() {
        val appConfig = JYMMKVManager.instance.getAppConfigData()
        if (appConfig != null && !StringUtils.instance.isEmpty(appConfig)) {
            val config = GsonUtils.fromJson(appConfig, MAppConfigBean::class.java)
            if (config != null) {
                gotoOtherPage(config.base_data.login_path)
                return
            }
        }
        ToastUtils.showShort("升级中")
    }

    /**
     * 跳转到我的设置页面
     */
    fun gotoMineSetActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Mine.MINE_SET)
        postcard.navigation()
    }

    /**
     * 跳转个人info
     */
    fun gotoAboutActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Mine.INFO)
        postcard.navigation()
    }

    /**
     * 跳转到修改个人信息页面
     */
    fun gotoInfoModifyActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Mine.INFO)
        postcard.navigation()
    }

    /**
     * 跳转到Share页面
     */
    fun gotoUMShareActivity(
        url: String,
        img: String,
        describe: String,
        name: String,
        invitation_code: String
    ) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.UMShare.SHARE_MAIN)
        postcard.withString(JYComConst.SHARE_URL, url)
        postcard.withString(JYComConst.SHARE_IMG, img)
        postcard.withString(JYComConst.SHARE_DES, describe)
        postcard.withString(JYComConst.SHARE_NAME, name)
        postcard.withString(JYComConst.SHARE_INVITATION, invitation_code)
        postcard.navigation()
    }

    /**
     * 跳转到ShareGuide页面
     */
    fun gotoUMShareGuideActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.UMShare.SHARE_GUIDE)
        postcard.navigation()
    }

    /**
     * 跳转到common resoult页面
     * @param title 标题
     * @param des 内容
     * @param sureText 确认按钮文案
     * @param type 类型
     * @param needCallBack 是否需要确认按钮点击反馈
     * @param tag 需要确认按钮反馈的时候输入的tag
     */
    fun gotoCommonResultActivity(
        title: String,
        des: String,
        sureText: String,
        type: Int = JYComConst.COMMON_RESULT_TYPE_SUCCESS,
        needCallBack: Boolean = false,
        tag: String = ""
    ) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Common.COMMON_RESULT)
        postcard.withString(JYComConst.COMMON_RESULT_TITLE, title)
        postcard.withString(JYComConst.COMMON_RESULT_DES, des)
        postcard.withString(JYComConst.COMMON_RESULT_SURE_NAME, sureText)
        postcard.withString(JYComConst.COMMON_RESULT_CALLBACK_TAG, tag)
        postcard.withInt(JYComConst.COMMON_RESULT_TYPE, type)
        postcard.withBoolean(JYComConst.COMMON_RESULT_CALLBACK, needCallBack)
        postcard.navigation()
    }

    fun gotoFeedbackActivity() {
//        val postcard: Postcard = ARouter.getInstance()
//            .build(RouterActivityPath.Common.COMMON_FEEDBACK)
//        postcard.navigation()
        val appConfig = JYMMKVManager.instance.getAppConfigData()
        if (appConfig != null && !StringUtils.instance.isEmpty(appConfig)) {
            val config = GsonUtils.fromJson(appConfig, MAppConfigBean::class.java)
            if (config != null) {
                gotoOtherPage(config.base_data.feedback_url)
                return
            }
        }
        ToastUtils.showShort("升级中")
    }

    /**
     * 跳转到版本更新页面
     * @param version 下载版本
     * @param url 下载链接
     * @param des 更新文案
     * @param fource 是否强制升级
     */
    fun gotoVersionUpdateActivity(
        version: String,
        url: String,
        des: String,
        fource: Boolean = false
    ) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Version.VERSION_UPDATE)
        postcard.withString(JYComConst.VERSION_DOWNLOAD_VERSION, version)
        postcard.withString(JYComConst.VERSION_DOWNLOAD_URL, url)
        postcard.withString(JYComConst.VERSION_DOWNLOAD_DES, des)
        postcard.withBoolean(JYComConst.VERSION_DOWNLOAD_FOURCE, fource)
        postcard.navigation()
    }

    fun gotoDownloadActivity(
        icon: String,
        url: String,
        des: String
    ) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Version.VERSION_DOWNLOAD)
        postcard.withString(JYComConst.VERSION_DOWNLOAD_VERSION, icon)
        postcard.withString(JYComConst.VERSION_DOWNLOAD_URL, url)
        postcard.withString(JYComConst.VERSION_DOWNLOAD_DES, des)
        postcard.withBoolean(JYComConst.VERSION_DOWNLOAD_FOURCE, true)
        postcard.navigation()
    }

    /**
     * 跳转到h5
     */
    fun gotoWebviewActivity(
        url: String,
        title: String = "加载中"
    ) {
        if (TextUtils.isEmpty(url)) {
            return
        }
        var linkUrl = url
        if (!StringUtils.instance.isHttpUrl(url)) {
            linkUrl = "${RuntimeData.getInstance().webHost}$url"
        }
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Webview.WEBVIEW_MAIN)
        postcard.withString(JYComConst.WEBVIEW_URL, linkUrl)
        postcard.withString(JYComConst.WEBVIEW_TITLE, title)
        postcard.navigation()
    }

    /**
     * 跳转到商品详情
     */
    fun gotoGoodsDetailsActivity(
        goodsBean: MGoodsBean.MJuggleGoodsItemBean
    ) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Goods.GOODS_MAIN)
        postcard.withSerializable(JYComConst.GOODS_DETAIL, goodsBean)
        postcard.navigation()
    }

    /**
     * 跳转到用户协议
     */
    fun gotoYonghuXieyiActivity() {
        val appConfig = JYMMKVManager.instance.getAppConfigData()
        if (appConfig != null && !StringUtils.instance.isEmpty(appConfig)) {
            val config = GsonUtils.fromJson(appConfig, MAppConfigBean::class.java)
            if (config != null) {
                gotoOtherPage(config.user_agreement)
                return
            }
        }
        ToastUtils.showShort("升级中")
    }

    /**
     * 跳转到隐私政策
     */
    fun gotoYinsizhengceActivity() {
        val appConfig = JYMMKVManager.instance.getAppConfigData()
        if (appConfig != null && !StringUtils.instance.isEmpty(appConfig)) {
            val config = GsonUtils.fromJson(appConfig, MAppConfigBean::class.java)
            if (config != null) {
                gotoOtherPage(config.privacy_policy)
                return
            }
        }
        ToastUtils.showShort("升级中")
    }

    /**
     * 跳转到BT记录页面
     */
    fun gotoBTRecordActivity() {
        gotoWebviewActivity(
            RuntimeData.getInstance().webHost + "/record",
            "记录页"
        )
    }

    /**
     * 新手引导页面
     */
    fun gotoMainWalkDetailActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Watch.WATCH_WALK_MAIN)
        postcard.navigation()
    }

    /**
     * 跳转个人info
     */
    fun gotoStrpChallengeRecordActivity() {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.Step.STEP_RECORD)
        postcard.navigation()
    }
}