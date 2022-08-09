package com.qm.module_juggle.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.dim.library.utils.DLog
import com.qm.lib.base.LocalUserManager
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.StringUtils
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.HomeMainMineBinding
import com.qm.module_juggle.entity.MHomeDataBean

/**
 * @ClassName HomeMineAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/3 7:51 PM
 * @Version 1.0
 */
class HomeMineAdapter(
    var context: Context,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.home_main_mine, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var bind: HomeMainMineBinding = DataBindingUtil.bind(holder.itemView)!!
        if (LocalUserManager.instance.getUser() == null) {
            return
        }

        if (LocalUserManager.instance.getUser() != null) {
            bind.code.text = "邀请码：" + LocalUserManager.instance.getUser()!!.invitation_code
            bind.vip.text = LocalUserManager.instance.getUser()!!.level
        }
//        bind.address.text = "ID：" + LocalUserManager.instance.getUserPublickey()
        bind.head.setImageUrlRound(LocalUserManager.instance.getUserHead())
        bind.mobile.text = LocalUserManager.instance.getUserNickname()
//        bind.quantity.text = "账户价值：¥${LocalUserManager.instance.getQuantity()}"
        bind.copy.setOnClickListener { onCopyClick() }
        bind.quantity.setOnClickListener { onQuanyiClick() }
        bind.head.setOnClickListener { onInfoClick() }
        bind.mobile.setOnClickListener { onInfoClick() }
        bind.code.setOnClickListener { onInfoClick() }
        bind.set.setOnClickListener { onSetClick() }
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }


    private fun onSetClick() {
        DLog.d("MineViewModel", "onSetClick")
        RouterManager.instance.gotoMineSetActivity()
    }

    private fun onInfoClick() {
        DLog.d("MineViewModel", "onInfoClick")
        RouterManager.instance.gotoInfoModifyActivity()
    }

    private fun onCopyClick() {
        DLog.d("MineViewModel", "onCopyClick")
        if (LocalUserManager.instance.getUser() != null) {
            StringUtils.instance.doCopy(LocalUserManager.instance.getUser()!!.invitation_code)
        }
    }

    private fun onQuanyiClick() {
        DLog.d("MineViewModel", "onQuanyiClick")

    }
}