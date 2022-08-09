package com.qy.login.entity

/**
 * @ClassName MLoginResultBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/2 11:02 AM
 * @Version 1.0
 */
class MLoginResultBean(
    var id: Long,
    var refereesId: Long,
    var userIdentity: Int,
    var realName: String,
    var nickName: String,
    var authToken: String
) {
}