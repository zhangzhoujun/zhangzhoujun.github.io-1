package com.qm.lib.entity

import android.text.TextUtils
import com.dim.library.utils.GsonUtils
import com.dim.library.utils.ToastUtils
import org.web3j.abi.datatypes.Bool
import java.io.Serializable

/**
 * @ClassName MAppConfigBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/22 12:33 PM
 * @Version 1.0
 */
class MAppConfigBean(
    var status: Int,
    var share:MAppShareInfoBean,
    var tabs: ArrayList<MAppConfigitem>,
    var blacklist: String,
    var must_login: Boolean,
    var privacy_policy: String,
    var user_agreement: String,
    var base_data: MAppBaseData
) : Serializable {

    class MAppConfigitem(
        var sub: List<MAppConfigitem>,
        var icon: String,
        var activation_icon: String,
        var label: String,
        var page_path: String,
        var text_align: String,
        // 黑白名单，用户userid，逗号分割
        var black_users: String,
        var white_users: String,

        var white_level: ArrayList<String>,
        var black_level: ArrayList<String>
    ) : Serializable

    class MAppShareInfoBean(
        var desc: String,
        var imgs: ArrayList<String>,
        var shareLink: String,
        var shareTypes: ArrayList<String>
    ) {
    }

    class MAppBaseData(
        var login_path: String,
        var feedback_url: String
    ){

    }
}