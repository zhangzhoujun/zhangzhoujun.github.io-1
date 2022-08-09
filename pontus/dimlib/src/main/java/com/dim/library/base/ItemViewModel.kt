package com.dim.library.base

/**
 * @ClassName ItemViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/12 5:31 PM
 * @Version 1.0
 */
open class ItemViewModel<VM : BaseViewModel?>(viewModel: VM) {
    protected var viewModel: VM = viewModel
}
