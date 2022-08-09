package com.qy.module_mine.ui.set

import android.os.Bundle
import android.view.View
import androidx.databinding.Observable
import com.alibaba.android.arouter.facade.annotation.Route
import com.dim.library.utils.VersionUtils
import com.qm.lib.base.JYActivity
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYDialogUtils
import com.qm.lib.utils.JYMMKVManager
import com.qy.module_mine.BR
import com.qy.module_mine.R
import com.qy.module_mine.databinding.MyActSetBinding

/**
 * @ClassName SetActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/3 5:08 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.Mine.MINE_SET)
class SetActivity : JYActivity<MyActSetBinding, SetViewModel>() {

    override fun initVariableId(): Int {
        return BR.viewModel
    }

    override fun initContentView(savedInstanceState: Bundle?): Int {
        return R.layout.my_act_set
    }

    override fun initData() {
        super.initData()

        getBind().icon.setImageBitmap(VersionUtils.getBitmap(this@SetActivity))
    }

    override fun initViewObservable() {
        super.initViewObservable()

        if (JYMMKVManager.instance.isAutoLogin()) {
            getBind().logout.visibility = View.VISIBLE
        } else {
            getBind().logout.visibility = View.INVISIBLE
        }
        getVm().showLogout.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                JYDialogUtils().showCommonDialog(
                    this@SetActivity,
                    "退出",
                    "确定要退出登录么？",
                    sureListener = object :
                        JYDialogUtils.JYDialogSureListener {
                        override fun onSureClick() {
                            getVm().logout()
                        }
                    })
            }
        })

        getVm().showSet.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                getVm().initTitle()
            }
        })

    }

    private fun getVm(): SetViewModel {
        return viewModel as SetViewModel
    }

    private fun getBind(): MyActSetBinding {
        return binding as MyActSetBinding
    }

    override fun onBackPressed() {
        if (getVm().showSet.get()) {
            super.onBackPressed()
        } else {
            getVm().showSet.set(true)
        }
    }

}