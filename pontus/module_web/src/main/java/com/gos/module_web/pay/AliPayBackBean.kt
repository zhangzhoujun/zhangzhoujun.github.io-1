package com.gos.module_web.pay

import android.text.TextUtils

/**
 * @ClassName AliPayBackBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/20 9:48 AM
 * @Version 1.0
 */
class AliPayBackBean {
    /**
     * {
     * resultStatus=9000,
     * result={
     * "alipay_trade_app_pay_response": {
     * "code": "10000",
     * "msg": "Success",
     * "app_id": "2016073100135246",
     * "auth_app_id": "2016073100135246",
     * "charset": "utf-8",
     * "timestamp": "2017-01-04 16:30:27",
     * "total_amount": "0.01",
     * "trade_no": "2017010421001004500200140633",
     * "seller_id": "2088102169336845",
     * "out_trade_no": "0104163011-1106"
     * },
     * "sign": "q4XLuI8k9sfv5bPoinjNiWdIespGrKOkxVGq4XGtj3FXSk2CEu4JrCYp4WhDv4nyPR
     * +cDi8enuRJpzaHZWmHI23P0cCnrMz5b+ygx08e9FzCIs+SuEo/FStGtIHm4fo
     * /2W2MfYb45D8W42UqUZiVITwYUXr1749G0gAzbGhqLbKj03xRmEW
     * /tYk6XdZUSIuSDGWhnhR448cIDETJrLIXkxIn5N42d69jf746onzhkLeY6Y+uCQtHqYO+4DNzLhDJhN
     * /4OG8uEi2M3dIPqKwtld19yAhbO/uNmGxisIZ3obouRrt2x0Ldr1XZjmymMhCRipA35hz+IF7s/Sss/DN3hw==",
     * "sign_type": "RSA2"
     * },
     * memo=""
     * }
     */
    private lateinit var resultStatus: String
    private lateinit var result: String
    private lateinit var memo: String

    constructor(rawResult: Map<String?, String?>?) {
        if (rawResult == null) {
            return
        }
        for (key in rawResult.keys) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = rawResult[key].toString()
            } else if (TextUtils.equals(key, "result")) {
                result = rawResult[key].toString()
            } else if (TextUtils.equals(key, "memo")) {
                memo = rawResult[key].toString()
            }
        }
    }

    override fun toString(): String {
        return ("resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}")
    }

    private fun gatValue(content: String, key: String): String? {
        val prefix = "$key={"
        return content.substring(
            content.indexOf(prefix) + prefix.length,
            content.lastIndexOf("}")
        )
    }

    /**
     * @return the resultStatus
     */
    fun getResultStatus(): String {
        return resultStatus
    }

    /**
     * @return the memo
     */
    fun getMemo(): String {
        return memo
    }

    /**
     * @return the result
     */
    fun getResult(): String {
        return result
    }
}