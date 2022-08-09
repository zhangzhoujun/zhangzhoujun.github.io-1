//
//  TabBarController.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/24.
//

import UIKit

class TabBarController: UITabBarController {

    override func viewDidLoad() {
        
        super.viewDidLoad()

        tabBar.isTranslucent = false

        setupLayout()
        
        selectedIndex = 0
    }
    
    func setupLayout() {
        print("TabBarController init")
        let config = DimRuntimeData.shared.appConfig
        if(config == nil){
            return
        }
        
        let sizeLevel = config?.tabs?.count ?? 0
        for i in stride(from:(sizeLevel - 1),through:0,by: -1) {
            let tab = config?.tabs?[i]
            // 黑白名单过滤 -> 身份级别
            if(isNeedDeleteLevel(whiteLevel: tab?.whiteLevel, blackLevel: tab?.blackLevel) == true){
                config?.tabs?.remove(at: i)
            }
        }
        let sizeUser = config?.tabs?.count ?? 0
        for i in stride(from:(sizeUser - 1),through:0,by: -1) {
            let tab = config?.tabs?[i]
            // 黑白名单过滤 -> 单个用户级别
            if(isNeedDeleteUsers(whiteUsers: tab?.whiteUsers ?? "", blackUsers: tab?.blackUsers ?? "") == true){
                config?.tabs?.remove(at: i)
            }
        }
        
        let count = config?.tabs?.count ?? 0
        let iconWid = CGFloat(50)
        for i in 0..<count {
            print("TabBarController.add = \(i)")
            
            let tab: AppConfigTabs = (config?.tabs?[i])!
            do{
                let vc = getNewUIViewController(pagePath: tab.pagePath, showHead: false)
               
                let iconUrl = URL(string: tab.icon!)
                let iconData = try Data(contentsOf: iconUrl!)
                var iconImg = UIImage(data: iconData)
                
                iconImg = UIImage.scaleTo(image: iconImg!, w: CGFloat(iconWid), h: CGFloat(50))
                
                let activationIconUrl = URL(string: tab.activationIcon!)
                let activationIconData = try Data(contentsOf: activationIconUrl!)
                var activationIconImg = UIImage(data: activationIconData)
                
                activationIconImg = UIImage.scaleTo(image: activationIconImg!, w: CGFloat(iconWid), h: CGFloat(50))
                if(vc != nil){
                    addChildController(vc!,
                                       title: "",
                                       image: iconImg,
                                       selectedImage: activationIconImg)
                }
            }catch let error as NSError{
                print(error)
            }
        }
    }
    
    func addChildController(_ childController: UIViewController, title:String?, image:UIImage? ,selectedImage:UIImage?) {
        
        childController.title = title
        childController.tabBarItem = UITabBarItem(title: nil,
                                                  image: image?.withRenderingMode(.alwaysOriginal),
                                                  selectedImage: selectedImage?.withRenderingMode(.alwaysOriginal))
        
        if UIDevice.current.userInterfaceIdiom == .phone {
            childController.tabBarItem.imageInsets = UIEdgeInsets(top: 6, left: 0, bottom: -6, right: 0)
        }
        addChild(NaviController(rootViewController: childController))
    }
}

extension TabBarController {

    override var preferredStatusBarStyle: UIStatusBarStyle {
        guard let select = selectedViewController else { return .lightContent }
        return select.preferredStatusBarStyle
    }
}
