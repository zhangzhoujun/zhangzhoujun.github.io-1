package com.qm.lib.base

import android.os.Bundle
import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.dim.library.utils.DLog
import com.qm.lib.widget.refresh.JYSwipeRefreshLayout

/**
 * @ClassName JYListFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/14 8:22 PM
 * @Version 1.0
 */
abstract class JYListFragment<V : ViewDataBinding?, VM : ListViewModel?> : JYFragment<V, VM>() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getListVM().initLoadingStatusView(getRecyclerView(), getEmptyContent())
    }

    private fun getListVM(): ListViewModel {
        return viewModel as ListViewModel
    }

    abstract fun getRefreshLayout(): JYSwipeRefreshLayout
    abstract fun getRecyclerView(): RecyclerView
    abstract fun getEmptyContent(): String
}
