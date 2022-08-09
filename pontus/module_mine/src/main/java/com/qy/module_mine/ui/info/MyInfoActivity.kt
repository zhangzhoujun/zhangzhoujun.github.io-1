package com.qy.module_mine.ui.info

import android.os.Bundle
import androidx.databinding.Observable
import com.alibaba.android.arouter.facade.annotation.Route
import com.dim.library.utils.ToastUtils
import com.qm.lib.base.JYPictureActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.router.RouterManager
import com.qm.lib.utils.JYMMKVManager
import com.qy.module_mine.BR
import com.qy.module_mine.R
import com.qy.module_mine.databinding.MyActInfoBinding

/**
 * @ClassName MianSetActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/18 10:17 AM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Mine.INFO)
class MyInfoActivity : JYPictureActivity<MyActInfoBinding, InfoViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.my_act_info
    }

    override fun initData() {
        super.initData()
    }

    override fun initViewObservable() {
        super.initViewObservable()

        getVm().showInfo.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                getVm().initTitle()
            }
        })

        if (!JYMMKVManager.instance.isAutoLogin()) {
            ToastUtils.showShort("请先登录")
            RouterManager.instance.gotoLoginActivity()
            finish()
        }
    }

    private fun getVm(): InfoViewModel {
        return viewModel as InfoViewModel
    }

    override fun onBackPressed() {
        if (getVm().showInfo.get()) {
            super.onBackPressed()
        } else {
            getVm().showInfo.set(true)
        }
    }
}