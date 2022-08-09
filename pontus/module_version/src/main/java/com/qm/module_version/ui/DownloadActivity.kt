package com.qm.module_version.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.DLog
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qm.module_version.BR
import com.qm.module_version.R
import com.qm.module_version.databinding.VersionDownloadBinding
import kotlinx.android.synthetic.main.version_download.*

/**
 * @ClassName VersionActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/22 11:34 AM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Version.VERSION_DOWNLOAD)
class DownloadActivity : JYActivity<VersionDownloadBinding, DownloadViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_VERSION)
    var version: String = "" // icon

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_URL)
    var downUrl: String = ""

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_DES)
    var des: String = ""

    @JvmField
    @Autowired(name = JYComConst.VERSION_DOWNLOAD_FOURCE)
    var fource: Boolean = true

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.version_download
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)
        setFinishOnTouchOutside(false)
        DLog.d("DownloadActivity","icon    = $version")
        DLog.d("DownloadActivity","des     = $des")
        DLog.d("DownloadActivity","downUrl = $downUrl")

        getBind().des.text = des
        getBind().icon.setImageUrl(version)

        getVm().initParam(version, des, downUrl, fource, getBind().downProgress)
    }

    private fun getVm(): DownloadViewModel {
        return viewModel as DownloadViewModel
    }

    private fun getBind(): VersionDownloadBinding {
        return binding as VersionDownloadBinding
    }

    override fun onBackPressed() {
        if (fource) {
            return
        }
        super.onBackPressed()
    }
}