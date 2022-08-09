package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.qm.lib.utils.ScreenUtils
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainItemHSRItemBinding
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.utils.JugglePathUtils


/**
 * @ClassName HomeHScrollRecyclerviewAdapter
 * @Description 横向滚动
 * @Author zhangzhoujun
 * @Date 2020/11/17 3:20 PM
 * @Version 1.0
 */
class HomeHScrollRecyclerviewAdapter(
    var item_width: Int,
    var context: Context,
    var listData: ArrayList<MHomeDataBean.MHomeDataItemData>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_item_h_s_r_item, parent, false)

        return HomeHScrollRecyclerviewViewHolder(view)
    }

    override fun onBindViewHolder(
        @NonNull holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        var bind: HomeMainItemHSRItemBinding = DataBindingUtil.bind(holder.itemView)!!
        bind.banner = listData[position]

        var lp = bind.icon.layoutParams

        var wid = ScreenUtils.getScreenWidth(context)
        lp.width = wid * item_width / 100
        lp.height = wid * item_width / 100

        bind.icon.layoutParams = lp

        holder.itemView.setOnClickListener {
            JugglePathUtils.instance.onJuggleItemClick(
                listData[position],
                "H_SCROLL_ITEM",
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    private class HomeHScrollRecyclerviewViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}

