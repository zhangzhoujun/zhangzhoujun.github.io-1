package com.qm.module_school.ui.index

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qm.lib.base.JYFragment
import com.qm.lib.entity.MAppConfigBean
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.JYComConst
import com.qm.module_school.BR
import com.qm.module_school.R
import com.qm.module_school.databinding.SchoolFraIndexBinding

/**
 * @ClassName SchoolIndexFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/3 4:26 PM
 * @Version 1.0
 */
@Route(path = RouterFragmentPath.School.SCHOOL_INDEX)
class SchoolIndexFragment : JYFragment<SchoolFraIndexBinding, SchoolIndexViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_ITEM)
    var page_item: MAppConfigBean.MAppConfigitem? = null

    @JvmField
    @Autowired(name = "tabTextAlign")
    var tabTextAlign: String = "left"

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.school_fra_index
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)

        if (tabTextAlign == "left") {
            getBind().bg.visibility = View.GONE
            getBind().title.visibility = View.GONE
            getBind().topStatus.visibility = View.VISIBLE
            getBind().schoolIndexTabLayout.visibility = View.GONE
            getBind().schoolIndexTabLayoutLeft.visibility = View.VISIBLE
        } else {
            getBind().bg.visibility = View.VISIBLE
            getBind().title.visibility = View.VISIBLE
            getBind().topStatus.visibility = View.GONE
            getBind().schoolIndexTabLayout.visibility = View.VISIBLE
            getBind().schoolIndexTabLayoutLeft.visibility = View.GONE
        }

        getVm().initView(
            this@SchoolIndexFragment,
            getBind().schoolIndexVp,
            if (tabTextAlign == "left") getBind().schoolIndexTabLayoutLeft else getBind().schoolIndexTabLayout,
            page_item, tabTextAlign
        )
    }

    private fun getVm(): SchoolIndexViewModel {
        return viewModel as SchoolIndexViewModel
    }

    private fun getBind(): SchoolFraIndexBinding {
        return binding as SchoolFraIndexBinding
    }
}