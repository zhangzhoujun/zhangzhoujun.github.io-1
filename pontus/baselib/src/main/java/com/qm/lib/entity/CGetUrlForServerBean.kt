package com.qm.lib.entity

/**
 * @ClassName MGetUrlForServerBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/5 9:51 AM
 * @Version 1.0
 */
class CGetUrlForServerBean(
    var url: String,
    var linkUrl: String,
    var needColse: Boolean,
    var hasReload: Boolean,
    var pageTag: String,
    var lastPageTag: String
) {
}