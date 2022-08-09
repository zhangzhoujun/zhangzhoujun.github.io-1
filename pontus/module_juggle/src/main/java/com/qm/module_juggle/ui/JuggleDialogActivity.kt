package com.qm.module_juggle.ui

import android.app.Activity
import android.os.Bundle
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.callback.NavigationCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.dim.library.utils.ToastUtils
import com.qm.lib.entity.BaseResultBean
import com.qm.lib.http.BaseObserver
import com.qm.lib.http.RetrofitClient
import com.qm.lib.router.RouterActivityPath
import com.qm.lib.utils.JYComConst
import com.qm.lib.utils.StringUtils
import com.qm.module_juggle.entity.MHomeDataBean
import com.qm.module_juggle.http.HomeService
import io.reactivex.disposables.Disposable

/**
 * @ClassName JuggleDialogActivity
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/23 4:46 PM
 * @Version 1.0
 */
@Route(path = RouterActivityPath.JuggleAct.JUGGLE_DIALOG)
class JuggleDialogActivity : Activity() {

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE)
    var page_key: String = ""

    @JvmField
    @Autowired(name = JYComConst.OPEN_CUSTOM_PAGE_TAG)
    var page_tag: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)

        if (StringUtils.instance.isEmpty(page_key)) {
            ToastUtils.showShort("数据错误，请重试")
            finish()
            return
        }
        if (page_tag == null) {
            page_tag = ""
        }
        if (page_key.contains("=")) {
            var keyList = page_key.split("=")
            page_key = keyList[keyList.size - 1]
        }

        getHomeData()
    }

    private fun gotoDialog(data: MHomeDataBean) {
        val postcard: Postcard = ARouter.getInstance()
            .build(RouterActivityPath.JuggleAct.JUGGLE_DIALOG_SHOW)
        postcard.withSerializable(JYComConst.OPEN_CUSTOM_PAGE_DATA, data)
        postcard.withSerializable(JYComConst.OPEN_CUSTOM_PAGE, page_key)
        postcard.withSerializable(JYComConst.OPEN_CUSTOM_PAGE_TAG, page_tag)
        postcard.withTransition(0, 0)
        postcard.navigation(this, object : NavigationCallback {
            override fun onLost(postcard: Postcard?) {
                ToastUtils.showShort("数据错误，请重试")
                this@JuggleDialogActivity.finish()
            }

            override fun onFound(postcard: Postcard?) {

            }

            override fun onInterrupt(postcard: Postcard?) {
                ToastUtils.showShort("数据错误，请重试")
                this@JuggleDialogActivity.finish()
            }

            override fun onArrival(postcard: Postcard?) {
                this@JuggleDialogActivity.finish()
            }

        })
    }

    private fun getHomeData() {
        val service: HomeService = RetrofitClient.getInstance().create(HomeService::class.java)

        RetrofitClient.getInstance().execute(
            service.getPageConfig(page_key),
            object : ObserverNoDialog<BaseResultBean<MHomeDataBean>>() {
                override fun onSuccess(result: BaseResultBean<MHomeDataBean>) {
                    if (result.data == null || result.data.page_base == null) {
                        ToastUtils.showShort("数据错误，请重试")
                        this@JuggleDialogActivity.finish()
                        return
                    }
                    gotoDialog(result.data)
                }

                override fun onError(throwable: Throwable) {
                    super.onError(throwable)
                    ToastUtils.showShort("数据错误，请重试")
                    this@JuggleDialogActivity.finish()
                }
            })
    }

    override fun onBackPressed() {
        finish()
    }

    open class ObserverNoDialog<T> : BaseObserver<T>() {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
        }

        override fun onError(throwable: Throwable) {
            super.onError(throwable)
        }

        override fun onComplete() {
            super.onComplete()
        }

        override fun onSuccess(o: T) {}
        override fun onFailed(t: T) {
            super.onFailed(t)
        }

        override fun onNext(t: T) {
            super.onNext(t)
        }
    }
}