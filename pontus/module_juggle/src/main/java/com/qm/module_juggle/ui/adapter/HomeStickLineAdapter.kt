package com.qm.module_juggle.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.qm.lib.utils.ScreenUtils
import com.qm.lib.utils.StringUtils
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainItemStickLineBinding
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.utils.JugglePathUtils


/**
 * @ClassName HomeTopAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class HomeStickLineAdapter(
    var context: Activity,
    var helper: StickyLayoutHelper,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_item_stick_line, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return item.data.size
    }

    override fun onCreateLayoutHelper(): StickyLayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeMainItemStickLineBinding = DataBindingUtil.bind(holder.itemView)!!
        var data = item.data[position]

        if (!StringUtils.instance.isEmpty(data.width_height)) {
            val iconData: Array<String> = data.width_height.split("\\*".toRegex()).toTypedArray()
            if (iconData.size == 2) {
                val wid: Int = ScreenUtils.getScreenWidth(context)
                val hei: Int = wid * iconData[1].toInt() / iconData[0].toInt()

                var lp = bind.bg.layoutParams;
                lp.height = hei

                bind.bg.layoutParams = lp

//                var iconLp = bind.homeMainImg.layoutParams
//                iconLp.height = hei
//                bind.homeMainImg.layoutParams = iconLp
            }
        }

        bind.homeMainImg.setImageUrl(data.img_url, data.width_height, 1)

        bind.homeMainImg.setOnClickListener {
            JugglePathUtils.instance.onJuggleItemClick(item.data[position], "STICK", position)
        }
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}