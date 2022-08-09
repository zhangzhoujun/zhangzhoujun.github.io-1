package com.gos.nodetransfer.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Route
import com.dim.library.utils.DLog
import com.gos.nodetransfer.BR
import com.gos.nodetransfer.R
import com.gos.nodetransfer.databinding.QmFraTaskBinding
import com.qm.lib.base.JYFragment
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.SLSLogUtils
import com.qy.qysdk.manager.QYManager


/**
 * @ClassName TaskFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/10 1:40 PM
 * @Version 1.0
 */
@Route(path = RouterFragmentPath.Main.MIAN_TASK)
class TaskFragment : JYFragment<QmFraTaskBinding, TaskViewModel>() {
    private val TAG = "TaskFragment"
    var taskCenterFrag: Fragment? = null

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.qm_fra_task
    }

    private fun getVm(): TaskViewModel {
        return viewModel as TaskViewModel
    }

    override fun initData() {
        super.initData()

        try {
            taskCenterFrag = QYManager.getInstance().homeFragment

            //val transaction = childFragmentManager!!.beginTransaction()

            val transaction: FragmentTransaction = childFragmentManager!!.beginTransaction()
            transaction.replace(R.id.task_container, QYManager.getInstance().homeFragment)
            transaction.commit()

//            var frameLayout = FrameLayout(context!!)
//            frameLayout.id = (Math.random() * 100000).toInt()
//
//            var lp = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            lp.height = 2000
//            frameLayout.layoutParams = lp
//            getBind().root.addView(frameLayout)
//            transaction.replace(R.id.task_container, taskCenterFrag!!)
//
//            transaction.commitAllowingStateLoss()

//            taskCenterFrag!!.setHomeBannerListener { position, actionValues ->
//                RouterManager.instance.gotoOtherPage(actionValues)
//            }
            getStep()

//            QYManager.getInstance().showHomeActivity(activity)
        } catch (e: Exception) {
            e.printStackTrace()

            SLSLogUtils.instance.sendLogError(
                "TaskFragment",
                "TaskFragment",
                "LOAD",
                extra = e.message!!
            )
        }
    }

    private fun getStep() {
//        taskCenterFrag!!.setStepUpdateListener {
//            DLog.d("taskCenterFrag", "同步到的微信步数为 $it")
//            sendStepToServer(it)
//
//            SLSLogUtils.instance.sendLogClick(
//                "TaskFragment",
//                "",
//                "SYNC_WECHAT_STEP"
//            )
//        }
    }

    private fun sendStepToServer(step: Int) {
        getVm().sendSyncStep(step)
    }

    private fun getBind(): QmFraTaskBinding {
        return binding as QmFraTaskBinding
    }

    override fun onResume() {
        super.onResume()
        DLog.d(TAG, "onResume")
//        if (taskCenterFrag != null) {
//            taskCenterFrag!!.activeReport();
//        }
    }

    override fun onPause() {
        super.onPause()
        DLog.d(TAG, "onPause")
    }
}