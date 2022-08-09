package com.qm.lib.entity

import java.io.Serializable

/**
 * @ClassName MJuggleGoodsBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/11/9 11:08 AM
 * @Version 1.0
 */
class MGoodsBean(
    var goodsList: ArrayList<MJuggleGoodsItemBean>
) : Serializable {
    class MJuggleGoodsItemBean(

        var goodsId: String,
        var goodsName: String,
        var goodsPic: String,
        var price: String,
        var originalPrice: String,
        var couponUsedPrice: String,
        var commission: String,
        var platform: String, // taobao
        var couponInfo: ArrayList<MJyggleGoodsCouponItem>,
        var imageInfo: ArrayList<String>,
        var clickUrl: String,
        var shopName: String,
        var sellNum: String
    ) : Serializable

    class MJyggleGoodsCouponItem(
        var discount: String,
        var couponStartTime: String,
        var couponEndTime: String,
        var couponStartFee: String
    ) : Serializable
}