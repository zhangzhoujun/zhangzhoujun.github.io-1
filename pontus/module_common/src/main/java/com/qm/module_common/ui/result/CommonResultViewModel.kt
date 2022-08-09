package com.qm.module_common.ui.result

import android.app.Application
import androidx.databinding.ObservableField
import com.dim.library.binding.command.BindingAction
import com.dim.library.binding.command.BindingCommand
import com.dim.library.bus.RxBus
import com.dim.library.utils.DLog
import com.dim.library.utils.Utils
import com.qm.lib.message.CommonResoultCallBack
import com.qm.lib.utils.JYComConst
import com.qm.lib.widget.toolbar.JYToolbarOptions
import com.qm.lib.widget.toolbar.ToolbarViewModel
import com.qm.module_common.R

/**
 * @ClassName PayResultViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/20 10:47 AM
 * @Version 1.0
 */
class CommonResultViewModel(application: Application) : ToolbarViewModel(application) {

    var des: ObservableField<String> = ObservableField()
    var sureText: ObservableField<String> = ObservableField()
    var mType: ObservableField<Int> = ObservableField()

    private var mNeedCallBack = false
    private var mTag = ""

    fun initViewData(
        titleString: String,
        desString: String,
        sureTextString: String,
        type: Int = JYComConst.COMMON_RESULT_TYPE_SUCCESS,
        needCallBack: Boolean = false,
        tag: String = ""
    ) {
        initTitle(titleString)
        des.set(desString)
        sureText.set(sureTextString)
        mType.set(type)
        mNeedCallBack = needCallBack
        mTag = tag
    }

    private fun initTitle(title: String) {
        val option = JYToolbarOptions()
        option.titleString = title
        option.isNeedNavigate = true
        option.backId = R.mipmap.lib_base_back
        option.bgColor = Utils.getContext().resources.getColor(R.color.white)
        setOptions(option)
    }

    var onSureClick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            DLog.d("onSureClick")
            if (mNeedCallBack) {
                RxBus.getDefault().post(CommonResoultCallBack(mTag))
            }
            finish()
        }
    })

}