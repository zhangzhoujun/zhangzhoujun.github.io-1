package com.gos.nodetransfer.ui.ks

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import com.dim.library.utils.DLog
import com.dim.library.utils.ToastUtils
import com.gos.nodetransfer.entity.CGetTaskBean
import com.gos.nodetransfer.entity.CStoreTaskBean
import com.gos.nodetransfer.entity.MTaskListBean
import com.gos.nodetransfer.entity.MTaskResultBean
import com.gos.nodetransfer.http.MainService
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.BaseBean
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.http.RetrofitClient
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.JYMMKVManager
import com.qm.lib.utils.RuntimeData
import java.util.*

/**
 * @ClassName KSViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/10 1:42 PM
 * @Version 1.0
 */
class KSViewModel(application: Application) : BaseAppViewModel(application) {
    private val TAG = "KSViewModel"

    var mTimeShow: ObservableField<String> = ObservableField("")
    var mSureShow: ObservableField<String> = ObservableField("")
    var mShowReward: ObservableBoolean = ObservableBoolean(false)

//    private lateinit var showAllianceAD: ShowAllianceAD
    private var mDoPlay = false
    private var ksId = ""
    private var mIsPreparPlay = false

    private var taskList: ArrayList<MTaskListBean.MTashListItemBean>? = null

    override fun onCreate() {
        super.onCreate()

        initDayVideoLook()
        getKSTask()
    }

//    fun setFragment(showAllianceAD: ShowAllianceAD, ksID: String) {
//        this.showAllianceAD = showAllianceAD
//        this.ksId = ksID
//
//        showAllianceAD.loadAD(ksId)
//        setListener()
//    }
//
//    private fun setListener() {
//        showAllianceAD.setShowAllianceListener(object : ShowAllianceADListener {
//            override fun onVideoPlayCompleted() {
//
//            }
//
//            override fun onVideoPlayPaused() {
//                mDoPlay = false
//                stopCountAndSaveCurrentCount()
//            }
//
//            override fun onPageEnter() {
//
//            }
//
//            override fun onVideoPlayResume() {
//                DLog.d(TAG, "VideoListener -> onVideoPlayResume")
//                if (!mDoPlay) {
//                    showCountView()
//                    mDoPlay = true
//                }
//            }
//
//            override fun onClickShare(p0: String?) {
//
//            }
//
//            override fun onPagePause() {
//
//            }
//
//            override fun onPageResume() {
//
//            }
//
//            override fun onVideoPlayStart() {
//                DLog.d(TAG, "VideoListener -> onVideoPlayStart")
//                if (!mIsPreparPlay) {
//                    mIsPreparPlay = true
//                }
//                if (!mDoPlay) {
//                    showCountView()
//                    mDoPlay = true
//                }
//            }
//
//            override fun onNoAD(p0: Int) {
//                DLog.d(TAG, "VideoListener -> onVideoPlayError")
//                mDoPlay = false
//                stopCountAndSaveCurrentCount()
//            }
//
//            override fun onPageLeave() {
//
//            }
//
//        })
//    }

    private fun stopCountAndSaveCurrentCount() {
        removeCount()
        JYMMKVManager.instance.setTodayVideoLookTime(mCountTime)
    }

    fun doGetReward(view: View) {
        RouterManager.instance.gotoBTRecordActivity()
//        if (!canGet) {
//            return
//        }
//        if (taskList == null) {
//            return
//        }
//
//        for (index in 0 until taskList!!.size) {
//            if (!taskList!![index].isFinished()) {
//                doGetRewardAuto(taskList!![index])
//                return
//            }
//        }
    }

    private fun doGetRewardAuto(item: MTaskListBean.MTashListItemBean) {
        if (!canGet) {
            return
        }
        if (taskList == null) {
            return
        }
        storeKSTask(
            item.taskCode,
            item.awards.awardName.replace("奖励", "")
        )
    }

    // 初始化计时器任务
    private var task: TimerTask? = null
    private var timer: Timer? = null
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            mCountTime += 1000
            DLog.d(TAG, "计时$mCountTime")

            showTimeChanged()
        }
    }

    private fun timeParse(duration: Long): String {
        var time: String = ""
        val minute = duration / 60000
        val seconds = duration % 60000
        val second = Math.round(seconds.toFloat() / 1000).toLong()
        if (minute < 10) {
            time += "0"
        }
        time += "$minute:"
        if (second < 10) {
            time += "0"
        }
        time += second
        return time
    }

    private fun showTimeChanged() {
        if (taskList == null) {
            return
        }
        for (index in 0 until taskList!!.size) {
            if (!taskList!![index].isFinished()) {
                var contentLongList = taskList!![index].getMatchRules()
                for (pos in 0 until contentLongList.size) {
                    var item = taskList!![index].getMatchRules()[pos]
                    val contentLong = item.getContentLong()
                    if (item.isDoneTime() && mCountTime < contentLong) {
                        mTimeShow.set("${timeParse(contentLong - mCountTime)}")
                        mSureShow.set("BT积攒中")
                        canGet = false
                        return
                    }
                }
                if (!taskList!![index].isFinished()) {
                    mTimeShow.set("")
                    // mTimeShow.set(taskList!![index].awards.awardName.replace("奖励", ""))
                    mSureShow.set("BT积攒中")
                    canGet = true
                    stopCountAndSaveCurrentCount()
                    // 自动领取
                    doGetRewardAuto(taskList!![index])
                    return
                }
            }
        }
        canGet = false
        mTimeShow.set("奖励已\n领取")
        mSureShow.set("")
        stopCountAndSaveCurrentCount()
    }

    var canGet = false

    private var mCountTime = 0L

    private fun startCount() {
        removeCount()
        if (timer == null) {
            timer = Timer()
        }
        if (task == null) {
            task = object : TimerTask() {
                override fun run() {
                    val msg = handler.obtainMessage()
                    msg.what = 1
                    msg.obj = ""
                    msg.sendToTarget()
                }
            }
        }

        timer!!.schedule(task, 1000, 1000)
    }

    private fun removeCount() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        if (task != null) {
            task!!.cancel()
            task = null
        }
    }

    private fun initDayVideoLook() {
        val c = Calendar.getInstance()
        c.time = Date()
        val day = c[Calendar.DAY_OF_MONTH]
        val lastDaya = JYMMKVManager.instance.getTodayVideoLook()
        if (day != lastDaya) {
            JYMMKVManager.instance.setTodayVideoLook(day)
            JYMMKVManager.instance.setTodayVideoLookTime(0)
        } else {
            mCountTime = JYMMKVManager.instance.getTodayVideoLookTime()
        }
    }

    private fun getKSTask() {
        val service = RetrofitClient.getInstance().create(MainService::class.java)

        RetrofitClient.getInstance()
            .execute(service.sendGetTaskList(getRequestBody(BaseBean(CGetTaskBean()))),
                object : AppObserverNoDialog<BaseResultBean<MTaskListBean>>() {
                    override fun onSuccess(o: BaseResultBean<MTaskListBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            if (o.data.taskList != null && o.data.taskList.isNotEmpty()) {
                                taskList = o.data.taskList
                                showCountView()
                            }
                        }
                    }
                })
    }

    private fun showCountView() {
        mShowReward.set(!RuntimeData.getInstance().hideBlock && mIsPreparPlay)
        startCount()
    }

    private fun storeKSTask(taskCode: String, taskAward: String) {
        if (taskList == null) {
            return
        }
        if (!canGet) {
            return
        }
        canGet = false
        val service = RetrofitClient.getInstance().create(MainService::class.java)

        RetrofitClient.getInstance()
            .execute(service.sendStoreTask(getRequestBody(BaseBean(CStoreTaskBean(taskCode = taskCode)))),
                object : AppObserverNoDialog<BaseResultBean<MTaskResultBean>>() {
                    override fun onSuccess(o: BaseResultBean<MTaskResultBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess() && o.data.status) {
                            for (index in 0 until taskList!!.size) {
                                if (taskList!![index].taskCode == taskCode) {
                                    // 标记完成
                                    taskList!![index].setFinishOnce()
                                    ToastUtils.showShort("恭喜您获得${taskAward}奖励！")
                                    mCountTime = 0
                                    JYMMKVManager.instance.setTodayVideoLookTime(mCountTime)
                                    showTimeChanged()
                                    if (mDoPlay) {
                                        showCountView()
                                    }
                                    return
                                }
                            }
                        } else {
                            ToastUtils.showShort(o.errMsg)
                            canGet = true
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        canGet = true
                    }
                })
    }

}