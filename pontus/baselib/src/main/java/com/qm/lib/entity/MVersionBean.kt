package com.qm.lib.entity

import com.dim.library.utils.Utils
import com.qm.lib.utils.CompareVersion
import com.qm.lib.utils.SystemUtil

/**
 * @ClassName MVersionBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/22 11:17 AM
 * @Version 1.0
 */
class MVersionBean(
    var comment: String,
    var url: String,
    var version: String,
    var isExamined: Int,
    var update: Boolean,
    var forceUpdate: Boolean = false
)