package com.qm.lib.entity

/**
 * @ClassName MBaseConfig
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/8/11 10:34 AM
 * @Version 1.0
 */
class MBaseConfig(
    var config: MBaseConfigBean
) {

    class MBaseConfigBean(
        var privacy_policy: String,
        var user_agreement: String,
        var address_management: String,
        var dese_download_android: String,
        var order_confirm: String,
        var order_list: MBaseConfigList
    )

    class MBaseConfigList(
        var all: String,
        var wait_pay: String,
        var wait_delivery: String,
        var wait_received: String,
        var finished: String
    )
}