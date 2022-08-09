package com.qm.module_umshare.entity

/**
 * @ClassName MShareIndexBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/25 3:16 PM
 * @Version 1.0
 */
class MShareIndexBean(
    var share_img: String,
    var is_exclusive: Boolean,
    var button: MShareIndexButtonBean
) {

    class MShareIndexButtonBean(var general: String, var share: String)

}