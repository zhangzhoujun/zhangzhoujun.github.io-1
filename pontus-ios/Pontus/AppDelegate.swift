//
//  AppDelegate.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import UIKit
import IQKeyboardManagerSwift
import Alamofire


@main
class AppDelegate: UIResponder, UIApplicationDelegate {
    
    var window: UIWindow?
    
    // 监测网络
    lazy var reachability: NetworkReachabilityManager? = {
        return NetworkReachabilityManager(host: "http://app.u17.com")
    }()

    // 申明手机屏幕旋转方向
    var orientation: UIInterfaceOrientationMask = .portrait
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
//        UMCommonLogSwift.setUpUMCommonLogManager()
//        UMCommonSwift.setLogEnabled(bFlag: true)
//        UMCommonSwift.initWithAppkey(appKey: PONTUS_UM_APP_KEY, channel: "App Store")
        
        window = UIWindow(frame: UIScreen.main.bounds);
        window?.backgroundColor = UIColor.white
        window?.makeKeyAndVisible()
        
        print("启动第一个页面")
        OnBoarding.shared.start(with: AppRouter(window: window!))
        // 配置
        setupBaseConfig()
    
        return true
    }
    
    deinit {
        print("loading 页面销毁，移除观察者")
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(JUGGLE_LOGIN_SCUCCESS), object: nil)
    }
    
    func start(){
        let tabVC = TabBarController()
        self.window?.rootViewController = tabVC
    }
    
    func setupBaseConfig() {
        // 1.键盘处理
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.shouldResignOnTouchOutside = true

        // 2.网络监控
        reachability?.listener = { status in
            switch status {
            case .reachable(.wwan):
                UNoticeBar(config: UNoticeBarConfig(title: "主人,检测到您正在使用移动数据")).show(duration: 2)
            default: break
            }
        }
        reachability?.startListening()
        
        DimRuntimeData.shared.setUserToken(token: AppPreferences.shared.getUserToken())
    }

    // 3.支持屏幕旋转
    func application(_ application: UIApplication, supportedInterfaceOrientationsFor window: UIWindow?) -> UIInterfaceOrientationMask {
        return orientation
    }
}

extension UIApplication {
    // 4. 强制旋转屏幕
    class func changeOrientationTo(landscapeRight: Bool) {
        guard let delegate = UIApplication.shared.delegate as? AppDelegate else { return }
        if landscapeRight == true {
            delegate.orientation = .landscapeRight
            UIApplication.shared.supportedInterfaceOrientations(for: delegate.window)
            UIDevice.current.setValue(UIInterfaceOrientation.landscapeRight.rawValue, forKey: "orientation")
        } else {
            delegate.orientation = .portrait
            UIApplication.shared.supportedInterfaceOrientations(for: delegate.window)
            UIDevice.current.setValue(UIInterfaceOrientation.portrait.rawValue, forKey: "orientation")
        }
    }
}
