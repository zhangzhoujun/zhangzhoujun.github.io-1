//
//  GuideVC.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/12/10.
//

import Foundation
import UIKit

class GuideVC: BaseController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("加载了 ============== GuideVC")
        
        // 首页背景图
//        let imageView = UIImageView.init(image: UIImage.init(named: "view_bg_image.png"))
//        imageView.frame = self.view.bounds
//        self.view.addSubview(imageView)
        
        // 引导页案例
        let imageGifArray = ["guide_0.png","guide_1.png","guide_2.png","guide_3.png"]
        let guideView = GuidePageView.init(images: imageGifArray, isHiddenSkipBtn: true, isHiddenStartBtn: false, loginRegistCompletion: {
            self.gotoNext()
        }) {
            self.gotoNext()
        }
        self.view.addSubview(guideView)
    }
    
    override func setupLayout() {
        
    }
    
    private func gotoNext() {
        print("gotoNext")
        AppPreferences.shared.setShowLaunched()
        OnBoarding.shared.processNextState()
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }

    override func configNavigationBar() {
        super.configNavigationBar()
        navigationController?.navigationBar.isHidden = true
    }
    
}
