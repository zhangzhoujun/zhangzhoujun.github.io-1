package com.qm.module_umshare.ui.share

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.module_umshare.BR
import com.qm.module_umshare.R
import com.qm.module_umshare.databinding.UmActSahreMainBinding
import com.umeng.socialize.UMShareAPI


/**
 * @ClassName ShareMainActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/19 2:14 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.UMShare.SHARE_MAIN)
class ShareMainActivity : JYActivity<UmActSahreMainBinding, ShareMainViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.um_act_sahre_main
    }

    private fun getBind(): UmActSahreMainBinding {
        return binding as UmActSahreMainBinding
    }

    private fun getVm(): ShareMainViewModel {
        return viewModel as ShareMainViewModel
    }

    override fun initData() {
        super.initData()
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        getBind().recyclerview.layoutManager = GridLayoutManager(this, 4)

        getVm().initShareView(this, getBind())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
}