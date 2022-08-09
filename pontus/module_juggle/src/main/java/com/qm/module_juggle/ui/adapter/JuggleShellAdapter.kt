package com.qm.module_juggle.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.dim.library.utils.DLog
import com.qm.lib.router.RouterManager
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.JuggleMainItemShellBinding
import com.qm.module_juggle.entity.MHomeDataBean


/**
 * @ClassName HomeTopAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class JuggleShellAdapter(
    var context: Fragment,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem,
    var pageTag: String,
    var pageTagLast: String,
    var mShellNum: Int
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context.context)
                .inflate(R.layout.juggle_main_item_shell, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        DLog.d("俄罗斯套娃", "item.id ${item.id}")
        var bind: JuggleMainItemShellBinding = DataBindingUtil.bind(holder.itemView)!!
//        val homeFra = ARouter.getInstance().build(item.id).navigation() as Fragment
        val fragment =
            RouterManager.instance.getNativeFragment("/ValueChain/task")

        val transaction =
            context.fragmentManager!!.beginTransaction()
//        transaction.add(R.id.juggle_item_shell_root, fragment!!)

        var frameLayout = FrameLayout(context.context!!)
        frameLayout.id = 500000 + (Math.random() * 100000).toInt() + (Math.random() * 100000).toInt() + (Math.random() * 100000).toInt() + mShellNum

        var lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = lp
        bind.rootview.addView(frameLayout)
        transaction.add(frameLayout.id, fragment!!)

        transaction.commitAllowingStateLoss()
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}