//
//  UserModel.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/12/1.
//

import Foundation
import ObjectMapper

class UserBean: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        app_key <- map["app_key"]
        app_name <- map["app_name"]
        token <- map["token"]
        invitation_code <- map["invitation_code"]
        id <- map["id"]
        level <- map["level"]
        mobile <- map["mobile"]
        nick_name <- map["nick_name"]
        real_name <- map["real_name"]
        id_card <- map["id_card"]
        avatar <- map["avatar"]
    }
    
    var app_key: String?
    var app_name: String?
    var token: String?
    var invitation_code: String?
    var id: Int = 0
    var level: Int = 0
    var mobile: String = ""
    var nick_name: String?
    var real_name: String?
    var id_card: String?
    var avatar: String?
}

class VersionModel: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        comment <- map["comment"]
        url <- map["url"]
        version <- map["version"]
        isExamined <- map["isExamined"]
        update <- map["update"]
        forceUpdate <- map["forceUpdate"]
    }
    
    var comment: String?
    var url: String?
    var version: String?
    var isExamined: Int = 0
    var update: Bool = false
    var forceUpdate: Bool = false
}

class OSSConfigModel: Mappable{
    required init?(map: Map) {}
    
    func mapping(map: Map) {
        AccessKeyId <- map["AccessKeyId"]
        Signature <- map["Signature"]
        Expire <- map["Expire"]
        Host <- map["Host"]
        Directory <- map["Directory"]
        cdn_domain <- map["cdn_domain"]
        Policy <- map["Policy"]
    }
    
    var AccessKeyId: String?
    var Signature: String = ""
    var Expire: String = ""
    var Host: String = ""
    var Directory: String = ""
    var cdn_domain: String = ""
    var Policy: String = ""
}
