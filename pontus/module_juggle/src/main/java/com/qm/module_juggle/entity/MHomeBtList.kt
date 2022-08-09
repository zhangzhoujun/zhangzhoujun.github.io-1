package com.qm.module_juggle.entity

/**
 * @ClassName MHomeBtList
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/4 2:57 PM
 * @Version 1.0
 */
class MHomeBtList(
    var ntList: ArrayList<MHomeNtItem>
) {

    class MHomeNtItem(
        var stage: Int,
        var ntAmount: String
    )
}