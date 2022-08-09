package com.qm.lib.utils

import android.graphics.Color
import android.text.TextUtils

/**
 * @ClassName ColorUtils
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/10/29 10:38 AM
 * @Version 1.0
 */
class ColorUtils private constructor() {
    companion object {
        val instance: ColorUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ColorUtils()
        }
    }

    // rgba(248, 248, 248, 1)
    private fun getColorByString(colorString: String): String {
        if (!colorString.startsWith("rgba(")) {
            return "#FFFFFF"
        }
        var color = colorString.replace("rgba(", "").replace(")", "").replace(" ", "")
        val colorArray = color.split(",")
        if (colorArray.size < 4) {
            return "#FFFFFF"
        }
        return "#" + getAlphaToHex(colorArray[3]) + getInttoHex(colorArray[0]) + getInttoHex(
            colorArray[1]
        ) + getInttoHex(colorArray[2])
    }

    private fun getAlphaToHex(color: String): String {
        var colorHex = Integer.toHexString((color.toFloat() * 255).toInt())
        if (colorHex.length < 2) {
            return "0$colorHex"
        }
        return colorHex
    }

    private fun getInttoHex(color: String): String {
        var colorHex = Integer.toHexString(color.toInt())
        if (colorHex.length < 2) {
            return "0$colorHex"
        }
        return colorHex
    }

    fun getColorForString(color: String): Int {
        if (TextUtils.isEmpty(color)) {
            return Color.parseColor("#00FFFFFF")
        }
        if(color.length < 7){
            return Color.parseColor("#00FFFFFF")
        }
        if (color.startsWith("#")) {
            return Color.parseColor(color)
        }
        return Color.parseColor(getColorByString(color))
    }
}