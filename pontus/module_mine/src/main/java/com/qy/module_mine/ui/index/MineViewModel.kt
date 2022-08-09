package com.qy.module_mine.ui.index

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import com.dim.library.utils.DLog
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.MUserBean
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.StringUtils

/**
 * @ClassName MineViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/2 3:43 PM
 * @Version 1.0
 */
class MineViewModel(application: Application) : BaseAppViewModel(application) {


    var mHeadUrl: ObservableField<String> = ObservableField()
    var mNickName: ObservableField<String> = ObservableField()
    var mMobile: ObservableField<String> = ObservableField()
    var mCode: ObservableField<String> = ObservableField()
    var mPublicKey: ObservableField<String> = ObservableField()
    var mVipName: ObservableField<String> = ObservableField()

    override fun onCreate() {
        super.onCreate()

        if (LocalUserManager.instance.getUser() == null) {
            getUserInfo()
        } else {
            initInfo()
        }
    }

    private fun initInfo() {
        mHeadUrl.set(LocalUserManager.instance.getUserHead())
        mNickName.set(LocalUserManager.instance.getUserNickname())
        mMobile.set(LocalUserManager.instance.getUserMobile())
        mCode.set("邀请码：" + LocalUserManager.instance.getUserNickname())
//        mPublicKey.set("ID：" + LocalUserManager.instance.getUserPublickey())
        mVipName.set("创世纪会员")
    }

    override fun onGetUserInfoBack(userBean: MUserBean?) {
        DLog.d("USERINFO", "onGetUserInfoBack")
        initInfo()
    }

    fun onSetClick(view: View?) {
        DLog.d("MineViewModel", "onSetClick")
        RouterManager.instance.gotoMineSetActivity()
    }

    fun onInfoClick(view: View?) {
        DLog.d("MineViewModel", "onInfoClick")
        RouterManager.instance.gotoInfoModifyActivity()
    }

    fun onCopyClick(view: View?) {
        DLog.d("MineViewModel", "onCopyClick")
        StringUtils.instance.doCopy(mCode.get()!!)
    }

    fun onQuanyiClick(view: View?) {
        DLog.d("MineViewModel", "onQuanyiClick")

    }
}