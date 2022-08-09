package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainHScrollBinding
import com.qm.module_juggle.entity.MHomeDataBean


/**
 * @ClassName HomeTopAdapter
 * @Description 横向滚动
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class HomeHScrollAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_h_scroll, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeMainHScrollBinding = DataBindingUtil.bind(holder.itemView)!!

        var lm = LinearLayoutManager(context)
        lm.orientation = LinearLayoutManager.HORIZONTAL

        bind.recyclerView.layoutManager = lm

        bind.recyclerView.adapter =
            HomeHScrollRecyclerviewAdapter(item.item_width, context, item.data)
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}