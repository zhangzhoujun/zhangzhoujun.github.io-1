package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.dim.library.utils.StringUtils
import com.qm.lib.base.LocalUserManager
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMineZichanItemBinding
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.utils.JugglePathUtils
import org.json.JSONObject

/**
 * @ClassName HomeMineAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/3 7:51 PM
 * @Version 1.0
 */
class HomeZichanAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem,
    var dataString: String
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_mine_zichan_item, parent, false)

        return RecyclerViewItemHolder(view)
    }

    fun setDataBena(dataString: String) {
        this.dataString = dataString
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return item.data.size
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeMineZichanItemBinding = DataBindingUtil.bind(holder.itemView)!!

        var item = item.data[position]
        bind.title.text = item.label
        if (StringUtils.isEmpty(item.sub_label)) {
            bind.subTitle.visibility = View.GONE
        } else {
            bind.subTitle.visibility = View.VISIBLE
            bind.subTitle.text = item.sub_label
        }
        if (item.schema_key == "bt") {
//            bind.content.text = LocalUserManager.instance.getUserTotalAmount()
        } else {
            if (!StringUtils.isEmpty(dataString)) {
                val key = item.schema_key
                var mapJSON = JSONObject(dataString)
                if (mapJSON.has(key)) {
                    bind.content.text = mapJSON.get(key).toString()
                }
            }
        }

        holder.itemView.setOnClickListener {
            JugglePathUtils.instance.onJuggleItemClick(
                item,
                "ASSETS",
                position
            )
        }
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}