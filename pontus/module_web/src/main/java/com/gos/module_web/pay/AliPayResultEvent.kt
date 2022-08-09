package com.gos.module_web.pay

/**
 * @ClassName PayResultEvent
 * @Description TODO
 *
 * payType 支付方式       // 支付类型：alipay,wxpay
 * payStatus 支付结果     // 0：失败，1：成功，2：取消支付
 * @Author zhangzhoujun
 * @Date 2020/5/20 10:16 AM
 * @Version 1.0
 */
class AliPayResultEvent(var payType: String, var payStatus: String,var orderSn : String) {

    companion object {
        const val PAY_TYPE_ALI = "1" //支付宝支付

        const val PAY_TYPE_WEIXIN = "2" //微信支付

        const val PAYRESULT_OK = "200" //支付成

        const val PAYRESULT_CANCEL = "100" //支付取消

        const val PAYRESULT_FAILE = "300" //支付失败
    }


}