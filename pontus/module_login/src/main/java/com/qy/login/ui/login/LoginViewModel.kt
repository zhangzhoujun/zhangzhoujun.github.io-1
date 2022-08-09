package com.qy.login.ui.login

import android.app.Activity
import android.app.Application
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.dim.library.base.AppManager
import com.dim.library.binding.command.BindingAction
import com.dim.library.binding.command.BindingCommand
import com.dim.library.bus.RxBus
import com.dim.library.bus.RxSubscriptions
import com.dim.library.utils.DLog
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.BaseBean
import com.qm.lib.base.Constants
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.*
import com.qm.lib.http.BaseService
import com.qm.lib.http.RetrofitClient
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.JYMMKVManager
import com.qm.lib.utils.SLSLogUtils
import com.qm.lib.utils.StringUtils
import com.qm.lib.widget.input.UserInputChangeMessage
import com.qm.lib.widget.piccode.AppPicCheckDialog
import com.qy.login.entity.CLoginBean
import com.qy.login.entity.CThirdAuth
import com.qy.login.entity.MLoginResultBean
import com.qy.login.entity.MThirdAuth
import com.qy.login.http.LoginService
import com.qy.login.widget.input.InputOption
import com.umeng.analytics.MobclickAgent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * @ClassName LoginViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/30 5:36 PM
 * @Version 1.0
 */
class LoginViewModel(application: Application) : BaseAppViewModel(application) {
    private var textSubscribe: Disposable? = null
    private var mTimer: CountDownTimer? = null
    private lateinit var mActivity: Activity

    var firstStep: ObservableBoolean = ObservableBoolean(true)

    fun init(activity: Activity) {
        mActivity = activity
        addTextChangeMessage()
    }

    // 是否可以发送，点击
    var canSend = ObservableBoolean(false)

    var canInvit = ObservableBoolean(false)

    var doesAgree = ObservableBoolean(true)

    // 手机输入框 的 内容
    var mobileOptions: ObservableField<InputOption> = ObservableField<InputOption>()

    // 验证码输入框 的 内容
    var codeOptions: ObservableField<InputOption> = ObservableField<InputOption>()

    var invitationOptions: ObservableField<InputOption> = ObservableField<InputOption>()

    private var countyCodeArr = ArrayList<String>()

    override fun onCreate() {
        super.onCreate()
        initData()

        AppManager.getAppManager().finishAllOtherActivity()
    }

    private fun initData() {
        val mobileOption = InputOption()
        mobileOption.inputEditHintString = "请输入手机号"
        mobileOption.isShowLeft = false
        mobileOption.leftDes = "+86 ▾"
        mobileOptions.set(mobileOption)

        val codeOption = InputOption()
        codeOption.isShowLeft = false
        codeOption.leftDes = ""
        codeOption.inputEditHintString = "请输入验证码"
        codeOption.inputRightString = "获取验证码"
        codeOptions.set(codeOption)

        val invitationOption = InputOption()
        invitationOption.isShowLeft = false
        invitationOption.leftDes = ""
        invitationOption.inputEditHintString = "请输入邀请码"
        invitationOptions.set(invitationOption)
    }

    var onGetSmsClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            SLSLogUtils.instance.sendLogClick("LoginActivity", "", "GET_SMS")
            DLog.d("LoginViewModel", "onGetSmsClick")
            val mobile = mobileOptions.get()!!.inputEditString
            if (!checkPhone(mobile)) {
                return
            }
            gotoPicCheckOldPhoneActivity(mobile)
        }
    })

    var onLeftDesClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
//            DLog.d("AAAA", "onLeftClick")
//            val builder = AlertDialog.Builder(mActivity)
//            builder.setItems(countyCodeArr, DialogInterface.OnClickListener { arg0, arg1 ->
//                DLog.d("AAA", "arg1 = $arg1")
//                mobileOptions.get()!!.leftDes = countyCodeArr.get(arg1)
//            })
//            builder.create().show()
        }
    })

    fun onInvitationClick(view: View?) {
        if (!doesCanInvate(true)) {
            return
        }
        DLog.d("LoginViewModel", "onInvitationClick")
        SLSLogUtils.instance.sendLogClick("LoginActivity", "", "INVITATION")
        val service = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            service.sendSaveUserInfo(getRequestBody(BaseBean(CUserInfoBean(invitationCode = invitationOptions.get()!!.inputEditString)))),
            object : AppObserver<BaseResultBean<Any>>() {
                override fun onSuccess(picCodeBean: BaseResultBean<Any>) {
                    if (picCodeBean.doesSuccess()) {
                        getUserInfo()
                    } else {
                        ToastUtils.showShort(picCodeBean.errMsg)
                    }
                }
            })
    }

    override fun onGetUserInfoBack(userBean: MUserBean?) {
        DLog.d("USERINFO", "onGetUserInfoBack")
        JYMMKVManager.instance.setAutoLogin(true)
        MobclickAgent.onProfileSignIn(LocalUserManager.instance.getUserId())
        RouterManager.instance.gotoMainActivity(JYComConst.HOME_CHOSE_TAB_HOME)
        finish()
    }

    fun onAgreeClick(view: View?) {
        SLSLogUtils.instance.sendLogClick("LoginActivity", "", "AGREE")
        DLog.d("LoginViewModel", "onAgreeClick")
        doesAgree.set(!doesAgree.get())
        onTextChanged()
    }

    private fun onTextChanged() {
        canSend.set(doesCanClick(false))
        canInvit.set(doesCanInvate(false))
    }

    // 图形验证码
    private fun gotoPicCheckOldPhoneActivity(mobile: String) {
        try {
            val dialog =
                AppPicCheckDialog(
                    mActivity, mobile,
                    AppPicCheckDialog.GosPicCheckDialogListener { key, code ->
                        DLog.d("AppPicCheckDialogResult success key = $key")
                        DLog.d("AppPicCheckDialogResult success code = $code")
                        DLog.d("图片验证码校验通过")
                        //图片验证码校验通过
                        doGetSms(mobile, code, key)
                    })
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun doGetSms(
        mobile: String,
        imgCode: String,
        imgKey: String
    ) {
        val service = RetrofitClient.getInstance().create(BaseService::class.java)

        RetrofitClient.getInstance().execute(
            service.getSms(getRequestBody(BaseBean(CSendSms(mobile, imgKey, imgCode)))),
            object : AppObserver<BaseResultBean<Any>>() {
                override fun onSuccess(picCodeBean: BaseResultBean<Any>) {
                    if (picCodeBean.doesSuccess()) {
                        startCountDown()
                        ToastUtils.showShort("短信验证码已发送")
                    } else {
                        ToastUtils.showShort(picCodeBean.errMsg)
                    }
                }
            })
    }

    private fun doesCanInvate(toast: Boolean): Boolean {
        if (TextUtils.isEmpty(invitationOptions.get()!!.inputEditString)) {
            if (toast) {
                ToastUtils.showShort("请输入邀请码")
            }
            return false
        }
        return true
    }


    private fun doesCanClick(toast: Boolean): Boolean {
        if (!doesAgree.get()) {
            return false
        }
        if (TextUtils.isEmpty(mobileOptions.get()!!.inputEditString)) {
            if (toast) {
                ToastUtils.showShort("手机号不能为空")
            }
            return false
        }
        if (TextUtils.isEmpty(codeOptions.get()!!.inputEditString)) {
            if (toast) {
                ToastUtils.showShort("验证码不能为空")
            }
            return false
        }
        return true
    }

    fun onBackToFitstStep(view: View?) {
        firstStep.set(true)
    }

    fun loginClick(view: View?) {
        if (!doesCanClick(true)) {
            return
        }

        if (!checkPhone(mobileOptions.get()!!.inputEditString)) {
            return
        }

        SLSLogUtils.instance.sendLogClick("LoginActivity", "", "LOGIN")

        val service = RetrofitClient.getInstance().create(LoginService::class.java)

        RetrofitClient.getInstance().execute(
            service.doLogin(
                getRequestBody(
                    BaseBean(
                        CLoginBean(
                            codeOptions.get()!!.inputEditString,
                            mobileOptions.get()!!.inputEditString
                        )
                    )
                )
            ),
            object : AppObserver<BaseResultBean<MLoginResultBean>>() {
                override fun onSuccess(loginResult: BaseResultBean<MLoginResultBean>) {
                    if (loginResult.doesSuccess()) {
                        JYMMKVManager.instance.setAppConfigData("")
                        JYMMKVManager.instance.setUserToken(loginResult.data.authToken)
                        JYMMKVManager.instance.setUserId(loginResult.data.id.toString())
                        RetrofitClient.getInstance().resetRetrofitClient()
                        // 填邀请码
                        if (loginResult.data.refereesId == 0L) {
                            SLSLogUtils.instance.sendLogClick(
                                "LoginActivity",
                                "",
                                "NEED_INVITATION"
                            )
                            JYMMKVManager.instance.setAutoLogin(false)
                            firstStep.set(false)
                        }
                        // 去首页
                        else {
                            getUserInfo()
                        }
                    } else {
                        ToastUtils.showShort(loginResult.errMsg)
                    }
                }
            })
    }

    override fun onGetAppConfig(bean: MAppConfigBean?) {
        super.onGetAppConfig(bean)
        getUserInfo()
    }

    fun checkPhone(phone: String?): Boolean {
        var result = true
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请先输入手机号")
            result = false
            return result
        }

        if (!StringUtils.instance.isNumeric(phone)) {
            ToastUtils.showShort("请输入正确的手机号")
            result = false
            return result
        }
        return result
    }

    private fun addTextChangeMessage() {
        textSubscribe = RxBus.getDefault().toObservable(UserInputChangeMessage::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onTextChanged() }
        //将订阅者加入管理站
        RxSubscriptions.add(textSubscribe)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (textSubscribe != null) {
            RxSubscriptions.remove(textSubscribe)
            textSubscribe = null
        }
        stopTimer()
    }

    private fun startCountDown() {
        stopTimer()
        mTimer = object : CountDownTimer(((Constants.COUNT_DOWN_NUM * 1000).toLong()), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                DLog.d("startCountDown onTick --> $millisUntilFinished")
                val count = (millisUntilFinished / 1000).toInt()
                if (count > 0) {
                    codeOptions.get()!!.isInputRightCount = true
                    codeOptions.get()!!.inputRightString = count.toString() + "s"
                } else {
                    codeOptions.get()!!.inputRightString = "获取验证码"
                    codeOptions.get()!!.isInputRightCount = false
                }
            }

            override fun onFinish() {
                DLog.d("startCountDown onFinish --> ")
                codeOptions.get()!!.inputRightString = "获取验证码"
                codeOptions.get()!!.isInputRightCount = false
            }
        }
        (mTimer as CountDownTimer).start()
    }

    private fun stopTimer() {
        if (mTimer != null) {
            mTimer!!.cancel()
            mTimer = null
        }
    }

    fun getThirdUser(access_key: String, mobile: String, sign: String) {
        val service = RetrofitClient.getInstance().create(LoginService::class.java)

        RetrofitClient.getInstance()
            .execute(service.getUserThirdAuth(
                getRequestBody(
                    BaseBean(
                        CThirdAuth(
                            access_key,
                            mobile,
                            sign
                        )
                    )
                )
            ),
                object : AppObserverNoDialog<BaseResultBean<MThirdAuth>>() {
                    override fun onSuccess(o: BaseResultBean<MThirdAuth>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            JYMMKVManager.instance.setUserToken(o.data.authToken)
                            JYMMKVManager.instance.setUserId(o.data.id.toString())
                            RetrofitClient.getInstance().resetRetrofitClient()
                            JYMMKVManager.instance.setAutoLogin(true)

                            getUserInfo()
                        }
                    }
                })
    }
}