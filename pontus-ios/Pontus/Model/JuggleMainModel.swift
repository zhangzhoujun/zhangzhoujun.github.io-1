//
//  JuggleMainModel.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/26.
//

import Foundation
import ObjectMapper
import SwiftyJSON

class JuggleMain: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        page_base <- map["page_base"]
        rows <- map["rows"]
    }
    
    var page_base: JugglePageBase?
    var rows: [JugglePageRows]?
}

class JugglePageBase: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        title <- map["title"]
        is_fulls_screen <- map["is_fulls_screen"]
        has_head <- map["has_head"]
        is_pull_refresh <- map["is_pull_refresh"]
        bg_color <- map["bg_color"]
        is_show_to_top <- map["is_show_to_top"]
        dialogs <- map["dialogs"]
        query <- map["query"]
        no_cache <- map["no_cache"]
        server_links <- map["server_links"]
        bg_url <- map["bg_url"]
        width_height <- map["width_height"]
        
        mask_color <- map["mask_color"]
        mask_close <- map["mask_close"]
        percentage <- map["percentage"]
        offset_top <- map["offset_top"]
        admission_animation <- map["admission_animation"]
        appearance_animation <- map["appearance_animation"]
    }
    
    var title: String?
    var is_fulls_screen: Bool?
    var has_head: Bool?
    var is_pull_refresh: Bool?
    var bg_color: String?
    var is_show_to_top: Bool?
    var dialogs: [JugglePageBaseDialog]?
    var query: [JugglePageBaseQuery]?
    var no_cache: Bool?
    var server_links: [String]?
    
    var bg_url: String?
    var width_height: String?
    
    // dialog
    var mask_color: String?
    var mask_close: Bool = true
    var percentage: [Int] = [0, 0]
    var offset_top: Int = 0
    var admission_animation: String?
    var appearance_animation: String?
}

class JugglePageBaseDialog: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        type <- map["type"]
        interval_time <- map["interval_time"]
        interval_type <- map["interval_type"]
        server_link <- map["server_link"]
        value <- map["value"]
    }
    
    var type: String?
    var interval_time: Int = 1
    var interval_type: String?
    var server_link: String?
    var value: String?
}

class JugglePageBaseQuery: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        key <- map["key"]
        value <- map["value"]
    }
    
    var key: String?
    var value: String?
}

class JugglePageRows: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        model_type <- map["model_type"]
        retouch <- map["retouch"]
        data <- map["data"]
        is_diy_components <- map["is_diy_components"]
        key <- map["key"]
        type <- map["type"]
        method <- map["method"]
        data_url <- map["data_url"]
        skin <- map["skin"]
        item_width <- map["item_width"]
        
        top <- map["top"]
        left <- map["left"]
        width <- map["width"]
        height <- map["height"]
        is_show_bg_color <- map["is_show_bg_color"]
        data_type <- map["data_type"]
        text <- map["text"]
        font_size <- map["font_size"]
        font_weight <- map["font_weight"]
        data_server_link <- map["data_server_link"]
        font_color <- map["font_color"]
        trigger_img <- map["trigger_img"]
        text_align <- map["text_align"]
        data_key <- map["data_key"]
        radius <- map["radius"]
        funs <- map["funs"]
        link <- map["link"]
        copy_text <- map["copy_text"]
        server_link <- map["server_link"]
        app_path <- map["app_path"]
        toast <- map["toast"]
        dialog_key <- map["dialog_key"]
        black_users <- map["black_users"]
        white_users <- map["white_users"]
        black_level <- map["black_level"]
        white_level <- map["white_level"]
        relatively <- map["relatively"]
        
        checkbox_line_color <- map["checkbox_line_color"]
        checkbox_bg_color <- map["checkbox_bg_color"]
        login_name_text <- map["login_name_text"]
        login_password_text <- map["login_password_text"]
        description_text_color <- map["description_text_color"]
        link_text_color <- map["link_text_color"]
    }
    var pagetag: String? // APP本地使用，标志页面
    
    var model_type: String?
    var retouch: String?
    var data: [JugglePageRowsData]?
    var is_diy_components: Bool?
    var key: String?
    
    // model_type为17，获取商品数据的时候区分是否需要请求数据
    var type: String?
    var method: String?
    var data_url: String?
    // 一行的个数，区分两种样式
    var skin: String?
    var item_width: Int = 50
    // 自定义组件的东西
    // var components_data
    
    // 自定义组件
    var top: Int?
    var left: Int?
    var width: Int?
    var height: Int?
    var is_show_bg_color: Bool?
    var data_type: String?
    var text: String?
    var font_size: String?
    var font_weight: Bool?
    var data_server_link: String?
    var font_color: String?
    var trigger_img: String?
    var text_align: String?
    var data_key: String?
    var radius: [Int]?
    var relatively: JugglePageRowsRelatively?
    var funs: [String]?
    var link: String?
    var copy_text: String?
    var server_link: String?
    var app_path: String?
    var toast: String?
    var dialog_key: String? 
    var black_users: String?
    var white_users: String?
    var black_level: [String]?
    var white_level: [String]?
    
    // 登录表单
    var checkbox_line_color: String?
    var checkbox_bg_color: String?
    var login_name_text: String?
    var login_password_text: String?
    var description_text_color: String?
    var link_text_color: String?
}

class JugglePageRowsRelatively: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        top <- map["top"]
        left <- map["left"]
        width <- map["width"]
        height <- map["height"]
    }
    
    var top: Double?
    var left: Double?
    var width: Double?
    var height: Double?
}

class JugglePageRowsData: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        link <- map["link"]
        is_new_webview <- map["is_new_webview"]
        img_url <- map["img_url"]
        toast <- map["toast"]
        funs <- map["funs"]
        copy_text <- map["copy_text"]
        server_link <- map["server_link"]
        app_path <- map["app_path"]
        id <- map["id"]
        black_users <- map["black_users"]
        white_users <- map["white_users"]
        dialog_key <- map["dialog_key"]
        width_height <- map["width_height"]
        black_level <- map["black_level"]
        white_level <- map["white_level"]
        is_show_to_server_link <- map["is_show_to_server_link"]
        limit_time <- map["limit_time"]
        item_width <- map["item_width"]
        
        goods_name <- map["goods_name"]
        price <- map["price"]
        coupon_link <- map["coupon_link"]
        main_img <- map["main_img"]
        
    }
    var pagetag: String? // APP本地使用，标志页面
    
    var is_new_webview: Bool?
    var img_url: String?
    var toast: String?
    var id: String?
    var funs: [String]?
    var link: String?
    var copy_text: String?
    var server_link: String?
    var app_path: String?
    var black_users: String?
    var white_users: String?
    var black_level: [[String]]?
    var white_level: [[String]]?
    var dialog_key: String?
    var width_height: String?
    var is_show_to_server_link: String?
    var limit_time: [Int]?
    var item_width: Double?
    
    // model_type为17，商品数据
    var goods_name: String?
    var price: String?
    var coupon_link: String?
    var main_img: String?
}

class JuggleServerData: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        is_show <- map["is_show"]
        url <- map["url"]
    }
    // 是否要显示
    var is_show = false
    // 服务端返回的跳转链接
    var url = ""
}

class JuggleServerLocalData {
    var jugglemain: JuggleMain?
    var dataCount: Int = 0
    var dataAddCount: Int = 0
    var dataList = [String : Data]()
}

class JuggleLoginLocalData{
    var accountHint: String = ""
    var passwordHint: String = ""
    var account: String = ""
    var password: String = ""
    var isAgree: Bool = false
}
