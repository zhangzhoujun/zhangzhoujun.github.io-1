package com.qm.lib.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils
import com.dim.library.utils.ToastUtils
import com.dim.library.utils.Utils
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * @ClassName ColorUtils
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/29 10:38 AM
 * @Version 1.0
 */
class StringUtils private constructor() {
    companion object {
        val instance: StringUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            StringUtils()
        }
    }

    fun isEmpty(string: String): Boolean {
        try {
            if (null == string || TextUtils.isEmpty(string)) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return true
        }
        return false
    }

    fun getStringSplit(string: String, split: String): List<String> {
        if (TextUtils.isEmpty(string)) {
            return ArrayList<String>()
        }
        if (!string.contains(split)) {
            var arrayList = ArrayList<String>()
            arrayList.add(string)
            return arrayList
        }

        return string.split(split)
    }

    fun getStringSplit(string: String, split: String, pos: Int): String {
        if (TextUtils.isEmpty(string)) {
            return ""
        }
        if (!string.contains(split)) {
            return string
        }

        var arrayList = string.split(split)
        if (pos < arrayList.size) {
            return arrayList[pos]
        }
        return arrayList[0]
    }

    fun doCopy(copyString: String) {
        if (TextUtils.isEmpty(copyString)) {
            return
        }
        //获取剪贴板管理器：
        val cm: ClipboardManager? =
            Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val mClipData = ClipData.newPlainText("Label", copyString)
        cm!!.setPrimaryClip(mClipData)

        ToastUtils.showShort("复制成功")
    }

    /**
     * 字符串纯数字判断
     *
     * @param str
     * @return 纯数字组成返回true
     */
    fun isNumeric(str: String?): Boolean {
        if (TextUtils.isEmpty(str)) {
            return false
        }
        val pattern = Pattern.compile("[0-9]*")
        return pattern.matcher(str).matches()
    }

    /**
     * 判断字符串是否为URL
     * @param urls 用户头像key
     * @return true:是URL、false:不是URL
     */
    fun isHttpUrl(urls: String): Boolean {
        if (urls.startsWith("http") || urls.startsWith("www")) {
            return true
        }
        var isurl = false
        val regex = ("(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)") //设置正则表达式
        val pat =
            Pattern.compile(regex.trim { it <= ' ' }) //比对
        val mat: Matcher = pat.matcher(urls.trim { it <= ' ' })
        isurl = mat.matches() //判断是否匹配
        if (isurl) {
            isurl = true
        } else {
            // 判断短链
            val regex =
                ("((http|https|ftp|ftps):\\/\\/)?([a-zA-Z0-9-]+\\.){1,5}(com|cn|net|org|hk|tw)((\\/(\\w|-)+(\\.([a-zA-Z]+))?)+)?(\\/)?(\\??([\\.%:a-zA-Z0-9_-]+=[#\\.%:a-zA-Z0-9_-]+(&amp;)?)+)?") //设置正则表达式
            val pat =
                Pattern.compile(regex.trim { it <= ' ' }) //比对
            val mat: Matcher = pat.matcher(urls.trim { it <= ' ' })
            isurl = mat.matches() //判断是否匹配
            if (isurl) {
                isurl = true
            }
        }
        return isurl
    }
}