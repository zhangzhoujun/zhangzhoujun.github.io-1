package com.gos.nodetransfer.ui.guide

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.gos.nodetransfer.BR
import com.gos.nodetransfer.R
import com.gos.nodetransfer.databinding.QmActGuideBinding
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.JYMMKVManager
import com.qm.lib.utils.SystemUtil

/**
 * @ClassName GuideActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/14 9:44 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Main.MAIN_GUIDE)
class GuideActivity : JYActivity<QmActGuideBinding, GuideViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.qm_act_guide
    }

    private fun getBind(): QmActGuideBinding {
        return binding as QmActGuideBinding
    }

    override fun initData() {
        super.initData()

        var listData = ArrayList<Int>()
        listData.add(R.mipmap.guide_0)
        listData.add(R.mipmap.guide_1)
        listData.add(R.mipmap.guide_2)
        listData.add(R.mipmap.guide_3)
        var adapter =
            GuideItemAdapter(this, listData, object : GuideItemAdapter.OnGuideItemClickListener {
                override fun onGuideItemClick(position: Int) {
                    if (position == listData.size - 1) {
                        JYMMKVManager.instance.setFirstForVersion(
                            SystemUtil.getVersionName(this@GuideActivity), false
                        )
                        if (!JYMMKVManager.instance.isAutoLogin() || JYMMKVManager.instance.getToken()
                                .isEmpty()
                        ) {
                            RouterManager.instance.gotoLoginActivity()
                        } else {
                            RouterManager.instance.gotoMainActivity(JYComConst.HOME_CHOSE_TAB_HOME)
                        }
                        finish()
                    }
                }
            })
        getBind().banner.adapter = adapter
    }
}