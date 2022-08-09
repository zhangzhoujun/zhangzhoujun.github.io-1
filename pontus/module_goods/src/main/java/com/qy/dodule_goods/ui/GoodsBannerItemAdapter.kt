package com.qy.dodule_goods.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.qy.dodule_goods.R
import com.qy.dodule_goods.databinding.GoodsBannerItemBinding
import com.youth.banner.adapter.BannerAdapter


/**
 * @ClassName HomeTopBannerAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/7/3 10:44 AM
 * @Version 1.0
 */
class GoodsBannerItemAdapter(
    var context: Context, var mLists: ArrayList<String>
) :
    BannerAdapter<String, GoodsBannerItemAdapter.BannerViewHolder>(
        mLists
    ) {
    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.goods_banner_item, parent, false)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = lp

        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: String,
        position: Int,
        size: Int
    ) {
        var bind: GoodsBannerItemBinding = DataBindingUtil.bind(holder!!.itemView)!!
        bind.banner = data
    }

    class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
}