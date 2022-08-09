//
//  AboutVC.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/12/7.
//

import Foundation
import SwiftyFitsize
import UIKit

class AboutVC: BaseController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("加载了 ============== AboutVC")
    }
    
    private lazy var logoView: UIImageView = {
        let logoView = UIImageView()
        logoView.contentMode = .scaleAspectFill
        logoView.clipsToBounds = true
        logoView.image = UIImage(named: "AppIcon")
        return logoView
    }()
    
    private lazy var xieyiNameView: UILabel = {
        let xieyiNameView = UILabel()
        xieyiNameView.backgroundColor = .white
        xieyiNameView.textColor = UIColor.black
        xieyiNameView.font = UIFont.systemFont(ofSize: 15~)
        xieyiNameView.isUserInteractionEnabled = false
        xieyiNameView.text = "  用户协议"
        return xieyiNameView
    }()
    
    private lazy var ysNameView: UILabel = {
        let ysNameView = UILabel()
        ysNameView.backgroundColor = .white
        ysNameView.textColor = UIColor.black
        ysNameView.font = UIFont.systemFont(ofSize: 15~)
        ysNameView.isUserInteractionEnabled = false
        ysNameView.text = "  隐私政策"
        return ysNameView
    }()
    
    private lazy var xieyiMoreView: UIImageView = {
        let xieyiMoreView = UIImageView()
        xieyiMoreView.contentMode = .scaleAspectFill
        xieyiMoreView.clipsToBounds = true
        xieyiMoreView.image = UIImage(named: "about_more")
        return xieyiMoreView
    }()
    
    private lazy var ysMoreView: UIImageView = {
        let ysMoreView = UIImageView()
        ysMoreView.contentMode = .scaleAspectFill
        ysMoreView.clipsToBounds = true
        ysMoreView.image = UIImage(named: "about_more")
        return ysMoreView
    }()
    
    private lazy var versionView: UILabel = {
        let versionView = UILabel()
        versionView.backgroundColor = .clear
        versionView.textColor = UIColor.init(r: 51, g: 51, b: 51)
        versionView.font = UIFont.systemFont(ofSize: 15~)
        versionView.isUserInteractionEnabled = false
        versionView.textAlignment = NSTextAlignment.center
        return versionView
    }()
    
    override func setupLayout() {
        view.addSubview(logoView)

        logoView.snp.makeConstraints { (make) in
            make.top.equalToSuperview().offset(45~)
            make.centerX.equalToSuperview()
            make.width.height.equalTo(90~)
        }
        
        view.addSubview(versionView)
        versionView.snp.makeConstraints { (make) in
            make.bottom.equalTo(logoView.snp_top).offset(10~)
            make.left.right.equalToSuperview()
            make.height.equalTo(22~)
        }
        
        view.addSubview(xieyiNameView)
        xieyiNameView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalTo(versionView.snp_bottom).offset(30~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(xieyiMoreView)
        xieyiMoreView.snp.makeConstraints { (make) in
            make.right.equalToSuperview().offset(-10~)
            make.top.equalTo(xieyiNameView.snp_top).offset(17~)
            make.height.equalTo(16~)
            make.width.equalTo(11~)
        }
        
        view.addSubview(ysNameView)
        ysNameView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalTo(xieyiNameView.snp_bottom).offset(1~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(ysMoreView)
        ysMoreView.snp.makeConstraints { (make) in
            make.right.equalToSuperview().offset(-10~)
            make.top.equalTo(ysNameView.snp_top).offset(17~)
            make.height.equalTo(16~)
            make.width.equalTo(11~)
        }
        
        let tapGestureAbout = UITapGestureRecognizer.init(target: self, action: #selector(didXieyiTapView(_:)))
        xieyiNameView.isUserInteractionEnabled = true
        xieyiNameView.addGestureRecognizer(tapGestureAbout)
        
        let tapGestureFb = UITapGestureRecognizer.init(target: self, action: #selector(didYSTapView(_:)))
        ysNameView.isUserInteractionEnabled = true
        ysNameView.addGestureRecognizer(tapGestureFb)
        
        let infoDictionary : [String : Any] = Bundle.main.infoDictionary!
        let mainVersion : Any = infoDictionary["CFBundleShortVersionString"] as Any // 主程序版本号
        
        versionView.text = PONTUS_APP_NAME + " V" + (mainVersion as! String)
    }
    
    @objc private func didXieyiTapView(_ sender: UITapGestureRecognizer) {
        print("didXieyiTapView 被点击")
        gotoLocalController(pagePath: DimRuntimeData.shared.appConfig?.userAgreement)
    }
    
    @objc private func didYSTapView(_ sender: UITapGestureRecognizer) {
        print("didYSTapView 被点击")
        gotoLocalController(pagePath: DimRuntimeData.shared.appConfig?.privacyPolicy)
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }

    override func configNavigationBar() {
        super.configNavigationBar()
        navigationController?.navigationBar.isHidden = false
        navigationController?.barStyle(.white)
        navigationItem.title = "关于" + PONTUS_APP_NAME
    }
    
}
