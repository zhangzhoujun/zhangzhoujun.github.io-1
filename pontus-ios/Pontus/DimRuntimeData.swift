//
//  DimRuntimeData.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import Foundation

public class DimRuntimeData{
    //单例
    public static let shared = DimRuntimeData()

    var appConfig: AppConfig? = nil
    
    var userInfo: UserBean? = nil
    
    private var userToken: String = ""
    
    private var appVersion: String = ""
    
    func getUserToken() -> String{
        if(userToken.isEmpty){
            userToken = AppPreferences.shared.getUserToken()
        }
        return userToken
    }
    
    func setUserToken(token: String){
        userToken = token
    }
    
    func getUserLevel() -> String{
        if(userInfo == nil){
            return ""
        }
        let level = userInfo?.level ?? 0
        return String(level)
    }
    
    func getUserMobile() -> String{
        if(userInfo == nil){
            return ""
        }
        return userInfo?.mobile ?? "" 
    }
    
    func getAppVersion() -> String{
        if(appVersion == ""){
            let infoDictionary : [String : Any] = Bundle.main.infoDictionary!
            //let appName : Any = infoDictionary["CFBundleDisplayName"] // 程序名称
            let mainVersion : Any = infoDictionary["CFBundleShortVersionString"] as Any // 主程序版本号
            //let minorVersion : Any = infoDictionary["CFBundleVersion"] as Any // 版本号(内部标示)
            appVersion = mainVersion as! String
        }
        return appVersion
    }
}

