package com.qm.module_juggle.ui.liandong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.qm.lib.base.JYFragment
import com.qm.lib.utils.SLSLogUtils
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeFraLiandongBinding
import com.qm.module_juggle.entity.MHomeDataBean

/**
 * @ClassName HomeHotFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/20 11:41 AM
 * @Version 1.0
 */
class HomeLDFragment : JYFragment<HomeFraLiandongBinding, HomeLDViewModel>() {

    private var index = -1
    private var pageKey = ""

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.home_fra_liandong
    }

    override fun initData() {
        super.initData()

        getBind().recyclerView.layoutManager = GridNoScrollLayoutManager(context, 5)

        val lists =
            arguments!!.getSerializable("lsit") as ArrayList<MHomeDataBean.MHomeDataItemData>
        index = arguments!!.getInt("pos", -1)
        pageKey = arguments!!.getString("pageKey", "")
        if (lists.isNotEmpty()) {
            getVm().initData(lists, index)
        }
    }

    private fun getVm(): HomeLDViewModel {
        return viewModel as HomeLDViewModel
    }

    private fun getBind(): HomeFraLiandongBinding {
        return binding as HomeFraLiandongBinding
    }

    override fun onPauseLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            pageKey,
            "ONPAUSE",
            index
        )
    }

    override fun onCreateLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            pageKey,
            "ONCREATE",
            index
        )
    }

    override fun onResumeLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            pageKey,
            "ONRESUME",
            index
        )
    }

    override fun onDestoryLog() {
        SLSLogUtils.instance.sendLogLoad(
            javaClass.simpleName,
            pageKey,
            "ONDESTORY",
            index
        )
    }
}