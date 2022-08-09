//
//  OnBoarding.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import Foundation

/// 这个类用于处理处理首次进入App的逻辑处理, 减少AppDelegate的臃肿.
/// 比如根据是否登陆进入不同的界面、注册系统服务、通用服务等。
public class OnBoarding {
    
    public static let shared = OnBoarding()
    
    enum State {
        case showGuide
        case showLogin
        case showHome
    }
    
    fileprivate var appRooter: AppRouter?
    fileprivate var currentState: State = .showHome
    
    /// 调用引接口即可进入处理登陆及界面展示等流程
    func start(with router: AppRouter) {
        appRooter = router
        // processNextState()
        let vc = appRooter!.loadingVC
        appRooter!.setWindowRootViewController(vc)
        startAppServices()
    }
    
    /// 处理 app 的下一个状态
    func processNextState() {
        
        calculateCurrentState()
        
        switch currentState {
        case .showGuide:
            showGuidePage()
        case .showLogin:
            showLoginPage()
        case .showHome:
            showHomePage()
        }
    }
    
    /// 计算当前 app 启动流程状态
    func calculateCurrentState() {
        let preferences = AppPreferences.shared

        if preferences.isFirstLaunch {
            currentState = .showGuide
            print("currentState = showGuide")
        } else if preferences.isNeedLogin {
            currentState = .showLogin
            print("currentState = showLogin")
        } else {
            currentState = .showHome
            print("currentState = showHome")
        }
    }
}

fileprivate extension OnBoarding {
    
    private func showGuidePage() {
        print("当前需要展示的页面 = showGuidePage")
        let vc = appRooter!.guideVC
        appRooter!.setWindowRootViewController(vc)
    }
    
    private func showLoginPage() {
        print("当前需要展示的页面 = showLoginPage")
        let vc = getNewUIViewController(pagePath: DimRuntimeData.shared.appConfig?.base_data?.login_path ,fromLoading: true)
        if(vc != nil){
            appRooter!.setWindowRootViewController(vc!)
        }
    }
    
    private func showHomePage() {
        print("当前需要展示的页面 = showHomePage")
        let tabVC = appRooter!.rootTabVC
        appRooter!.setWindowRootViewController(tabVC)
    }
}

//MARK: - App相应服务模块的启动
fileprivate extension OnBoarding {
    
    /// 启动App所需要的基础服务
    func startAppServices() {
        //监听网络状态
        startNetworkMonitor()
    }

    /// 启动网络监控服务
    private func startNetworkMonitor() {
        
    }

}
