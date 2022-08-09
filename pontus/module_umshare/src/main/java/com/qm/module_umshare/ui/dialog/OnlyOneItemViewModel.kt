package com.qm.module_umshare.ui.dialog

import androidx.databinding.ObservableField
import com.dim.library.base.MultiItemViewModel
import com.qm.module_umshare.entity.MOnlyDialogBean

/**
 * @ClassName HomeItemViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/14 5:33 PM
 * @Version 1.0
 */
class OnlyOneItemViewModel(
    viewModel: OnlyOneDialogViewModel,
    data: MOnlyDialogBean.MOnlyDialogItemBean
) :
    MultiItemViewModel<OnlyOneDialogViewModel>(viewModel) {
    var item: ObservableField<MOnlyDialogBean.MOnlyDialogItemBean> =
        ObservableField<MOnlyDialogBean.MOnlyDialogItemBean>()

    init {
        item.set(data)
    }
}