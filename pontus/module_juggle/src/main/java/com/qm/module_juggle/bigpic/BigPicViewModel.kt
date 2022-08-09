package com.qm.module_juggle.bigpic

import android.app.Application
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qm.module_juggle.R

/**
 * @ClassName BigPicViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/16 11:24 AM
 * @Version 1.0
 */
class BigPicViewModel(application: Application) : ToolbarViewModel(application){

    private fun initTitle() {
        val option = JYToolbarOptions()
        option.titleString = "查看大图"
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    override fun onCreate() {
        super.onCreate()

        initTitle()
    }

}