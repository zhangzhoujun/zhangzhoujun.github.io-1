package com.qy.module_mine.ui.set

import android.app.Application
import android.os.SystemClock
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.dim.library.binding.command.BindingAction
import com.dim.library.binding.command.BindingCommand
import com.dim.library.utils.DLog
import com.dim.library.utils.VersionUtils
import com.qm.lib.base.LocalUserManager
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qy.module_mine.R

/**
 * @ClassName SetViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/3 4:47 PM
 * @Version 1.0
 */
class SetViewModel(application: Application) : ToolbarViewModel(application) {

    var showSet: ObservableBoolean = ObservableBoolean(true)

    var mMobile: ObservableField<String> = ObservableField()

    var mVersion: ObservableField<String> = ObservableField()

    var mVersionName: ObservableField<String> = ObservableField()

    var showLogout: ObservableBoolean = ObservableBoolean(false)

    var mAboutName: ObservableField<String> = ObservableField()

    private var appName = ""

    fun initTitle() {
        val option = JYToolbarOptions()
        if (showSet.get()) {
            option.titleString = "设置"
        } else {
            option.titleString = mAboutName.get()
        }
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    override fun onCreate() {
        super.onCreate()
        appName = VersionUtils.getAppName(getApplication())
        mAboutName.set("关于${appName}")

        initTitle()

        mVersion.set("版本号：V " + VersionUtils.getAppVersionName(getApplication()))
        mVersionName.set("$appName V " + VersionUtils.getAppVersionName(getApplication()))
        mMobile.set(LocalUserManager.instance.getUserMobile())
    }

    var onAboutClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("SetActivity", "", "ABOUT")
            showSet.set(false)
        }
    })

    var onFankuiClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("SetActivity", "", "FEEDBACK")
            RouterManager.instance.gotoFeedbackActivity()
        }
    })

    var onLogoutClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            DLog.d("onLoginOutClick")
            SLSLogUtils.instance.sendLogClick("SetActivity", "", "LOGOUT")
            showLogout.set(!showLogout.get())
        }
    })

    var onXieyiClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("SetActivity", "", "XIEYI")
            RouterManager.instance.gotoYonghuXieyiActivity()
        }
    })

    var onYinsiClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("SetActivity", "", "YINSI")
            RouterManager.instance.gotoYinsizhengceActivity()
        }
    })

    fun logout() {
        LocalUserManager.instance.logonOut()
        RouterManager.instance.gotoLoginActivity()
    }

    fun onIconClick(view: View) {
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()
        if (mHits[0] >= (SystemClock.uptimeMillis() - duration)) {
            if (mHits.size == 6) {
                RouterManager.instance.gotoQRActivity()
            }
        }
    }

    val counts: Int = 6
    val duration: Long = 3 * 1000
    val mHits = LongArray(counts)
}