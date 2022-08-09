package com.qm.module_version.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qm.module_version.BR
import com.qm.module_version.R
import com.qm.module_version.databinding.VersionUpdateBinding

/**
 * @ClassName VersionActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/22 11:34 AM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Version.VERSION_UPDATE)
class VersionActivity : JYActivity<VersionUpdateBinding, VersionViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_VERSION)
    var version: String = ""

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_URL)
    var downUrl: String = ""

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_DES)
    var des: String = ""

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_FOURCE)
    var fource: Boolean = false

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.version_update
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)
        setFinishOnTouchOutside(false)
        getVm().initParam(version, des, downUrl, fource, getBind().downProgress)
    }

    private fun getVm(): VersionViewModel {
        return viewModel as VersionViewModel
    }

    private fun getBind(): VersionUpdateBinding {
        return binding as VersionUpdateBinding
    }

    override fun onBackPressed() {
        if (fource) {
            return
        }
        super.onBackPressed()
    }
}