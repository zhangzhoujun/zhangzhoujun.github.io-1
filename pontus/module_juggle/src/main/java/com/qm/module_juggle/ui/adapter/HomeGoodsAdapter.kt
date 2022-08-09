package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.dim.library.utils.DLog
import com.qm.lib.entity.MGoodsBean
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.SLSLogUtils
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainItemGoodBinding
import com.qm.module_juggle.entity.MHomeDataBean
import java.text.DecimalFormat


/**
 * @ClassName HomeTopAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class HomeGoodsAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var dataItem: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {
    var mGoodsList: ArrayList<MGoodsBean.MJuggleGoodsItemBean>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_item_good, parent, false)

        return RecyclerViewItemHolder(view)
    }

    fun setGoodData(goodsList: ArrayList<MGoodsBean.MJuggleGoodsItemBean>) {
        mGoodsList = goodsList
        notifyDataSetChanged()
    }

    fun setGoodDataAdd(goodsList: ArrayList<MGoodsBean.MJuggleGoodsItemBean>) {
        if (goodsList != null && goodsList.isNotEmpty()) {
            mGoodsList!!.addAll(goodsList)
            notifyItemInserted(mGoodsList!!.size - goodsList.size)
        }
    }

    override fun getItemCount(): Int {
        return if (mGoodsList == null) 0 else mGoodsList!!.size
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeMainItemGoodBinding = DataBindingUtil.bind(holder.itemView)!!

        var item = mGoodsList!![position]

        var wid = context.resources.getDimension(R.dimen.dp_170).toInt()
        bind.icon.setImageUrl(item.goodsPic)
        bind.name.text = item.goodsName
        //bind.price.text = item.couponUsedPrice
        bind.pricaNormal.text = "¥${item.price}"
        bind.pricaNormal.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        bind.youhui.text = "${item.couponInfo[0].discount}元劵"

        var text = "¥${item.couponUsedPrice}"

        var count = text.length
        if (text.contains(".")) {
            var textNoPoint = text.split(".")[0]
            count = textNoPoint.length
        }

        val styledText = SpannableString(text)
        styledText.setSpan(
            TextAppearanceSpan(context, R.style.home_price_small),
            0,
            1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            TextAppearanceSpan(context, R.style.home_price_big),
            1,
            count,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        styledText.setSpan(
            TextAppearanceSpan(context, R.style.home_price_small),
            count,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        bind.price.setText(styledText, TextView.BufferType.SPANNABLE)

        // 销量
        if (item.sellNum.length >= 5) {
            var sellLong = item.sellNum.toDouble()
            bind.sale.text = "月销量：${DecimalFormat("0.0").format(sellLong / 10000)}万"
        } else {
            bind.sale.text = "月销量：${item.sellNum}"
        }

        bind.root.setOnClickListener {
            DLog.d("跳转到商品详情")

            SLSLogUtils.instance.sendLogClick(
                "", dataItem.mPageKey, "GOODS", position, "",
                extra = "platfrom = ${item.platform} , goodsId = ${item.goodsId} , GoodsName = ${item.goodsName}"
                , viewId = dataItem.id
            )
            RouterManager.instance.gotoGoodsDetailsActivity(item)
        }
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}