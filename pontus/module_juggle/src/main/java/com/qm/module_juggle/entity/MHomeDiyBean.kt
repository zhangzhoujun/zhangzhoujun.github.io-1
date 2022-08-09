package com.qm.module_juggle.entity

import java.io.Serializable

/**
 * @ClassName MHomeDiyBean
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 3/3/21 4:21 PM
 * @Version 1.0
 */
class MHomeDiyBean(
    var page_base: MHomeDataPage,
    var rows: ArrayList<MHomeDataItem>
) : Serializable {

    public class MHomeDataPage(
        // DIY
        var bg_url: String,
        var width_height: String,
        var radius: ArrayList<Float>,
        var title: String,
        var server_links: ArrayList<String>
    ) : Serializable

    class MHomeDataItem(
        var pageKey: String = "",

        // 当前页面的唯一标志，APP本地使用
        var pageTag: String,
        // 上一个页面的唯一标志，APP本地使用
        var lastPageTag: String,
        // 跳转链接
        var link: String,
        var is_show_bg_color: Boolean,
        var data_type: String,
        var text: String,
        var font_size: String,
        var font_weight: Boolean,
        var data_server_link: String,
        var font_color: String,
        var funs: ArrayList<String>,
        var trigger_img: String,
        var width_height: String,
        var text_align: String,
        var data_key: String,
        var id: String,
        var dialog_key: String,
        // 黑白名单，用户userid，逗号分割
        var white_users: String,
        var black_users: String,
        var relatively: MHomeDataItemRelatively,

        var white_level: List<List<String>>,
        var black_level: List<List<String>>,
        var radius: List<Float>,

        // 可见时间，限制时间，开始和结束
        var limit_time: List<String>,
        // 复制文案
        var copy_text: String,
        // 跳转到APP的路由+参数
        var app_path: String,
        // 服务端接口地址
        var server_link: String,
        var toast: String

    ) : Serializable

    class MHomeDataItemRelatively(
        var top: Double,
        var left: Double,
        var width: Double,
        var height: Double
    )
}