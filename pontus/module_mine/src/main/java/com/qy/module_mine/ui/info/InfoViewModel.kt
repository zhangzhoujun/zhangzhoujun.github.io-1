package com.qy.module_mine.ui.info

import android.app.Application
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.dim.library.binding.command.BindingAction
import com.dim.library.binding.command.BindingCommand
import com.dim.library.bus.RxBus
import com.dim.library.utils.DLog
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.BaseBean
import com.qm.lib.base.LocalUserManager
import com.qm.lib.base.PictureViewModel
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.entity.CUserInfoBeanNew
import com.qm.lib.entity.MUserBean
import com.qm.lib.http.BaseService
import com.qm.lib.http.RetrofitClient
import com.qm.lib.message.RefreshUserInfo
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.widget.input.JYInputOptions
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qy.module_mine.R

/**
 * @ClassName InfoViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/2 5:04 PM
 * @Version 1.0
 */
class InfoViewModel(application: Application) : PictureViewModel(application) {

    var showInfo: ObservableBoolean = ObservableBoolean(true)

    var mHeadUrl: ObservableField<String> = ObservableField()
    var mNickName: ObservableField<String> = ObservableField()

    // 输入框 的 内容
    var nameOptions: ObservableField<JYInputOptions> = ObservableField<JYInputOptions>()

    override fun onCreate() {
        super.onCreate()

        if (LocalUserManager.instance.getUser() == null) {
            getUserInfo()
        } else {
            initInfo()
        }
        initTitle()
    }

    private fun initInfo() {
        mHeadUrl.set(LocalUserManager.instance.getUserHead())
        mNickName.set(LocalUserManager.instance.getUserNickname())
        initViewData()
    }

    override fun onGetUserInfoBack(userBean: MUserBean?) {
        DLog.d("USERINFO", "onGetUserInfoBack")
        initInfo()
    }

    fun initTitle() {
        val option = JYToolbarOptions()
        if (showInfo.get()) {
            option.titleString = "个人信息"
        } else {
            option.titleString = "设置昵称"
        }
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    private fun initViewData() {
        val codeOption = JYInputOptions()
        codeOption.isShowLeft = false
        codeOption.inputEditHintString = "请输入昵称"
        codeOption.inputEditString = LocalUserManager.instance.getUserNickname()
        nameOptions.set(codeOption)
    }

    override fun onPicUploadSuccess(key: String, path: String) {
        sendModify(key, path)
    }

    var onModifyCLick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            var name = nameOptions.get()!!.inputEditString
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort("请先输入昵称")
                return
            }
            SLSLogUtils.instance.sendLogClick("MyInfoActivity", "", "SEND_CHANGE_NICKNAME")
            sendModifyNickname()
        }
    })

    var onHeadClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("MyInfoActivity", "", "CHANGE_HEAD")
            onPictureClick()
        }
    })

    var onNicknameClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("MyInfoActivity", "", "CHANGE_NICKNAME")
            showInfo.set(false)
        }
    })

    var onEIDClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("MyInfoActivity", "", "EID")
            RouterManager.instance.gotoWebviewActivity(
                "http://eid.cn/knoweid/howtogeteid.html",
                "eID申领与开通"
            )
        }
    })

    override fun setOnBackClick() {
        if (!showInfo.get()) {
            showInfo.set(true)
        } else {
            super.setOnBackClick()
        }
    }

    private fun sendModify(path: String, head: String) {
        var nickName = nameOptions.get()!!.inputEditString.toString()
        val service = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            service.sendSaveUserInfo(getRequestBody(CUserInfoBeanNew(nickName, head))),
            object : AppObserver<BaseResultBean<Any>>() {
                override fun onSuccess(picCodeBean: BaseResultBean<Any>) {
                    if (picCodeBean.doesSuccess()) {
                        LocalUserManager.instance.getUser()!!.avatar = head
                        RxBus.getDefault().post(RefreshUserInfo())
                        mHeadUrl.set(head)
                        ToastUtils.showShort("修改成功")
                    } else {
                        ToastUtils.showShort(picCodeBean.errMsg)
                    }
                }
            })
    }

    private fun sendModifyNickname() {
        var head = mHeadUrl.get().toString()
        var nickName = nameOptions.get()!!.inputEditString.toString()
        val service = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            service.sendSaveUserInfo(getRequestBody(CUserInfoBeanNew(nickName, head))),
            object : AppObserver<BaseResultBean<Any>>() {
                override fun onSuccess(picCodeBean: BaseResultBean<Any>) {
                    if (picCodeBean.doesSuccess()) {
                        LocalUserManager.instance.getUser()!!.nick_name = nickName
                        RxBus.getDefault().post(RefreshUserInfo())
                        mNickName.set(nickName)
                        ToastUtils.showShort("修改成功")
                    } else {
                        ToastUtils.showShort(picCodeBean.errMsg)
                    }
                }
            })
    }
}