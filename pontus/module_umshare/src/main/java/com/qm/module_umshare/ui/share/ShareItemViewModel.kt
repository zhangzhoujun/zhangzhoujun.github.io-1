package com.qm.module_umshare.ui.share

import androidx.databinding.ObservableField
import com.dim.library.base.MultiItemViewModel
import com.dim.library.binding.command.BindingAction
import com.dim.library.binding.command.BindingCommand
import com.dim.library.utils.ToastUtils
import com.qm.lib.utils.RuntimeData
import com.qm.module_umshare.entity.CShareItemBean

/**
 * @ClassName HomeItemViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/14 5:33 PM
 * @Version 1.0
 */
class ShareItemViewModel(
    viewModel: ShareMainViewModel,
    data: CShareItemBean,
    listener: OnShareItemClickListener
) :
    MultiItemViewModel<ShareMainViewModel?>(viewModel) {
    var item: ObservableField<CShareItemBean> = ObservableField<CShareItemBean>()

    init {
        item.set(data)
    }

    var onItemCLick: BindingCommand<Any> = BindingCommand(object : BindingAction {
        override fun call() {
                listener?.onItemClick(item.get()!!.type)
        }
    })

    interface OnShareItemClickListener {
        fun onItemClick(type: String)
    }
}