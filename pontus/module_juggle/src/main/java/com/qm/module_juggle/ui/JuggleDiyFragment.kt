package com.qm.module_juggle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qm.lib.base.JYFragment
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.JYComConst
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.JuggleFraDiyBinding

/**
 * @ClassName JuggleDiyFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 3/3/21 11:37 AM
 * @Version 1.0
 */
@Route(path = RouterFragmentPath.JuggleFra.JUGGLE_FRA_DIY)
class JuggleDiyFragment : JYFragment<JuggleFraDiyBinding, JuggleDiyViewModel>() {

    private val TAG = "JuggleDiyFragment"

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE)
    var page_key: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_TAG)
    var page_tag: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_TAG_LAST)
    var last_page_tag: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_SERVER_DATA)
    var page_server_data: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_COUNT)
    var page_count: Int = 1

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.juggle_fra_diy
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)
        if (page_key == null) {
            return
        }
        if (page_server_data == null) {
            page_server_data = ""
        }
        getVm().initData(
            page_key,
            getBind().root,
            context!!,
            page_tag,
            page_count,
            last_page_tag,
            page_server_data
        )
    }

    private fun getVm(): JuggleDiyViewModel {
        return viewModel as JuggleDiyViewModel
    }

    private fun getBind(): JuggleFraDiyBinding {
        return binding as JuggleFraDiyBinding
    }
}