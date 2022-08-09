package com.qy.dodule_goods.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qm.lib.base.JYActivity
import com.qm.lib.entity.MGoodsBean
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qy.dodule_goods.BR
import com.qy.dodule_goods.R
import com.qy.dodule_goods.databinding.GoodsActMainBinding

/**
 * @ClassName GoodsActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/9 3:53 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Goods.GOODS_MAIN)
class GoodsActivity : JYActivity<GoodsActMainBinding, GoodsViewModel>() {

    @JvmField
    @Autowired(name = JYComConst.GOODS_DETAIL)
    var goodsBean: MGoodsBean.MJuggleGoodsItemBean? = null

    override fun initData() {
        super.initData()
        ARouter.getInstance().inject(this)
        if (goodsBean == null) {
            finish()
        }

        val layoutManager: LinearLayoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically(): Boolean {
                // 直接禁止垂直滑动
                return false
            }
        }
        getBind().recyclerview.layoutManager = layoutManager
        getVm().onViewInit(goodsBean!!, getBind(), this)
    }

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.goods_act_main
    }

    private fun getBind(): GoodsActMainBinding {
        return binding as GoodsActMainBinding
    }

    private fun getVm(): GoodsViewModel {
        return viewModel as GoodsViewModel
    }
}