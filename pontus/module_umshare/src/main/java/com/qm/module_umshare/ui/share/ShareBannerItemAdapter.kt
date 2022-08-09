package com.qm.module_umshare.ui.share

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.qm.module_umshare.R
import com.qm.module_umshare.databinding.UmShareIconItemBinding
import com.qm.module_umshare.entity.MShareMainBean
import com.qm.module_umshare.util.QRCodeUtil
import com.youth.banner.adapter.BannerAdapter


/**
 * @ClassName HomeTopBannerAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/7/3 10:44 AM
 * @Version 1.0
 */
class ShareBannerItemAdapter(
    var context: Context, var mBean: MShareMainBean
) :
    BannerAdapter<String, ShareBannerItemAdapter.BannerViewHolder>(
        mBean.backImgList
    ) {
    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.um_share_icon_item, parent, false)

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
        var bind: UmShareIconItemBinding = DataBindingUtil.bind(holder!!.itemView)!!

        bind.icon.setImageUrl(data)
        val mBitmap = QRCodeUtil.createQRCodeBitmap(mBean.shareUrl, 480, 480)
        bind.qrcode.setImageBitmap(mBitmap)

        bind.code.text = "邀请码：" + mBean.shareCode
    }

    class BannerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)

}