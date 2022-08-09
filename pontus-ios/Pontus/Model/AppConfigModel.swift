//
//  AppConfigModel.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//
import Foundation
import ObjectMapper

class AppConfig: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        status <- map["status"]
        blacklist <- map["blacklist"]
        mustLogin <- map["must_login"]
        privacyPolicy <- map["privacy_policy"]
        userAgreement <- map["user_agreement"]
        tabs <- map["tabs"]
        share <- map["share"]
        base_data <- map["base_data"]
    }
    
    var status: Int = 0
    var blacklist: String?
    var mustLogin: Bool = false
    var privacyPolicy: String?
    var userAgreement: String?
    var tabs: [AppConfigTabs]?
    var share: AppConfigShare?
    var base_data: AppConfigBaseData?
}

class AppConfigTabs: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        icon <- map["icon"]
        label <- map["label"]
        pagePath <- map["page_path"]
        textAlign <- map["text_align"]
        blackLevel <- map["black_level"]
        blackUsers <- map["black_users"]
        whiteLevel <- map["white_level"]
        whiteUsers <- map["white_users"]
        activationIcon <- map["activation_icon"]
        sub <- map["sub"]
    }
    
    var icon: String?
    var label: String?
    var pagePath: String?
    var textAlign: String?
    var blackLevel: [String]?
    var blackUsers: String?
    var whiteLevel: [String]?
    var whiteUsers: String?
    var activationIcon: String?
    var sub: [AppConfigTabs]?
}

class AppConfigShare: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        desc <- map["desc"]
        imgs <- map["imgs"]
        shareLink <- map["share_link"]
        shareTypes <- map["share_types"]
    }
    
    var desc: String?
    var imgs: [String]?
    var shareLink: String?
    var shareTypes: [String]?
}

class AppConfigBaseData: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        login_path <- map["login_path"]
        feedback_url <- map["feedback_url"]
    }
    
    var login_path: String?
    var feedback_url: String?
}
