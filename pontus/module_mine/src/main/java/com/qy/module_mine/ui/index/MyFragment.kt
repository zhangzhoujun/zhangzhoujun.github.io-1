package com.qy.module_mine.ui.index

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alibaba.android.arouter.facade.annotation.Route
import com.qm.lib.base.JYFragment
import com.qm.lib.router.RouterFragmentPath
import com.qy.module_mine.BR
import com.qy.module_mine.R
import com.qy.module_mine.databinding.MyFraMineBinding
import com.qy.module_mine.ui.index.MineViewModel


/**
 * @ClassName HomeFragment
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/13 7:29 PM
 * @Version 1.0
 */
@Route(path = RouterFragmentPath.Mine.MINE_INDEX)
class MyFragment : JYFragment<MyFraMineBinding, MineViewModel>() {

    override fun initContentView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): Int {
        return R.layout.my_fra_mine
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initData() {
        super.initData()
    }

    override fun initViewObservable() {
        super.initViewObservable()
    }

    private fun getBind(): MyFraMineBinding {
        return binding as MyFraMineBinding
    }

    private fun getVm(): MineViewModel {
        return viewModel as MineViewModel
    }
}