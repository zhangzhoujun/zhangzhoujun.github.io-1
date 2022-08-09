package com.qm.lib.base

import android.app.Application
import androidx.databinding.ObservableBoolean
import com.dim.library.binding.command.BindingAction
import com.dim.library.binding.command.BindingCommand
import com.dim.library.bus.event.SingleLiveEvent
import com.dim.library.utils.DLog
import com.qm.lib.widget.toolbar.ToolbarViewModel

/**
 * @ClassName ListViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/14 8:08 PM
 * @Version 1.0
 */
open abstract class ListViewModel(application: Application) : ToolbarViewModel(application) {

    protected var mCurrentPage = 1
    protected var mTotalPage = 1
    var doesCanLoadMore: SingleLiveEvent<Boolean> = SingleLiveEvent()

    class UIChangeObservable {
        //下拉刷新完成
        var finishRefreshing = ObservableBoolean(false)

        //上拉加载完成
        var finishLoadmore = ObservableBoolean(false)
    }

    var listuc = UIChangeObservable()

    //下拉刷新
    var onRefreshCommand: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            DLog.d("onRefreshCommand")
            onLoadRefresh()
        }
    })

    var onLoadMoreCommand: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
            DLog.d("onLoadMoreCommand")
            onLoadMore()
        }
    })

    protected fun onLoadFinish() {
        if (mCurrentPage == 1) {
            listuc.finishRefreshing.set(!listuc.finishRefreshing.get())
        } else {
            listuc.finishLoadmore.set(!listuc.finishLoadmore.get())
        }
        if (mCurrentPage >= mTotalPage) {
            doesCanLoadMore.setValue(false)
        } else {
            doesCanLoadMore.setValue(true)
        }
        onLoadFisished()
    }

    abstract fun onLoadRefresh()
    abstract fun onLoadMore()
    abstract fun onLoadFisished()
}