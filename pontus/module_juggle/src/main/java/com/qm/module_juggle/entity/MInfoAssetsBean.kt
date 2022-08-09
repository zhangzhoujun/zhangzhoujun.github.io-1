package com.qm.module_juggle.entity

/**
 * @ClassName MInfoAssets
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/5 10:26 AM
 * @Version 1.0
 */
class MInfoAssetsBean(
    var title: String,
    var rightTitle: String,
    var rightUrl: String,
    var list: ArrayList<MInfoAssetsItem>
) {
    class MInfoAssetsItem(
        var icon: String,
        var value: String,
        var key: String,
        var url: String
    )
}