package com.qm.module_juggle.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.dim.library.utils.DLog
import com.google.gson.JsonObject
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.JYComConst
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.JuggleMainItemDiyBinding

/**
 * @ClassName JuggleDiyComponentsListAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2021/11/19 4:36 下午
 * @Version 1.0
 */
class JuggleDiyComponentsListAdapter(
    var context: Fragment,
    var helper: LayoutHelper,
    var count: Int,
    var pageTag: String,
    var pageTagLast: String
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {
    var key: String = ""
    var list: ArrayList<JsonObject>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context.context)
                .inflate(R.layout.juggle_main_item_diy, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        var count = if (list == null) 0 else list!!.size
        return count
    }

    fun setPageData(key: String, list: ArrayList<JsonObject>) {
        this.list = list
        this.key = key
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        DLog.d("FUCKFUCK", "key = $key , position - > $position ; data = ")
        var bind: JuggleMainItemDiyBinding = DataBindingUtil.bind(holder.itemView)!!

        if(bind.juggleItemDiyRootview.childCount > 0){
            DLog.d("FUCKFUCK", "ADD_VIEW  position - > $position -> is added")
            return
        }

        val homeFra =
            ARouter.getInstance().build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_DIY)
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE, key)
                .withSerializable(
                    JYComConst.OPEN_CUSTOM_PAGE_SERVER_DATA,
                    list!![position].toString()
                )
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_COUNT, count)
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_TAG, pageTag)
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_TAG_LAST, pageTagLast)
                .navigation() as Fragment
        val transaction =
            context.fragmentManager!!.beginTransaction()

        var frameLayout = FrameLayout(context.context!!)
        frameLayout.id =
            (Math.random() * 100000).toInt() + (Math.random() * 100000).toInt() + (Math.random() * 100000).toInt() + +(Math.random() * 1000000).toInt()

        var lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = lp
        bind.juggleItemDiyRootview.addView(frameLayout)
        transaction.add(frameLayout.id, homeFra)

        transaction.commitAllowingStateLoss()
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}