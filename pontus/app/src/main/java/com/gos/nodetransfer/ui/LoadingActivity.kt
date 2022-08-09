package com.gos.nodetransfer.ui

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.gos.nodetransfer.BR
import com.gos.nodetransfer.R
import com.gos.nodetransfer.databinding.QmActLoadingBinding
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterManager
import com.qmuiteam.qmui.span.QMUITouchableSpan
import com.qy.qysdk.manager.QYManager


/**
 * @ClassName LoadingActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 4:54 PM
 * @Version 1.0
 */
class LoadingActivity : JYActivity<QmActLoadingBinding, LoadingViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.qm_act_loading
    }

    override fun initData() {
        super.initData()

        showFullScreen(true)

        QYManager.getInstance().init(this)

        initBottomDes()

        getVm().setActivity(this, getBind().root, getBind().loadingAdFra)
    }

    private fun getVm(): LoadingViewModel {
        return viewModel as LoadingViewModel
    }

    private fun getBind(): QmActLoadingBinding {
        return binding as QmActLoadingBinding
    }

    private fun initBottomDes() {
        getBind().content.setMovementMethodDefault()
        getBind().content.setNeedForceEventToParent(true)
        getBind().content.text = generateSp(
            getBind().content,
            resources.getString(R.string.qm_dialog_toast)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun generateSp(tv: TextView, text: String): SpannableString? {
        val highlight1 = "《用户协议》"
        val highlight2 = "《隐私政策》"
        val sp = SpannableString(text)
        var start = 0
        var end: Int
        var index: Int
        while (text.indexOf(highlight1, start).also { index = it } > -1) {
            end = index + highlight1.length
            sp.setSpan(object : QMUITouchableSpan(
                tv,
                R.attr.app_skin_span_loading_color,
                R.attr.app_skin_span_loading_color,
                R.attr.app_skin_span_normal_bg_color,
                R.attr.app_skin_span_pressed_bg_color
            ) {
                override fun onSpanClick(widget: View) {
                    RouterManager.instance.gotoYonghuXieyiActivity()
                }
            }, index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sp.setSpan(StyleSpan(Typeface.NORMAL), index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end
        }
        start = 0
        while (text.indexOf(highlight2, start).also { index = it } > -1) {
            end = index + highlight2.length
            sp.setSpan(object : QMUITouchableSpan(
                tv,
                R.attr.app_skin_span_loading_color,
                R.attr.app_skin_span_loading_color,
                R.attr.app_skin_span_normal_bg_color,
                R.attr.app_skin_span_pressed_bg_color
            ) {
                override fun onSpanClick(widget: View) {
                    RouterManager.instance.gotoYinsizhengceActivity()
                }
            }, index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sp.setSpan(StyleSpan(Typeface.NORMAL), index, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end
        }
        return sp
    }
}