package com.qm.module_juggle.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.SLSLogUtils.Companion.instance
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeActJuggleBinding

/**
 * @ClassName JuggleActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/30 2:22 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.JuggleAct.JUGGLE_INDEX)
class JuggleActivity : JYActivity<HomeActJuggleBinding, JuggleActViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE)
    var page_key: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_TAG)
    var page_tag: String = ""

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.home_act_juggle
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)

        if (TextUtils.isEmpty(page_key)) {
            ToastUtils.showShort("数据错误，请重试")
            finish()
        } else {
            val homeFra =
                ARouter.getInstance().build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_INDEX)
                    .withString(JYComConst.OPEN_CUSTOM_PAGE, page_key)
                    .withString(JYComConst.OPEN_CUSTOM_PAGE_TAG, page_tag)
                    .navigation() as Fragment
            val transaction =
                supportFragmentManager.beginTransaction()
            transaction.add(R.id.frameLayout, homeFra)
            transaction.commitAllowingStateLoss()
        }
    }

    override fun onPauseLog() {
        instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONPAUSE"
        )
    }

    override fun onCreateLog() {
        instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONCREATE"
        )
    }

    override fun onResumeLog() {
        instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONRESUME"
        )
    }

    override fun onDestoryLog() {
        instance.sendLogLoad(
            javaClass.simpleName,
            page_key,
            "ONDESTORY"
        )
    }
}