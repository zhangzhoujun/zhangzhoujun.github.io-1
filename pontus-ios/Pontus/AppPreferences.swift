//
//  AppPreferences.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import UIKit

final class AppPreferences {
    static let shared = AppPreferences()
    
    enum Keys: String {
        case firstLanuch = "IsFirstLanuch"
        case token = "UserToken"
        case userId = "UserID"
        case dialog = "Dialog"
    }
    
    fileprivate let PreferencesKey = "AppPreferences"
    fileprivate var preferences = [String: Any]()
    
    init() {
        loadPrefernces()
        observeApplicationState()
    }
   
    /// 用户是否第一次运行App
    var isFirstLaunch: Bool {
        if(PONTUS_SHOW_GUIDE == 0){
            return false
        }
        let uDefault = UserDefaults.standard;
        if uDefault.bool(forKey: Keys.firstLanuch.rawValue) {
            return true
        } else {
            return false
        }
    }
    
    func setShowLaunched() {
        let uDefault = UserDefaults.standard;
        uDefault.set(false, forKey: Keys.firstLanuch.rawValue)
        uDefault.synchronize();
    }
    
    var isLogined: Bool{
        let token = getUserToken()
        if(token.isEmpty){
            return false
        }
        return true
    }
    
    var isNeedLogin: Bool{
        return DimRuntimeData.shared.appConfig?.mustLogin == true && !AppPreferences.shared.isLogined
    }
    
    func getUserToken() -> String{
        let uDefault = UserDefaults.standard
        let token = uDefault.string(forKey: Keys.token.rawValue)
        return token ?? ""
    }
    
    func setUserToken(token: String) {
        let uDefault = UserDefaults.standard
        uDefault.set(token, forKey: Keys.token.rawValue)
        uDefault.synchronize();
    }
    
    func getUserId() -> String{
        let uDefault = UserDefaults.standard
        let token = uDefault.string(forKey: Keys.userId.rawValue)
        return token ?? ""
    }
    
    func setUserId(userId: String) {
        let uDefault = UserDefaults.standard
        uDefault.set(userId, forKey: Keys.userId.rawValue)
        uDefault.synchronize();
    }
    
    func getDialogTime(dialogId: String) -> Int{
        let uDefault = UserDefaults.standard
        let time = uDefault.integer(forKey: Keys.dialog.rawValue + dialogId + getUserId())
        return time
    }
    
    func setDialogTime(dialogId: String, time: Int) {
        let uDefault = UserDefaults.standard
        uDefault.set(time, forKey: Keys.dialog.rawValue + dialogId + getUserId())
        uDefault.synchronize();
    }
    
    func setUserLogout(){
        setUserId(userId: "")
        setUserToken(token: "")
    }
}

fileprivate extension AppPreferences {
    
    func loadPrefernces() {
        if let infoDic = UserDefaults.standard.dictionary(forKey: PreferencesKey) {
            preferences = infoDic
        } else {
            resetDefaultPreference()
        }
    }
    
    func resetDefaultPreference() {
        print("resetDefaultPreference")
        DToast.showToastAction(message: "resetDefaultPreference")
        
        let uDefault = UserDefaults.standard;
        uDefault.set(true, forKey: Keys.firstLanuch.rawValue);
        uDefault.set("", forKey: Keys.token.rawValue);
        uDefault.synchronize();
    }
    
    func observeApplicationState() {
        NotificationCenter.default.addObserver(self, selector: #selector(syncToUserDefaults), name: UIApplication.willResignActiveNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(syncToUserDefaults), name: UIApplication.willTerminateNotification, object: nil)
    }
    
    @objc func syncToUserDefaults() {
        UserDefaults.standard.set(preferences, forKey: PreferencesKey)
    }
}
