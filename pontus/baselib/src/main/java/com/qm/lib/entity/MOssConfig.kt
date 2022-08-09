package com.qm.lib.entity

/**
 * @ClassName MOssConfig
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/6/23 8:28 PM
 * @Version 1.0
 */
class MOssConfig(
    var AccessKeyId: String,
    var Signature: String,
    var Expire: String,
    var Host: String,
    var Directory: String,
    var cdn_domain: String,
    var Policy: String
) {
}