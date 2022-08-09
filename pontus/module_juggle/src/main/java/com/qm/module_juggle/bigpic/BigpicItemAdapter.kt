package com.qm.module_juggle.bigpic

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeBigpicItemBinding
import com.youth.banner.adapter.BannerAdapter


/**
 * @ClassName HomeTopBannerAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/7/3 10:44 AM
 * @Version 1.0
 */
class BigpicItemAdapter(
    var context: Context, var mBean: ArrayList<String>,var listener : OnBiPicClickListener
) :
    BannerAdapter<String, BigpicItemAdapter.BannerViewHolder>(
        mBean
    ) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_bigpic_item, parent, false)

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
        var bind: HomeBigpicItemBinding = DataBindingUtil.bind(holder!!.itemView)!!

        bind.icon.setImageUrl(data)

        bind.icon.setOnClickListener { e ->
            run {
                if (listener != null) {
                    listener.onBigPicClick(position)
                }
            }
        }
    }

    class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

    public interface OnBiPicClickListener{
        public fun onBigPicClick(position: Int)
    }
}