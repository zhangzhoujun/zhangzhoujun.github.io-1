package com.qm.module_umshare.ui.dialog

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.module_umshare.BR
import com.qm.module_umshare.R
import com.qm.module_umshare.databinding.UmActOneDialogBinding

/**
 * @ClassName ShareMainActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/19 2:14 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.UMShare.SHARE_ONE_DIALOG)
class OnlyOneDialogActivity : JYActivity<UmActOneDialogBinding, OnlyOneDialogViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.um_act_one_dialog
    }

    private fun getBind(): UmActOneDialogBinding {
        return binding as UmActOneDialogBinding
    }

    private fun getVm(): OnlyOneDialogViewModel {
        return viewModel as OnlyOneDialogViewModel
    }

    override fun initData() {
        super.initData()
        getBind().recyclerview.layoutManager = LinearLayoutManager(this)

        getVm().initDialog(getBind().iconBg,getBind().btCount)
    }
}