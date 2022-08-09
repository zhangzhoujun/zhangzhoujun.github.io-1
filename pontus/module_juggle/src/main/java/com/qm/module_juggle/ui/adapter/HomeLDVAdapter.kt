package com.qm.module_juggle.ui.adapter

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.dim.library.utils.DLog
import com.kunminx.linkage.adapter.viewholder.LinkagePrimaryViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryFooterViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryHeaderViewHolder
import com.kunminx.linkage.adapter.viewholder.LinkageSecondaryViewHolder
import com.kunminx.linkage.bean.BaseGroupedItem
import com.kunminx.linkage.contract.ILinkagePrimaryAdapterConfig
import com.kunminx.linkage.contract.ILinkageSecondaryAdapterConfig
import com.qm.lib.utils.ScreenUtils
import com.qm.lib.widget.dim.AppImageView
import com.qm.lib.widget.dim.AppTextView
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainLdVBinding
import com.qm.module_juggle.entity.ElemeGroupedItem
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.utils.JugglePathUtils


/**
 * @ClassName HomeHotAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/20 11:31 AM
 * @Version 1.0
 */
class HomeLDVAdapter(
    var context: Activity,
    var helper: LinearLayoutHelper,
    var item: MHomeDataBean.MHomeDataItem,
    var scrollToBottom: Boolean,
    var page_no_back: Boolean
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    var homeHotBind: HomeMainLdVBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_ld_v, parent, false)

        return RecyclerViewItemHolder(view)
    }

    fun setGroupScrollToBottom(toBottom: Boolean) {
        this.scrollToBottom = toBottom
        if (homeHotBind != null) {
            homeHotBind!!.linkage.setGroupScrollToBottom(scrollToBottom)
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        homeHotBind = DataBindingUtil.bind(holder.itemView)!!

        var items = ArrayList<ElemeGroupedItem>()
        for (index in 0 until item.data.size) {
            var firstItem = item.data[index]
            items.add(ElemeGroupedItem(true, firstItem.title))
            for (pos in 0 until firstItem.data.size) {
                items.add(
                    ElemeGroupedItem(
                        ElemeGroupedItem.ItemInfo(
                            firstItem.data[pos],
                            firstItem.title,
                            "$index-$pos"
                        )
                    )
                )
            }
            // foot
            items.add(ElemeGroupedItem(ElemeGroupedItem.ItemInfo("", firstItem.title)))
        }
        homeHotBind!!.linkage.init(
            items,
            ElemeLinkagePrimaryAdapterConfig(),
            ElemeLinkageSecondaryAdapterConfig()
        )

        homeHotBind!!.linkage.setGroupScrollToBottom(scrollToBottom)

        homeHotBind!!.linkage.isGridMode = true

//         var statusbarHei = QMUIStatusBarHelper.getStatusbarHeight(context)
        var bottomHeight = context.resources.getDimension(R.dimen.dp_49).toInt()
        var topHeight = context.resources.getDimension(R.dimen.dp_56).toInt()
        var screenHeight = ScreenUtils.getScreenHeight(context)
        var bottomStatusHei = ScreenUtils.getBottomStatusHeight(context)
        var topViewHeight =
            if (page_no_back) context.resources.getDimension(R.dimen.dp_20).toInt() else 0

        DLog.d("KKKKKK", "bottomHeight    = $bottomHeight")
        DLog.d("KKKKKK", "topViewHeight   = $topViewHeight")
        DLog.d("KKKKKK", "topHeight       = $topHeight")
        DLog.d("KKKKKK", "screenHeight    = $screenHeight")
        DLog.d("KKKKKK", "bottomStatusHei = $bottomStatusHei")
        DLog.d(
            "KKKKKK",
            "lp.height       = ${screenHeight - bottomHeight - topHeight - bottomStatusHei - topViewHeight}"
        )

        var lp = homeHotBind!!.linkage.layoutParams
        lp.height = screenHeight - bottomHeight - topHeight - bottomStatusHei - topViewHeight

        homeHotBind!!.linkage.layoutParams = lp
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }

    private class ElemeLinkagePrimaryAdapterConfig :
        ILinkagePrimaryAdapterConfig {
        private var mContext: Context? = null
        override fun setContext(context: Context) {
            mContext = context
        }

        override fun getLayoutId(): Int {
            return R.layout.home_main_ld_v_item_left
        }

        override fun getGroupTitleViewId(): Int {
            return R.id.name
        }

        override fun getRootViewId(): Int {
            return R.id.root_bg
        }

        override fun onBindViewHolder(
            holder: LinkagePrimaryViewHolder,
            selected: Boolean,
            title: String
        ) {
            val tvTitle = holder.mGroupTitle as AppTextView
            tvTitle.text = title
            tvTitle.setBackgroundColor(
                mContext!!.resources.getColor(
                    if (selected) R.color.F8F8F8 else R.color.white
                )
            )
            tvTitle.setTextColor(
                ContextCompat.getColor(
                    mContext!!,
                    if (selected) R.color.c_333333 else R.color.c_999999
                )
            )
            tvTitle.ellipsize =
                if (selected) TextUtils.TruncateAt.MARQUEE else TextUtils.TruncateAt.END
            tvTitle.isFocusable = selected
            tvTitle.isFocusableInTouchMode = selected

            tvTitle.marqueeRepeatLimit = if (selected) -1 else 0
        }

        override fun onItemClick(
            holder: LinkagePrimaryViewHolder,
            view: View,
            title: String
        ) {
            //TODO
        }
    }

    private class ElemeLinkageSecondaryAdapterConfig :
        ILinkageSecondaryAdapterConfig<ElemeGroupedItem.ItemInfo> {
        private var mContext: Context? = null
        override fun setContext(context: Context) {
            mContext = context
        }

        override fun getGridLayoutId(): Int {
            return R.layout.home_main_ld_v_item_right
        }

        override fun getLinearLayoutId(): Int {
            return R.layout.home_main_ld_v_item_right
        }

        override fun getHeaderLayoutId(): Int {
            return R.layout.home_main_ld_v_item_head
        }

        override fun getFooterLayoutId(): Int {
            return R.layout.home_main_ld_v_item_foot
        }

        override fun getHeaderTextViewId(): Int {
            return R.id.name
        }

        override fun getSpanCountOfGridMode(): Int {
            return 3
        }

        override fun onBindViewHolder(
            holder: LinkageSecondaryViewHolder,
            item: BaseGroupedItem<ElemeGroupedItem.ItemInfo>
        ) {
            if (item.info.item != null) {
                (holder.getView<View>(R.id.icon) as AppImageView).setImageUrl(item.info.item.img_url)

                holder.getView<View>(R.id.root_bg)
                    .setOnClickListener { v: View? ->
                        JugglePathUtils.instance.onJuggleItemClick(
                            item.info.item,
                            "LD",
                            posString = item.info.pos
                        )
                    }
            }
        }

        override fun onBindHeaderViewHolder(
            holder: LinkageSecondaryHeaderViewHolder,
            item: BaseGroupedItem<ElemeGroupedItem.ItemInfo>
        ) {
            (holder.getView<View>(R.id.name) as AppTextView).text = item.header
        }

        override fun onBindFooterViewHolder(
            holder: LinkageSecondaryFooterViewHolder,
            item: BaseGroupedItem<ElemeGroupedItem.ItemInfo>
        ) {
        }
    }
}