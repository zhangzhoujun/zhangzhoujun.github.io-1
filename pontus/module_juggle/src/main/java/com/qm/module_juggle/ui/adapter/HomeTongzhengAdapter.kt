package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.qm.lib.base.LocalUserManager
import com.qm.lib.router.RouterManager.Companion.instance
import com.qm.lib.utils.ColorUtils
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeTzMainBinding
import com.qm.module_juggle.entity.MHomeDataBean

/**
 * @ClassName HomeMineAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/3 7:51 PM
 * @Version 1.0
 */
class HomeTongzhengAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_tz_main, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeTzMainBinding = DataBindingUtil.bind(holder.itemView)!!
        if (LocalUserManager.instance.getUser() == null) {
            return
        }

//        bind.address.text = "ID：${LocalUserManager.instance.getUserPublickey()}"
        bind.btIcon.setImageUrl(item.bt_icon)
        bind.btBg.setImageUrl(item.main_bg_url)
//        bind.btShow.text = LocalUserManager.instance.getUserTotalAmount()
        bind.iconBg.setImageUrl(item.retouch)
        bind.title.text = item.title

        bind.title.setTextColor(ColorUtils.instance.getColorForString(item.title_font_color))

        var contentColor = ColorUtils.instance.getColorForString(item.content_font_color)
        bind.btShow.setTextColor(contentColor)
        bind.btName.setTextColor(contentColor)
        bind.address.setTextColor(contentColor)

        bind.address.setOnClickListener { e ->
            instance.gotoBTRecordActivity()
        }
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

}