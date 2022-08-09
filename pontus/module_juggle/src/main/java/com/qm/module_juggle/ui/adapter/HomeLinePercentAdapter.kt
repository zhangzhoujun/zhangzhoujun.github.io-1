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
import com.qm.lib.widget.dim.AppImageView
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainLinePercentBinding
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.utils.JugglePathUtils


/**
 * @ClassName HomeTopAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class HomeLinePercentAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_line_percent, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeMainLinePercentBinding = DataBindingUtil.bind(holder.itemView)!!
        bind.rootBg.removeAllViews()
        var wid = ScreenUtils.getScreenWidth(context)

        var allWid = 0
        var bgHei = 100
        val allCount = item.data.size

        for (index in 0 until allCount) {
            var cItem = item.data[index]
            var image = AppImageView(context)

            var widHeight = cItem.width_height

            if (widHeight == null || TextUtils.isEmpty(widHeight)) {
                image.setImageUrl(cItem.img_url)
            } else {
                val iconData: Array<String> =
                    widHeight.split("\\*".toRegex()).toTypedArray()
                if (iconData.size != 2) {
                    image.setImageUrl(cItem.img_url)
                } else {
                    var width = wid * cItem.item_width / 100
                    if (index < allCount - 1) {
                        allWid += width
                    } else {
                        width = wid - allWid
                    }
                    var cHei = width * iconData[1].toInt() / iconData[0].toInt()

                    if (bgHei < cHei) bgHei = cHei

                    var imgLp = ViewGroup.LayoutParams(width, bgHei)
                    image.layoutParams = imgLp

                    image.setImageUrl(cItem.img_url, wid, bgHei)
                }
            }

            image.setOnClickListener {
                JugglePathUtils.instance.onJuggleItemClick(cItem, "LINE_PERCENT", position)
            }

            bind.rootBg.addView(image)
        }
        var lp = bind.rootBg.layoutParams
        lp.height = bgHei
        bind.rootBg.layoutParams = lp
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}