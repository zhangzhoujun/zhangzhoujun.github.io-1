package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.qm.lib.utils.ScreenUtils
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainItemBannerBinding
import com.qm.module_juggle.entity.MHomeDataBean
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.util.BannerUtils


/**
 * @ClassName HomeTopAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class HomeBannerAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_item_banner, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeMainItemBannerBinding = DataBindingUtil.bind(holder.itemView)!!

        if (item.retouch != null && item.retouch.isNotEmpty()) {
            bind.bg.setImageUrl(item.retouch)
            bind.bg.visibility = View.VISIBLE
        } else {
            bind.bg.visibility = View.INVISIBLE
        }

        var widHeight = item.data[0].width_height
        if (widHeight != null && !TextUtils.isEmpty(widHeight)) {
            val iconData: Array<String> =
                widHeight.split("\\*".toRegex()).toTypedArray()
            if (iconData.size == 2) {
                val wid: Int = ScreenUtils.getScreenWidth(context)
                val hei = wid * iconData[1].toInt() / iconData[0].toInt()
                var lp = bind.banner.layoutParams
                lp.height = hei
                bind.banner.layoutParams = lp
            }
        }

        bind.banner
            //.addBannerLifecycleObserver(this)//添加生命周期观察者
            .setAdapter(HomeBannerItemAdapter(context, item.data))
            //.setIndicator(CircleIndicator(context))

            .setIndicator(RectangleIndicator(context))
            .setIndicatorSelectedWidth(BannerUtils.dp2px(12f).toInt())
            .setIndicatorSpace(BannerUtils.dp2px(4f).toInt())
            .setIndicatorRadius(0)
            .start()
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}