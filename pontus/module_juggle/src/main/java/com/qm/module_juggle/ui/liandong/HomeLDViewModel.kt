package com.qm.module_juggle.ui.liandong

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.qm.lib.base.BaseAppViewModel
import com.qm.module_juggle.BR
import com.qm.module_juggle.R
import com.qm.module_juggle.entity.MHomeDataBean
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @ClassName HomeHotViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/20 11:41 AM
 * @Version 1.0
 */
class HomeLDViewModel(application: Application) : BaseAppViewModel(application) {

    var observableList: ObservableList<HomeLDItemViewModel> =
        ObservableArrayList<HomeLDItemViewModel>()

    var itemBinding: ItemBinding<HomeLDItemViewModel> =
        ItemBinding.of<HomeLDItemViewModel>(BR.viewModel, R.layout.home_liandong_icon_item)
    val adapter: BindingRecyclerViewAdapter<HomeLDItemViewModel> =
        BindingRecyclerViewAdapter<HomeLDItemViewModel>()

    fun initData(list: ArrayList<MHomeDataBean.MHomeDataItemData>, pos: Int) {
        observableList.clear()
        for (i in 0 until list.size) {
            val item: MHomeDataBean.MHomeDataItemData = list[i]
            val itemViewModel =
                HomeLDItemViewModel(
                    this@HomeLDViewModel,
                    item,
                    "$pos-$i"
                )
            observableList.add(itemViewModel)
        }
    }
}