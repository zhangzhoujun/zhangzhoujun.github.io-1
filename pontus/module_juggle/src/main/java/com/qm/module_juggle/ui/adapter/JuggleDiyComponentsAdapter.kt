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
import com.qm.lib.router.RouterFragmentPath
import com.qm.lib.utils.JYComConst
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.JuggleMainItemDiyBinding
import com.qm.module_juggle.entity.MHomeDataBean


/**
 * @ClassName HomeTopAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class JuggleDiyComponentsAdapter(
    var context: Fragment,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem,
    var pageTag: String,
    var pageTagLast: String,
    var mDiyNum: Int
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context.context)
                .inflate(R.layout.juggle_main_item_diy, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: JuggleMainItemDiyBinding = DataBindingUtil.bind(holder.itemView)!!
        if (mDiyNum == 1) {
            bind.juggleItemDiyRootview.removeAllViews()
        }
        val homeFra =
            ARouter.getInstance().build(RouterFragmentPath.JuggleFra.JUGGLE_FRA_DIY)
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE, item.key)
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_TAG, pageTag)
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_COUNT, 1)
                .withSerializable(JYComConst.OPEN_CUSTOM_PAGE_TAG_LAST, pageTagLast)
                .navigation() as Fragment
        val transaction =
            context.fragmentManager!!.beginTransaction()
//        if (mDiyNum == 1) {
//            transaction.add(R.id.juggle_item_diy_root, homeFra)
//        } else {
        var frameLayout = FrameLayout(context.context!!)
        frameLayout.id =
            (Math.random() * 100000).toInt() + (Math.random() * 100000).toInt() + (Math.random() * 100000).toInt() + mDiyNum

        var lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        frameLayout.layoutParams = lp
        bind.juggleItemDiyRootview.addView(frameLayout)
        transaction.add(frameLayout.id, homeFra)
//        }

        transaction.commitAllowingStateLoss()
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}