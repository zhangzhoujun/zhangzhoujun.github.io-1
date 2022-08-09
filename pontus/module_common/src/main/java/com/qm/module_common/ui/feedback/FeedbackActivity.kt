package com.qm.module_common.ui.feedback

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.module_common.BR
import com.qm.module_common.R
import com.qm.module_common.databinding.CommonActFeedbackBinding
import com.qmuiteam.qmui.util.QMUIDisplayHelper

/**
 * @ClassName FeedbackActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/19 5:42 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Common.COMMON_FEEDBACK)
class FeedbackActivity : JYActivity<CommonActFeedbackBinding, FeedbackViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.common_act_feedback
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    private fun getBind(): CommonActFeedbackBinding {
        return binding as CommonActFeedbackBinding
    }

    override fun initData() {
        super.initData()

        var lp = getBind().topView.layoutParams

        lp.height = QMUIDisplayHelper.getStatusBarHeight(this)

        getBind().topView.layoutParams = lp

//        FeedbackAPI.setTranslucent(true)
//        FeedbackAPI.setBackIcon(R.mipmap.lib_base_back)
//        FeedbackAPI.setTitleBarHeight(QMUIDisplayHelper.getActionBarHeight(this))
//        FeedbackAPI.setDefaultUserContactInfo(LocalUserManager.instance.getUserMobile())
//        FeedbackAPI.setUserNick("${LocalUserManager.instance.getUserNickname()}(${LocalUserManager.instance.getUserId()})")
//
//        val fm: FragmentManager = supportFragmentManager
//        val transaction: FragmentTransaction = fm.beginTransaction()
//        val feedback: Fragment = FeedbackAPI.getFeedbackFragment()
//        // must be called
//        FeedbackAPI.setFeedbackFragment(Callable<Any?> {
//            transaction.replace(R.id.container, feedback)
//            transaction.commit()
//
//            FeedbackAPI.setTranslucent(false)
//            null
//        } /*success callback*/,
//            Callable<Any?> {
//                finish()
//                null
//            } /*fail callback*/)
    }
}