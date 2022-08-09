package com.qm.lib.entity

/**
 * @ClassName COssConfig
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/23 8:37 PM
 * @Version 1.0
 */
class COssConfig(
    var OSSAccessKeyId: String,
    var policy: String,
    var Signature: String,
    var success_action_status: String,
    var key: String
) {


}