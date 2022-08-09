package com.qm.module_juggle.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.LayoutHelper
import com.dim.library.utils.DLog
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.ColorUtils
import com.qm.module_juggle.R
import com.qm.module_juggle.databinding.JuggleMainItemLoginBinding
import com.qm.module_juggle.entity.MHomeDataBean

/**
 * @ClassName JuggleLoginAdapter
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 2:01 PM
 * @Version 1.0
 */
class JuggleLoginAdapter(
    var context: Fragment,
    var helper: LayoutHelper,
    var item: MHomeDataBean.MHomeDataItem
) : DelegateAdapter.Adapter<RecyclerView.ViewHolder>() {

    lateinit var bind: JuggleMainItemLoginBinding

    private var isChose = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view =
            LayoutInflater.from(context.context)
                .inflate(R.layout.juggle_main_item_login, parent, false)

        return RecyclerViewItemHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return helper
    }

    fun getAccount(): String {
        return bind.account.text.toString()
    }

    fun getPassword(): String {
        return bind.password.text.toString()
    }

    fun getIsChose(): Boolean {
        return isChose
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        DLog.d("登录组件", "item.id ${item.id}")
        bind = DataBindingUtil.bind(holder.itemView)!!

        var roundBackDrawable = bind.roundBack.drawable
        roundBackDrawable.setTint(ColorUtils.instance.getColorForString(item.checkbox_bg_color))

        var roundDrawable = bind.round.drawable
        roundDrawable.setTint(ColorUtils.instance.getColorForString(item.checkbox_line_color))

        bind.iconBg.setImageUrl(item.retouch)

        bind.account.hint = item.login_name_text
        bind.password.hint = item.login_password_text

        bind.des1.setTextColor(ColorUtils.instance.getColorForString(item.description_text_color))
        bind.des3.setTextColor(ColorUtils.instance.getColorForString(item.description_text_color))
        bind.des2.setTextColor(ColorUtils.instance.getColorForString(item.link_text_color))
        bind.des4.setTextColor(ColorUtils.instance.getColorForString(item.link_text_color))

        bind.des4.setOnClickListener { RouterManager.instance.gotoYinsizhengceActivity() }
        bind.des2.setOnClickListener { RouterManager.instance.gotoYonghuXieyiActivity() }

        bind.roundBg.setOnClickListener {
            if (isChose) {
                bind.round.setImageDrawable(context.resources.getDrawable(R.drawable.lib_gouzi))
                isChose = false
            } else {
                bind.round.setImageDrawable(context.resources.getDrawable(R.drawable.lib_gouzi_chose))
                isChose = true
            }
            var roundDrawable = bind.round.drawable
            roundDrawable.setTint(ColorUtils.instance.getColorForString(item.checkbox_line_color))
        }
    }

    private class RecyclerViewItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
    }
}