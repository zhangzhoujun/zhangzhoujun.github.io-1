package com.qm.module_juggle.entity

import java.io.Serializable

/**
 * @ClassName MHomeDataBean
 * @Description 后台配置的，直接跳原生页面的数据
 * 参考 https://www.yuque.com/xdnis1/ps25gq/ui171r
 * @Author zhangzhoujun
 * @Date 2020/10/27 10:24 AM
 * @Version 1.0
 */
public class MHomeDataBean(
    var page_base: MHomeDataPage,
    var rows: ArrayList<MHomeDataItem>
) : Serializable {

    /**
     * 模块item属性
     */
    class MHomeDataItem(
        // 是否是自定义组件
        var is_diy_components: Boolean = false,
        // 自定义组件的key
        var key: String,

        var mPageKey: String,
        var index: Int,
        // 样式
        var model_type: String,
        // model_type == 17 循环组件的时候，是不是直接取列表数据
        var type: String,
        // 标题
        var title: String,
        var id: String,
        var remarks: String,
        // 内容
        var data: ArrayList<MHomeDataItemData>,
        // 是否展示
        var state: Boolean,
        // 背景图URL
        var retouch: String,
        // 是否吸底  宽度撑满-堆图片 （model_type == 3） 才有
        var is_fixed_bottom: Boolean,
        // 宽度百分比 横向滚动-堆图片 （model_type == 4） 才有
        var item_width: Int,

        // model_type == 11 联动组件的时候才有 rgba(248, 248, 248, 1)
        // 整个的背景色
        var background_color: String,
        // tab切换的背景色
        var curr_background_color: String,
        // tab的字体颜色
        var curr_font_color: String,
        // 模块请求的连接，比如商品模块需要请求的地址
        var server_link: String,
        // type == 15 通证中心专有
        var main_bg_url: String,
        var title_font_color: String,
        var bt_icon: String,
        var content_font_color: String,
        // type == 16 豆腐块专有 1是一行2个  2是一行3个
        // type == 17 循环组件 1是一行1个  2是一行2个
        var skin: String,
        // 网络请求的地址
        var data_url: String,
        var method: String,
        // type == 30 登录组建专有
        var checkbox_line_color: String,
        var checkbox_bg_color: String,
        var login_name_text: String,
        var login_password_text: String,
        var description_text_color: String,
        var link_text_color: String,

        var components_data: MHomeDataItemDiyListData
    ) : Serializable {
        fun setPageTag(pageTag: String) {
            if (data != null) {
                for (index in 0 until data.size) {
                    data[index].pageTag = pageTag

                    if (data[index].data != null && data[index].data.size > 0) {
                        for (j in 0 until data[index].data.size) {
                            data[index].data[j].pageTag = pageTag
                        }
                    }
                }
            }
        }

        fun setPageKey(pageKey: String) {
            if (data != null) {
                for (index in 0 until data.size) {
                    data[index].pageKey = pageKey

                    if (data[index].data != null && data[index].data.size > 0) {
                        for (j in 0 until data[index].data.size) {
                            data[index].data[j].pageKey = pageKey
                        }
                    }
                }
            }
            mPageKey = pageKey
        }

        fun setLastPageTag(lastPageTag: String) {
            if (data != null) {
                for (index in 0 until data.size) {
                    data[index].lastPageTag = lastPageTag

                    if (data[index].data != null && data[index].data.size > 0) {
                        for (j in 0 until data[index].data.size) {
                            data[index].data[j].lastPageTag = lastPageTag
                        }
                    }
                }
            }
        }
    }

    class MHomeDataItemDiyListData(
        var key: String,
        // 当前请求的是第几页的数据，本地使用
        var page: Int
    ) {

    }

    /**
     * 模块item 中的具体配置
     */
    class MHomeDataItemData(
        // 当前页面的唯一标志
        var pageKey: String,
        // 当前页面的唯一标志，APP本地使用
        var pageTag: String,
        // 上一个页面的唯一标志，APP本地使用
        var lastPageTag: String,
        // 跳转链接
        var link: String,
        // 发送请求
        var baseURL: String,
        // 图片URL
        var img_url: String = "",
        // toast提示文案，有值即谈，不阻断后续逻辑
        var toast: String,
        // open_page： 打开页面，配合link，is_new_webview
        // copy： 复制，配合copy_text
        // toast： 提示文案，配合toast
        // get_open_url： 获取跳转链接，或者单纯的往服务端发个请求，配合server_link，link。同时存在，先发请求，回调根据link跳转，只有server_link，需要服务端必须返回跳转地址
        // submit_form： 触发页面上的表单提交，表单信息，另外配置
        var funs: ArrayList<String>,
        // 复制文案
        var copy_text: String,
        // 跳转到APP的路由+参数
        var app_path: String,
        // 服务端接口地址
        var server_link: String,
        var id: String,
        // 宽度百分比  自定义宽度-堆图片  （model_type == 9） 才有
        var item_width: Int,

        // model_type == 11 联动组件的时候才有
        // 标题
        var title: String,
        // 内容
        var data: ArrayList<MHomeDataItemData>,
        // type == 16 豆腐块专有
        var label: String,
        var schema_key: String,
        var sub_label: String,
        // 黑白名单，用户userid，逗号分割
        var white_users: String,
        var black_users: String,

        var white_level: List<List<String>>,
        var black_level: List<List<String>>,

        // 图片的宽高
        var width_height: String,
        // 弹窗的key
        var dialog_key: String,

        // 根据配置的服务端配置，是否显示当前模块,弹窗是否需要弹
        var is_show_to_server_link: String = "",

        var seldHide: Boolean = false,
        // 可见时间，限制时间，开始和结束
        var limit_time: List<String>,

        // 商品展示
        var goods_name: String,
        var main_img: String,
        var price: String,
        // var link: String,
        var coupon_link: String

    ) : Serializable

    /**
     * 页面上的基础配置
     */
    public class MHomeDataPage(
        // 标题
        var title: String,
        // 是否全屏
        var is_fulls_screen: Boolean,
        // 是不是要本地缓存
        var no_cache: Boolean = false,
        // 是否显示头部
        var has_head: Boolean,
        // 是否下拉刷新
        var is_pull_refresh: Boolean,
        // 是否显示返回顶部的按钮
        var is_show_to_top: Boolean,
        // 全局背景色
        var bg_color: String,
        // 对话框属性
        var mask_color: String,
        var mask_close: Boolean,
        var percentage: ArrayList<Int>,
        var offset_top: Int,
        var admission_animation: String,
        var appearance_animation: String,
        var dialogs: ArrayList<MHomeDataPageDialog>,
        var server_links: ArrayList<String>,
        // DIY
        var bg_url: String,
        var width_height: String,
        var radius: ArrayList<Int>
    ) : Serializable

    class MHomeDataPageDialog(
        var type: String,
        var interval_time: Int,
        var interval_type: String,
        var server_link: String,
        var value: String
    ) : Serializable
}

