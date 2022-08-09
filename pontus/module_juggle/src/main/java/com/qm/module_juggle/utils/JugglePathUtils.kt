package com.qm.module_juggle.utils

import android.text.TextUtils
import com.dim.library.bus.RxBus
import com.dim.library.utils.ToastUtils
import com.qm.lib.entity.CGetUrlForServerBean
import com.qm.lib.entity.MClosePageBean
import com.qm.lib.entity.MReloadPageBean
import com.qm.lib.message.RefreshUserByServer
import com.qm.lib.message.SubmitFromBean
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.utils.StringUtils
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.entity.MHomeDiyBean

/**
 * @ClassName ColorUtils
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/29 10:38 AM
 * @Version 1.0
 */
class JugglePathUtils private constructor() {
    companion object {
        val instance: JugglePathUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JugglePathUtils()
        }
    }

    fun onJuggleItemClick(
        item: MHomeDataBean.MHomeDataItemData,
        type: String = "",
        pos: Int = -1,
        posString: String = ""
    ) {
        var viewImageUrl = ""
        if (item.img_url != null) {
            viewImageUrl = item.img_url
        }
        SLSLogUtils.instance.sendLogJuggleClick(
            item.pageKey,
            type,
            pos,
            posString,
            item.id,
            viewImageUrl
        )

        if (item.app_path != null && !StringUtils.instance.isEmpty(item.app_path)) {
            RouterManager.instance.gotoNativeApp(item.app_path, item.pageTag)
        }
        if (item.copy_text != null && !StringUtils.instance.isEmpty(item.copy_text)) {
            StringUtils.instance.doCopy(item.copy_text)
        }
        if (item.toast != null && !StringUtils.instance.isEmpty(item.toast)) {
            ToastUtils.showShort(item.toast)
        }
        if (item.dialog_key != null && !StringUtils.instance.isEmpty(item.dialog_key)) {
            openDialog(item.dialog_key, item.pageTag)
        }
        var hasClose = false
        var hasReload = false
        var refreshUser = false
        var submitForm = false

        var arrayString = item.funs
        for (index in arrayString.indices) {
            var funs = arrayString[index]
            when (funs) {
                "close_webview" -> hasClose = true
                "reload_webview" -> hasReload = true
                "refresh_user" -> refreshUser = true
                "submit_form" -> submitForm = true
            }
        }

        if (refreshUser) {
            RxBus.getDefault().post(RefreshUserByServer())
        }

        if (submitForm) {
            RxBus.getDefault().post(SubmitFromBean(item.pageKey))
        }

        if (item.server_link != null && !StringUtils.instance.isEmpty(item.server_link)) {
            doServerGet(
                item.server_link,
                item.link,
                hasClose,
                hasReload,
                item.pageTag,
                item.lastPageTag
            )
        } else {
            if (!StringUtils.instance.isEmpty(item.link)) {
                RouterManager.instance.gotoWebviewActivity(item.link)
            }
            if (hasClose) {
                if (hasReload) {
                    // 关闭当前页面并刷新上一个页面
                    reloadPage(item.lastPageTag)
                    closePage(item.pageTag)
                } else {
                    // 关闭当前页面
                    closePage(item.pageTag)
                }
            } else {
                // 刷新当前页面
                if (hasReload) {
                    reloadPage(item.pageTag)
                }
            }
        }
    }

    private fun closePage(pageTag: String) {
        if (!pageTag.isNullOrEmpty()) {
            RxBus.getDefault().post(MClosePageBean(pageTag))
        }
    }

    private fun reloadPage(pageTag: String) {
        RxBus.getDefault().post(MReloadPageBean(pageTag))
    }

    private fun openDialog(dialogKey: String, lastPageTag: String) {
        RouterManager.instance.gotoJuggleDialogActivity(dialogKey, lastPageTag)
    }

    // 发送网络请求
    private fun doServerGet(
        server_link: String,
        linkUrl: String,
        needColse: Boolean,
        hasReload: Boolean,
        pageTag: String,
        lastPageTag: String
    ) {
        RxBus.getDefault()
            .post(
                CGetUrlForServerBean(
                    server_link,
                    linkUrl,
                    needColse,
                    hasReload,
                    pageTag,
                    lastPageTag
                )
            )
    }

    fun onJuggleItemClick(
        item: MHomeDiyBean.MHomeDataItem,
        type: String = "",
        pos: Int = -1,
        posString: String = ""
    ) {
        SLSLogUtils.instance.sendLogJuggleClick(
            item.pageKey,
            type,
            pos,
            posString,
            item.id,
            ""
        )

        if (item.app_path != null && !StringUtils.instance.isEmpty(item.app_path)) {
            RouterManager.instance.gotoNativeApp(item.app_path, item.pageTag)
        }
        if (item.copy_text != null && !StringUtils.instance.isEmpty(item.copy_text)) {
            StringUtils.instance.doCopy(item.copy_text)
        }
        if (item.toast != null && !StringUtils.instance.isEmpty(item.toast)) {
            ToastUtils.showShort(item.toast)
        }
        if (item.dialog_key != null && !StringUtils.instance.isEmpty(item.dialog_key)) {
            openDialog(item.dialog_key, item.pageTag)
        }
        var hasClose = false
        var hasReload = false

        var arrayString = item.funs
        for (index in arrayString.indices) {
            var funs = arrayString[index]
            when (funs) {
                "close_webview" -> hasClose = true
                "reload_webview" -> hasReload = true
            }
        }

        if (item.server_link != null && !StringUtils.instance.isEmpty(item.server_link)) {
            doServerGet(
                item.server_link,
                item.link,
                hasClose,
                hasReload,
                item.pageTag,
                item.lastPageTag
            )
        } else {
            if (item.link != null && !TextUtils.isEmpty(item.link)) {
                RouterManager.instance.gotoWebviewActivity(item.link)
            }
            if (hasClose) {
                if (hasReload) {
                    // 关闭当前页面并刷新上一个页面
                    reloadPage(item.lastPageTag)
                    closePage(item.pageTag)
                } else {
                    // 关闭当前页面
                    closePage(item.pageTag)
                }
            } else {
                // 刷新当前页面
                if (hasReload) {
                    reloadPage(item.pageTag)
                }
            }
        }
    }
}