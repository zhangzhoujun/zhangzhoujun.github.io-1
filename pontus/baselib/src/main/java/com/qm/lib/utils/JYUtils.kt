package com.qm.lib.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.qm.lib.R

/**
 * @ClassName JYMMKVManager
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/21 3:48 PM
 * @Version 1.0
 */
class JYUtils private constructor() {
    companion object {
        val instance: JYUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            JYUtils()
        }
    }

    fun getMetaDatByName(context: Context, name: String): String {
        val appInfo: ApplicationInfo = context.packageManager
            .getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
        var value = appInfo.metaData.getString(name, "")
        if (value.isNotEmpty() && value.startsWith("QM_ID")) {
            value = value.replace("QM_ID", "")
        }
        return value
    }


    fun getAnimByType(isIn: Boolean, type: String): Int {
        when (type) {
            "top" -> if (isIn) return R.anim.in_top else return R.anim.out_top
            "bottom" -> if (isIn) return R.anim.in_bottom else return R.anim.out_bottom
            "left" -> if (isIn) return R.anim.in_left else return R.anim.out_left
            "right" -> if (isIn) return R.anim.in_right else return R.anim.out_right
            "fade" -> if (isIn) return R.anim.mid_enter_anim else return R.anim.mid_exit_anim
        }
        return 0
    }

}