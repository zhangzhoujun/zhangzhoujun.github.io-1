package com.qm.module_juggle.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.DLog
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.*
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeActJuggleDialogBinding
import com.qm.module_juggle.entity.MHomeDataBean

/**
 * @ClassName JuggleActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/30 2:22 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.JuggleAct.JUGGLE_DIALOG_SHOW)
class JuggleDialogShowActivity : JYActivity<HomeActJuggleDialogBinding, JuggleActViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE)
    var page_key: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_DATA)
    var page_data: MHomeDataBean? = null

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_TAG)
    var page_tag: String = ""

    private var isCanBack = true

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.home_act_juggle_dialog
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)
        if (page_tag == null) {
            page_tag = ""
        }

        if (page_data == null) {
            ToastUtils.showShort("数据错误，请重试")
            finish()
        } else {
            var pageBean = page_data!!.page_base

            isCanBack = pageBean.mask_close

            DLog.d("initData", "isCanBack -> $isCanBack")

            getBind().bgLayout.setBackgroundColor(ColorUtils.instance.getColorForString(pageBean.mask_color))
            getBind().bgLayout.setOnClickListener { e -> if (pageBean.mask_close) onBackPressed() else null }

            var wid = ScreenUtils.getScreenWidth(this)
            var hei = ScreenUtils.getScreenHeight(this)

            var lp = getBind().frameLayout.layoutParams as RelativeLayout.LayoutParams
            lp.topMargin = hei * pageBean.offset_top / 100
            lp.leftMargin = wid * pageBean.percentage[0] / 100
            lp.rightMargin = wid * (100 - pageBean.percentage[1]) / 100

            getBind().frameLayout.layoutParams = lp

            val homeFra =
                ARouter.getInstance().build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_INDEX)
                    .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_DATA, page_data)
                    .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_TAG, page_tag)
                    .withSerializable(JYComConst.OPEN_CUSTOM_PAGE, page_key)
                    .navigation() as Fragment
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.add(R.id.frameLayout, homeFra)
            transaction.commitAllowingStateLoss()

            var aninRes =
                JYUtils.instance.getAnimByType(true, page_data!!.page_base.admission_animation)

            if (aninRes == 0) {
                getBind().frameLayout.visibility = View.VISIBLE
            } else {
                var showAnim = AnimationUtils.loadAnimation(this, aninRes)
                getBind().frameLayout.startAnimation(showAnim)
                getBind().frameLayout.visibility = View.VISIBLE
                showAnim!!.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        getBind().frameLayout.clearAnimation()
                    }

                    override fun onAnimationStart(animation: Animation?) {
                    }

                })
            }
        }
    }

    private fun getBind(): HomeActJuggleDialogBinding {
        return binding as HomeActJuggleDialogBinding
    }

    private var disAnim: Animation? = null
    private var isDoFinish = false

    override fun onBackPressed() {
        DLog.d("onBackPressed", "isCanBack -> $isCanBack")
        if (!isCanBack) {
            return
        }
        var aninRes =
            JYUtils.instance.getAnimByType(false, page_data!!.page_base.appearance_animation)
        if (aninRes == 0) {
            super.onBackPressed()
            return
        }

        if (isDoFinish) {
            return
        }
        if (disAnim == null) {
            isDoFinish = true
            getBind().frameLayout.clearAnimation()
            disAnim = AnimationUtils.loadAnimation(this, aninRes)
            getBind().frameLayout.startAnimation(disAnim)
            disAnim!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    isDoFinish = false
                    onBackPressed()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
            return
        }
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onPauseLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONPAUSE"
        )
    }

    override fun onCreateLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONCREATE"
        )
    }

    override fun onResumeLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONRESUME"
        )
    }

    override fun onDestoryLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONDESTORY"
        )
    }
}