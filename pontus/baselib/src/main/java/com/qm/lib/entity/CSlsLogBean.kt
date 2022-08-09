package com.qm.lib.entity

/**
 * @ClassName CSlsLogBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 1/15/21 7:21 PM
 * @Version 1.0
 */
class CSlsLogBean(
    var userID: String,
    var userToken: String,
    var appType: String,
    var mobile: String,
    var deviceID: String,
    var pageKey: String,
    var type: String,
    var pos: String,
    var version: String,
    var appId: String,
    var appName: String,
    var timeStamp: String,
    var viewID: String,
    var imgUrl: String,
    var host: String,
    var event: String,
    var pageName: String,
    var umId: String,
    var phoneModel: String,
    var phoneBrand: String,
    var phoneVersion: String,
    var extra: String = ""
) {
}