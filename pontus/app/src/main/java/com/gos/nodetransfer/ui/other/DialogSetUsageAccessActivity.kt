package com.gos.nodetransfer.ui.other

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import com.alibaba.android.arouter.facade.annotation.Route
import com.gos.nodetransfer.BR
import com.gos.nodetransfer.R
import com.gos.nodetransfer.databinding.QmDialogIbxAppBinding
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.JYActivity
import com.qm.lib.base.LocalUserManager
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.SLSLogUtils

/**
 * @ClassName VersionActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/22 11:34 AM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Main.MAIN_USAGE)
class DialogSetUsageAccessActivity : JYActivity<QmDialogIbxAppBinding, BaseAppViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.qm_dialog_ibx_app
    }

    override fun initData() {
        super.initData()
        setFinishOnTouchOutside(false)

        getBind().sure.setOnClickListener {
            SLSLogUtils.instance.sendLogLoad(
                "IBXAppActivity",
                "",
                "IBX_APP_ACCESS_DIALOG_SURE",
                -1,
                ""
            )
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivityForResult(intent, 1239)
        }

        getBind().cancel.setOnClickListener {
            SLSLogUtils.instance.sendLogLoad(
                "IBXAppActivity",
                "",
                "IBX_APP_ACCESS_DIALOG_CANCEL",
                -1,
                ""
            )
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1239) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val appOps: AppOpsManager =
                    getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                var mode = 0
                mode = appOps.checkOpNoThrow(
                    "android:get_usage_stats",
                    Process.myUid(),
                    packageName
                )
                val granted = mode == AppOpsManager.MODE_ALLOWED
                if (granted) {
                    SLSLogUtils.instance.sendLogLoad(
                        "IBXAppActivity",
                        "",
                        "IBX_APP_ACCESS_DIALOG_AGREE",
                        -1,
                        ""
                    )

                    SLSLogUtils.instance.sendLogLoad("IBXAppActivity", "", "IBX_APP_OPEN", -1, "")
//                    IBXSdk.getInstance()
//                        .init(
//                            application,
//                            "142793155",
//                            "dc8d848531e438bc",
//                            LocalUserManager.instance.getUserId()
//                        )
//                        .setHighCostState(true)
//                    IBXSdk.getInstance().start(this)
                } else {
                    SLSLogUtils.instance.sendLogLoad(
                        "IBXAppActivity",
                        "",
                        "IBX_APP_ACCESS_DIALOG_NOTAGREE",
                        -1,
                        ""
                    )
                }
            }
        }
        finish()
    }

    private fun getBind(): QmDialogIbxAppBinding {
        return binding as QmDialogIbxAppBinding
    }
}