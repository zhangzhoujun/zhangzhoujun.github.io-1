package com.qy.login.ui.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterManager
import com.qmuiteam.qmui.span.QMUITouchableSpan
import com.qy.login.BR
import com.qy.login.R
import com.qy.login.databinding.LoginActMainBinding

/**
 * @ClassName LoginActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/30 8:19 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Login.LOGIN_LOGIN)
class LoginActivity : JYActivity<LoginActMainBinding, LoginViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.login_act_main
    }


    private fun getBind(): LoginActMainBinding {
        return binding as LoginActMainBinding
    }

    private fun getVm(): LoginViewModel {
        return viewModel as LoginViewModel
    }

    override fun initData() {
        super.initData()
        getVm().init(this@LoginActivity)
        initBottomDes()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        // 获取 国金生态过来的免登数据
        if (intent != null) {
            var access_key = ""
            var mobile = ""
            var sign = ""
            if (intent.hasExtra("access_key")) {
                access_key = intent.getStringExtra("access_key")
            }
            if (intent.hasExtra("mobile")) {
                mobile = intent.getStringExtra("mobile")
            }
            if (intent.hasExtra("sign")) {
                sign = intent.getStringExtra("sign")
            }
            if (!TextUtils.isEmpty(access_key) && !TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(
                    sign
                )
            ) {
                getVm().getThirdUser(access_key, mobile, sign)
                return
            }
        }
    }

    private fun initBottomDes() {
        getBind().regiestBottom.setMovementMethodDefault()
        getBind().regiestBottom.setNeedForceEventToParent(true)
        getBind().regiestBottom.text = generateSp(
            getBind().regiestBottom,
            resources.getString(R.string.login_bottom_des)
        )
    }

    private fun generateSp(tv: TextView, text: String): SpannableString? {
        val highlight1 = "\"用户协议\""
        val highlight2 = "\"隐私政策\""
        val sp = SpannableString(text)
        var start = 0
        var end: Int
        var index: Int
        while (text.indexOf(highlight1, start).also { index = it } > -1) {
            end = index + highlight1.length
            sp.setSpan(object : QMUITouchableSpan(
                tv,
                R.attr.app_skin_span_normal_text_color,
                R.attr.app_skin_span_pressed_text_color,
                R.attr.app_skin_span_normal_bg_color,
                R.attr.app_skin_span_pressed_bg_color
            ) {
                override fun onSpanClick(widget: View) {
                    RouterManager.instance.gotoYonghuXieyiActivity()
                }
            }, index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sp.setSpan(StyleSpan(Typeface.BOLD), index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end
        }
        start = 0
        while (text.indexOf(highlight2, start).also { index = it } > -1) {
            end = index + highlight2.length
            sp.setSpan(object : QMUITouchableSpan(
                tv,
                R.attr.app_skin_span_normal_text_color,
                R.attr.app_skin_span_pressed_text_color,
                R.attr.app_skin_span_normal_bg_color,
                R.attr.app_skin_span_pressed_bg_color
            ) {
                override fun onSpanClick(widget: View) {
                    RouterManager.instance.gotoYinsizhengceActivity()
                }
            }, index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sp.setSpan(StyleSpan(Typeface.BOLD), index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end
        }
        return sp
    }
}