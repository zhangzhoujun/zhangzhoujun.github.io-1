package com.gos.nodetransfer.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.Utils
import com.qm.lib.utils.JYUtils


/**
 * @ClassName SchemeFilterActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/30 4:02 PM
 * @Version 1.0
 */
class SchemeFilterActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        var currentScheme = JYUtils.instance.getMetaDatByName(Utils.getContext(), "scheme_host")
//        if (currentScheme != null && !TextUtils.isEmpty(currentScheme) && pathAndParams.contains(
//                "qm.valuechain.com"
//            ) && currentScheme != "qm.valuechain.com"
//        ) {
//            pathAndParams.replace("qm.valuechain.com", currentScheme)
//        }

        val uri: Uri? = intent.data

        ARouter.getInstance().build(uri).navigation(this, object : NavCallback() {
            override fun onArrival(postcard: Postcard) {
                finish()
            }
        })
    }

}