package com.qm.module_umshare.entity

/**
 * @ClassName MOnlyDialogBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/16 3:42 PM
 * @Version 1.0
 */
class MOnlyDialogBean(
    var list: ArrayList<MOnlyDialogItemBean>
) {

    class MOnlyDialogItemBean(
        var key: String,
        var value: String
    )

}