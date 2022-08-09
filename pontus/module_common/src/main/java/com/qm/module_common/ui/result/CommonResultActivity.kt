package com.qm.module_common.ui.result

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qm.module_common.BR
import com.qm.module_common.R
import com.qm.module_common.databinding.CommonActResultBinding

/**
 * @ClassName CommonResultActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/20 11:18 AM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Common.COMMON_RESULT)
class CommonResultActivity : JYActivity<CommonActResultBinding, CommonResultViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.COMMON_RESULT_TITLE)
    var title: String = "结果详情"

    @JvmField
    @Autowired(name = JYComConst.COMMON_RESULT_DES)
    var des: String = "成功"

    @JvmField
    @Autowired(name = JYComConst.COMMON_RESULT_SURE_NAME)
    var sureName: String = "好的"

    @JvmField
    @Autowired(name = JYComConst.COMMON_RESULT_CALLBACK_TAG)
    var tag: String = ""

    @JvmField
    @Autowired(name = JYComConst.COMMON_RESULT_TYPE)
    var resoultType: Int = JYComConst.COMMON_RESULT_TYPE_SUCCESS

    @JvmField
    @Autowired(name = JYComConst.COMMON_RESULT_CALLBACK)
    var needCallBack: Boolean = false

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.common_act_result
    }

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)

        getVm().initViewData(title, des, sureName, resoultType, needCallBack,tag)
    }

    private fun getVm(): CommonResultViewModel {
        return viewModel as CommonResultViewModel
    }
}