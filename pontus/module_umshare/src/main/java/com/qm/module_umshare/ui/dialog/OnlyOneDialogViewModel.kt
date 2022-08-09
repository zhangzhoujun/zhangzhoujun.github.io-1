package com.qm.module_umshare.ui.dialog

import android.app.Application
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import com.qm.lib.base.BaseAppViewModel
import com.qm.lib.base.BaseBean
import com.qm.lib.base.LocalUserManager
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.http.RetrofitClient
import com.qm.lib.widget.dim.AppImageView
import com.qm.lib.widget.dim.AppTextView
import com.qm.module_umshare.BR
import com.qm.module_umshare.R
import com.qm.module_umshare.entity.MOnlyDialogBean
import com.qm.module_umshare.http.ShareService
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 * @ClassName OnlyOneDialogViewModel
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/16 3:18 PM
 * @Version 1.0
 */
class OnlyOneDialogViewModel(application: Application) : BaseAppViewModel(application) {

    private lateinit var mIconBg: AppImageView
    private lateinit var mBtCount: AppTextView

    var observableList: ObservableList<OnlyOneItemViewModel> = ObservableArrayList()

    var itemBinding: ItemBinding<OnlyOneItemViewModel> =
        ItemBinding.of(BR.viewModel, R.layout.um_item_one_dialog)
    val adapter: BindingRecyclerViewAdapter<OnlyOneItemViewModel> =
        BindingRecyclerViewAdapter<OnlyOneItemViewModel>()

    override fun onCreate() {
        super.onCreate()

//        getPopData()
    }

    fun initDialog(icon: AppImageView, btCount: AppTextView) {
        mIconBg = icon
        mBtCount = btCount
        if (LocalUserManager.instance.getUserIdentity() == "10") {
            mIconBg.setBackgroundResource(R.drawable.um_one_dialog_bg_vip)
        } else if (LocalUserManager.instance.getUserIdentity() == "20") {
            mIconBg.setBackgroundResource(R.drawable.um_one_dialog_bg_super)
        }
//        mBtCount.text = "${LocalUserManager.instance.getUserTotalAmount()}BT"
    }

    fun onSureClick(view: View) {
        sendPopSure()
    }

    private fun getPopData() {
        val service = RetrofitClient.getInstance().create(ShareService::class.java)

        RetrofitClient.getInstance()
            .execute(service.getHomePopUp(getRequestBody(BaseBean<Any>())),
                object : AppObserver<BaseResultBean<MOnlyDialogBean>>() {
                    override fun onSuccess(o: BaseResultBean<MOnlyDialogBean>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            if (o.data.list.isEmpty()) {
                                finish()
                            }
                            for (index in 0 until o.data.list.size) {
                                observableList.add(
                                    OnlyOneItemViewModel(
                                        this@OnlyOneDialogViewModel,
                                        MOnlyDialogBean.MOnlyDialogItemBean(
                                            "${o.data.list[index].key}：",
                                            o.data.list[index].value
                                        )
                                    )
                                )
                            }
                        } else {
                            finish()
                        }
                    }

                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        finish()
                    }
                })
    }

    private fun sendPopSure() {
        val service = RetrofitClient.getInstance().create(ShareService::class.java)

        RetrofitClient.getInstance()
            .execute(service.sendHomePopSure(getRequestBody(BaseBean<Any>())),
                object : AppObserver<BaseResultBean<Any>>() {
                    override fun onSuccess(o: BaseResultBean<Any>) {
                        super.onSuccess(o)
                        if (o.doesSuccess()) {
                            if (LocalUserManager.instance.getUser() != null) {
//                                LocalUserManager.instance.getUser()!!.isPopUp = true
                            }
                            finish()
                        }
                    }
                })
    }
}