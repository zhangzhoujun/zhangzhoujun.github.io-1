package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainItemBannerItemBinding
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.utils.JugglePathUtils
import com.youth.banner.adapter.BannerAdapter


/**
 * @ClassName HomeTopBannerAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/7/3 10:44 AM
 * @Version 1.0
 */
class HomeBannerItemAdapter(
    var context: Context, var mLists: ArrayList<MHomeDataBean.MHomeDataItemData>
) :
    BannerAdapter<MHomeDataBean.MHomeDataItemData, HomeBannerItemAdapter.BannerViewHolder>(
        mLists
    ) {
    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_item_banner_item, parent, false)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = lp

        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: MHomeDataBean.MHomeDataItemData,
        position: Int,
        size: Int
    ) {
        var bind: HomeMainItemBannerItemBinding = DataBindingUtil.bind(holder!!.itemView)!!

        bind.homeMainImg.setImageUrl(data.img_url, data.width_height, 1)

        bind.homeMainImg.setOnClickListener {
            JugglePathUtils.instance.onJuggleItemClick(mLists[position], "CAROUSEL", position)
        }
    }

    class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}