package com.qm.module_common.ui.feedback

import android.app.Application
import com.dim.library.utils.Utils
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qm.module_common.R

/**
 * @ClassName FeedbackViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/19 5:40 PM
 * @Version 1.0
 */
class FeedbackViewModel(application: Application) : ToolbarViewModel(application) {

    override fun onCreate() {
        super.onCreate()

        // initTitle()
    }

    private fun initTitle() {
        val option = JYToolbarOptions()
        option.titleString = ""
        option.isNeedNavigate = false
        option.backId = R.mipmap.lib_base_back
        option.bgColor = Utils.getContext().resources.getColor(R.color.white)
        setOptions(option)
    }
}