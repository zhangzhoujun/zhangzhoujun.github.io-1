package com.qm.module_juggle.player

import android.app.Application
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qm.module_juggle.R

/**
 * @ClassName PlayerViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/15 9:30 PM
 * @Version 1.0
 */
class PlayerViewModel(application: Application) : ToolbarViewModel(application) {

    private fun initTitle() {
        val option = JYToolbarOptions()
        option.titleString = "视频播放"
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        setOptions(option)
    }

    override fun onCreate() {
        super.onCreate()

        initTitle()
    }

}