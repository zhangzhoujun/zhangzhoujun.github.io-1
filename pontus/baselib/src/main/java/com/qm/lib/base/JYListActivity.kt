package com.qm.lib.base

import android.os.Bundle
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.dim.library.utils.DLog
import com.qm.lib.widget.refresh.JYSwipeRefreshLayout

/**
 * @ClassName JYActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/14 8:33 PM
 * @Version 1.0
 */
abstract class JYListActivity<V : ViewDataBinding?, VM : ListViewModel?> : JYActivity<V, VM>() {

    override fun initViewObservable() {
        super.initViewObservable()
        getListVM().listuc.finishLoadmore.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(
                sender: Observable,
                propertyId: Int
            ) {
                DLog.d("finishLoadmore")
                getRefreshLayout().finishLoadmore()
            }
        })
        getListVM().listuc.finishRefreshing.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(
                sender: Observable,
                propertyId: Int
            ) {
                DLog.d("finishRefreshing")
                getRefreshLayout().finishRefresh()
            }
        })

        getListVM().doesCanLoadMore.observe(
            this,
            Observer { aBoolean -> getRefreshLayout().setLoadmoreEnable(aBoolean!!) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getListVM().initLoadingStatusView(getRecyclerView(), getEmptyContent())
    }

    private fun getListVM(): ListViewModel {
        return viewModel as ListViewModel
    }

    abstract fun getRefreshLayout(): JYSwipeRefreshLayout
    abstract fun getRecyclerView(): RecyclerView
    abstract fun getEmptyContent(): String

}