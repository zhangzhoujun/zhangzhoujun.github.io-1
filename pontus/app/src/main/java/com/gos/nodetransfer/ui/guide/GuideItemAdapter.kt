package com.gos.nodetransfer.ui.guide

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gos.nodetransfer.R
import com.gos.nodetransfer.databinding.QmGuideItemBinding
import com.youth.banner.adapter.BannerAdapter


/**
 * @ClassName HomeTopBannerAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/7/3 10:44 AM
 * @Version 1.0
 */
class GuideItemAdapter(
    var context: Context, var mBean: ArrayList<Int>,var listener : OnGuideItemClickListener
) :
    BannerAdapter<Int, GuideItemAdapter.BannerViewHolder>(
        mBean
    ) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.qm_guide_item, parent, false)

        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        view.layoutParams = lp

        return BannerViewHolder(view)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: Int,
        position: Int,
        size: Int
    ) {
        var bind: QmGuideItemBinding = DataBindingUtil.bind(holder!!.itemView)!!

        bind.icon.setImageUrl(data)

        bind.icon.setOnClickListener {
            if(listener != null){
                listener.onGuideItemClick(position)
            }
        }
    }

    class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    interface OnGuideItemClickListener{
        fun onGuideItemClick(position: Int)
    }

}