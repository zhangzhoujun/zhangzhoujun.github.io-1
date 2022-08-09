package com.dim.library.base

/**
 * @ClassName MultiItemViewModel
 * @Description RecycleView多布局ItemViewModel是封装
 * @Author zhangzhoujun
 * @Date 2020/5/12 5:32 PM
 * @Version 1.0
 */
open class MultiItemViewModel<VM : BaseViewModel?>(viewModel: VM) :
    ItemViewModel<VM?>(viewModel) {
    protected var multiType: Any? = null
    fun getItemType(): Any? {
        return multiType
    }

    fun multiItemType(multiType: Any) {
        this.multiType = multiType
    }
}
