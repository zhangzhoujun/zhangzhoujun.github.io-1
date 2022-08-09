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
import com.qm.module_juggle.R.style
import com.qm.module_juggle.databinding.HomeMainItemGoodBinding
import com.qm.module_juggle.databinding.JuggleMainItemGoodSkinBinding
import com.qm.module_juggle.databinding.JuggleMainItemGoodSkinGridBinding
import com.qm.module_juggle.entity.MHomeDataBean
import java.text.DecimalFormat

/**
 * @ClassName JuggleGoodsAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2021/11/19 10:43 上午
 * @Version 1.0
 */
class JuggleGoodsGridAdapter (
    var context: Context,
    var helper: LayoutHelper,
    var dataItem: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.juggle_main_item_good_skin_grid, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return if (dataItem.data == null) 0 else dataItem.data!!.size
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: JuggleMainItemGoodSkinGridBinding = DataBindingUtil.bind(holder.itemView)!!

        var item = dataItem.data!![position]

        bind.icon.setImageUrl(item.main_img)
        bind.name.text = item.goods_name
        bind.price.text = "¥ ${item.price}"

        bind.coupon.setOnClickListener {
            RouterManager.instance.gotoOtherPage(item.coupon_link)
        }
        bind.buy.setOnClickListener {
            RouterManager.instance.gotoOtherPage(item.link)
        }
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}