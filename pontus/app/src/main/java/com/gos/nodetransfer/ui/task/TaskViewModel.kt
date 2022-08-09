package com.gos.nodetransfer.ui.task

import android.app.Application
import com.gos.nodetransfer.entity.CStepSyncBean
import com.gos.nodetransfer.http.MainService
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.BaseBean
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.http.RetrofitClient

/**
 * @ClassName TaskViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/10 1:42 PM
 * @Version 1.0
 */
class TaskViewModel(application: Application) : BaseAppViewModel(application) {
    private val TAG = "TaskViewModel"


    override fun onCreate() {
        super.onCreate()
    }

    fun sendSyncStep(step: Int) {
        val service = RetrofitClient.getInstance().create(MainService::class.java)

        RetrofitClient.getInstance()
            .execute(service.sendStepSync(
                getRequestBody(
                    BaseBean(
                        CStepSyncBean(
                            step,
                            System.currentTimeMillis()
                        )
                    )
                )
            ),
                object : AppObserverNoDialog<BaseResultBean<Any>>() {
                    override fun onSuccess(o: BaseResultBean<Any>) {
                        super.onSuccess(o)

                    }
                })
    }
}