package com.qm.module_juggle.qr

import android.app.Application
import com.qm.lib.utils.ColorUtils
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qm.module_juggle.R

/**
 * @ClassName QRViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/30 3:40 PM
 * @Version 1.0
 */
class QRViewModel(application: Application) : ToolbarViewModel(application){

    private fun initTitle() {
        val option = JYToolbarOptions()
        option.titleString = "扫描二维码"
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    override fun onCreate() {
        super.onCreate()

        initTitle()
    }

}