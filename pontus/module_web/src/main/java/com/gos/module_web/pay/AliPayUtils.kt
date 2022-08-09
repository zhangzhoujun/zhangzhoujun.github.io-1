package com.gos.module_web.pay

import android.app.Activity
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.dim.library.bus.RxBus
import com.dim.library.utils.DLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DefaultObserver
import io.reactivex.schedulers.Schedulers

/**
 * @ClassName AliPayUtils
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/20 9:47 AM
 * @Version 1.0
 */
class AliPayUtils {

    fun startAliPay(activity: Activity?, sign: String, orserSn: String) {
        DLog.d("支付url : $sign")
        val observable: Observable<AliPayBackBean?>? =
            Observable.create { emitter -> // 构建PayTask 对象
                val task = PayTask(activity)
                // 调用支付接口，获取支付结果
                val aliPayBean = AliPayBackBean(task.payV2(sign, true))
                emitter.onNext(aliPayBean)
                emitter.onComplete()
            }
        observable?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : DefaultObserver<AliPayBackBean?>() {
                override fun onNext(aliPayBean: AliPayBackBean) {
                    DLog.d("支付返回 ... ")
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    val resultInfo: String = aliPayBean.getResult() // 同步返回需要验证的信息
                    val resultStatus: String = aliPayBean.getResultStatus()
                    DLog.d("Ali支付结果：$aliPayBean")
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        DLog.d("支付返回 ... 9000")

                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        RxBus.getDefault().post(
                            AliPayResultEvent(
                                AliPayResultEvent.PAY_TYPE_ALI,
                                AliPayResultEvent.PAYRESULT_OK, orserSn
                            )
                        )
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        DLog.d("支付返回 ... 6001")
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        RxBus.getDefault().post(
                            AliPayResultEvent(
                                AliPayResultEvent.PAY_TYPE_ALI,
                                AliPayResultEvent.PAYRESULT_CANCEL, orserSn
                            )
                        )
                    } else {
                        DLog.d("支付返回 ... 支付失败")
                        RxBus.getDefault().post(
                            AliPayResultEvent(
                                AliPayResultEvent.PAY_TYPE_ALI,
                                AliPayResultEvent.PAYRESULT_FAILE, orserSn
                            )
                        )
                    }
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })
    }

}