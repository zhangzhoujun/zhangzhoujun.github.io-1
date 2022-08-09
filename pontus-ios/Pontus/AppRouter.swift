//
//  AppRouter.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import Foundation
import UIKit

final class AppRouter {
    
    fileprivate var window: UIWindow
    
    init(window: UIWindow) {
        self.window = window
    }
    
    lazy var guideVC: GuideVC = {
        let guideVC = GuideVC()
        return guideVC
    }()
    
    lazy var rootTabVC: TabBarController = {
        let tabVC = TabBarController()
        return tabVC
    }()
    
    lazy var loadingVC: LoadingVC = {
        let loadingVC = LoadingVC()
        return loadingVC
    }()
    
    func setWindowRootViewController(_ viewController: UIViewController) {
        if window.rootViewController == nil{
            window.rootViewController = viewController
            return
        }
        window.rootViewController = viewController
    }

}

