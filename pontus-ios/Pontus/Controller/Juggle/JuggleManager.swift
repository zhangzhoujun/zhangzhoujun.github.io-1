//
//  JuggleManager.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/12/1.
//

import Foundation
import UIKit

let JUGGLE_LOGIN_SCUCCESS: String = "juggle_login_success"
let JUGGLE_USERINFO_SCUCCESS: String = "juggle_userinfo_success"
let JUGGLE_USERINFO_LIGOUT: String = "juggle_userinfo_ligout"

let JUGGLE_DIALOG_DISMISS: String = "juggle_dialog_dismiss"
let JUGGLE_DIALOG_CLICK: String = "juggle_dialog_click"

let JUGGLE_INFO_CHANGED: String = "juggle_info_changed"
let JUGGLE_ITEM_CLICK: String = "juggle_item_click"
let JUGGLE_ITEM_TYPE: String = "juggle_item_type"
let JUGGLE_CLOSE_WEBVIEW: String = "juggle_close_webview"
let JUGGLE_RELOAD_WEBVIEW: String = "juggle_reload_webview"
let JUGGLE_OPEN_DIALOG: String = "juggle_open_dialog"
let JUGGLE_OPEN_DIALOG_KEY: String = "juggle_open_dialog_key"
let JUGGLE_SUBMIT_FROM: String = "juggle_submit_form"
let JUGGLE_GET_OPEN_URL: String = "juggle_get_open_url"
let JUGGLE_REFRESH_USER: String = "juggle_refresh_user"
let JUGGLE_DATA: String = "juggle_data"

let JUGGLE_PAGE_TAG: String = "pagetag"
let JUGGLE_DIALOG_CLICK_DATA: String = "dialogClickData"

func onJuggleItemClick(data: JugglePageRowsData){
    if(data.funs?.isEmpty == true){
        return
    }
    let count = data.funs?.count ?? 0
    for i in 0..<count {
        let type = data.funs?[i] ?? ""
        if(type.isEmpty == true){
            continue
        }
        switch type{
        case "open_page":
            gotoNewWebviewController(url: data.link)
            break
        case "to_app_path":
            gotoLocalController(pagePath: data.app_path)
            break
        case "toast":
            DToast.showToastActionBottom(message: data.toast ?? "")
            break
        case "copy":
            UIPasteboard.general.string = data.copy_text ?? ""
            DToast.showToastActionBottom(message: "复制成功")
            break
        case "close_webview":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_CLOSE_WEBVIEW])
            break
        case "reload_webview":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_RELOAD_WEBVIEW])
            break
        case "is_show_to_server":
            
            break
        case "submit_form":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_SUBMIT_FROM])
            break
        case "dialog":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_OPEN_DIALOG,
                                                                                                            JUGGLE_OPEN_DIALOG_KEY : data.dialog_key])
            break
        case "get_open_url":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_GET_OPEN_URL,
                                                                                                                       JUGGLE_DATA : data.server_link])
            break
        case "refresh_user":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_REFRESH_USER])
            break
        default:
            break
        }
    }
}

func onJuggleItemClick(data: JugglePageRows){
    if(data.funs?.isEmpty == true){
        return
    }
    let count = data.funs?.count ?? 0
    for i in 0..<count {
        let type = data.funs?[i] ?? ""
        if(type.isEmpty == true){
            continue
        }
        switch type{
        case "open_page":
            gotoNewWebviewController(url: data.link)
            break
        case "to_app_path":
            gotoLocalController(pagePath: data.app_path)
            break
        case "toast":
            DToast.showToastActionBottom(message: data.toast ?? "")
            break
        case "copy":
            UIPasteboard.general.string = data.copy_text ?? ""
            DToast.showToastActionBottom(message: "复制成功")
            break
        case "close_webview":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_CLOSE_WEBVIEW])
            break
        case "reload_webview":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_RELOAD_WEBVIEW])
            break
        case "is_show_to_server":
            
            break
        case "submit_form":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_SUBMIT_FROM])
            break
        case "dialog":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_OPEN_DIALOG,
                                                                                                            JUGGLE_OPEN_DIALOG_KEY : data.dialog_key])
            break
        case "get_open_url":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_GET_OPEN_URL,
                                                                                                                       JUGGLE_DATA : data.server_link])
            break
        case "refresh_user":
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil, userInfo:  [JUGGLE_PAGE_TAG : data.pagetag,
                                                                                                                  JUGGLE_ITEM_TYPE : JUGGLE_REFRESH_USER])
            break
        default:
            break
        }
    }
}

func isNeedDeleteTime(item: JugglePageRowsData) -> Bool {
    // 先处理时间
    if (item.limit_time != nil && item.limit_time!.count == 2) {
        let currentTime = Date().milliStamp
        let startTime = item.limit_time![0]
        let endTime = item.limit_time![1]
        if(currentTime < startTime || currentTime > endTime){
            return true
        }
    }
    return false
}

func isNeedDeleteLevel(whiteLevel: [String]?, blackLevel: [String]?) -> Bool{
    let userLevel = DimRuntimeData.shared.getUserLevel()
    
    if(userLevel == ""){
        return false
    }
   
    var inWhiteLevel = false
    var hasWhiteLevel = false
    var inBlackLevel = false
    // 白名单
    if(whiteLevel != nil && (whiteLevel?.isEmpty == false)){
        hasWhiteLevel = true
        for i in 0..<(whiteLevel?.count ?? 0) {
            if(whiteLevel![i] == userLevel){
                inWhiteLevel = true
                break
            }
        }
    }
    // 黑名单
    if(blackLevel != nil && (blackLevel?.isEmpty == false)){
        for i in 0..<(blackLevel?.count ?? 0) {
            if(blackLevel![i] == userLevel){
                inBlackLevel = true
                break
            }
        }
    }
    // 白名单 黑名单 判断
    // 如果白名单为空 直接判断黑名单
    // 如果用户在白名单，在判断在不在黑名单
    // 如果 白名单不为空 且 用户不在白名单 不给看
    if (hasWhiteLevel) {
        if (inWhiteLevel) {
            if (inBlackLevel) {
                return true
            }
        } else {
            return true
        }
    } else {
        if (inBlackLevel) {
            return true
        }
    }
    
    return false
}

func isNeedDeleteLevel(whiteLevel: [[String]]?, blackLevel: [[String]]?) -> Bool{
    let appType = PONTUS_APP_KEY
    let userLevel = DimRuntimeData.shared.getUserLevel()
    
    if(userLevel == ""){
        return false
    }
   
    var inWhiteLevel = false
    var hasWhiteLevel = false
    var inBlackLevel = false
    // 白名单
    if(whiteLevel != nil && (whiteLevel?.isEmpty == false)){
        hasWhiteLevel = true
        for i in 0..<(whiteLevel?.count ?? 0) {
            if(whiteLevel![i][0] == appType && whiteLevel![i][1] == userLevel){
                inWhiteLevel = true
                break
            }
        }
    }
    // 黑名单
    if(blackLevel != nil && (blackLevel?.isEmpty == false)){
        for i in 0..<(blackLevel?.count ?? 0) {
            if(blackLevel![i][0] == appType && blackLevel![i][1] == userLevel){
                inBlackLevel = true
                break
            }
        }
    }
    // 白名单 黑名单 判断
    // 如果白名单为空 直接判断黑名单
    // 如果用户在白名单，在判断在不在黑名单
    // 如果 白名单不为空 且 用户不在白名单 不给看
    if (hasWhiteLevel) {
        if (inWhiteLevel) {
            if (inBlackLevel) {
                return true
            }
        } else {
            return true
        }
    } else {
        if (inBlackLevel) {
            return true
        }
    }
    
    return false
}

func isNeedDeleteUsers(whiteUsers: String, blackUsers: String) -> Bool{
    var inWhiteUsers = false
    var hasWhiteUsers = false
    var inBlackUsers = false
    let userMobile = DimRuntimeData.shared.getUserMobile()
    if(userMobile == ""){
        return false
    }
    // white
    if(whiteUsers.isEmpty == false){
        hasWhiteUsers = true
        let whiteArrays = whiteUsers.split(separator: ",")
        for i in 0..<(whiteArrays.count) {
            if(whiteArrays[i] == userMobile){
                inWhiteUsers = true
                break
            }
        }
    }
    // black
    if(blackUsers.isEmpty == false){
        let blackArrays = blackUsers.split(separator: ",")
        for i in 0..<(blackArrays.count) {
            if(blackArrays[i] == userMobile){
                inBlackUsers = true
                break
            }
        }
    }
    // 白名单 黑名单 判断
    // 如果白名单为空 直接判断黑名单
    // 如果用户在白名单，在判断在不在黑名单
    // 如果 白名单不为空 且 用户不在白名单 不给看
    if (hasWhiteUsers) {
        if (inWhiteUsers) {
            if (inBlackUsers) {
                return true
            }
        } else {
            return true
        }
    } else {
        if (inBlackUsers) {
            return true
        }
    }
    return false
}
