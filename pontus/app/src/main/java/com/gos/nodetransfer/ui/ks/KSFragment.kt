package com.gos.nodetransfer.ui.ks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.dim.library.utils.DLog
import com.gos.nodetransfer.BR
import com.gos.nodetransfer.R
import com.gos.nodetransfer.databinding.QmFraKsBinding
import com.qm.lib.base.JYFragment
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.JYUtils


/**
 * @ClassName KSFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/10 1:40 PM
 * @Version 1.0
 */
@Route(path = RouterFragmentPath.Main.MIAN_KS)
class KSFragment : JYFragment<QmFraKsBinding, KSViewModel>() {
    private val TAG = "KSFragment"

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.qm_fra_ks
    }

    private fun getVm(): KSViewModel {
        return viewModel as KSViewModel
    }

    override fun initData() {
        super.initData()
        var ksId = JYUtils.instance.getMetaDatByName(context!!, "GG_KS_CHANNEDID")
        DLog.d(TAG, "加载快手ID -> $ksId")

//        var showAllianceAD =
//            ShowAllianceAD(activity, childFragmentManager.beginTransaction(), R.id.ks_container)

//        getVm().setFragment(showAllianceAD, ksId)
    }

    private fun getBind(): QmFraKsBinding {
        return binding as QmFraKsBinding
    }

    override fun onResume() {
        super.onResume()
        DLog.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        DLog.d(TAG, "onPause")
    }


}