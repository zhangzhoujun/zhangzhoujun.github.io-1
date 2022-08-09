//
//  SetVC.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/12/7.
//

import Foundation
import SwiftyFitsize
import UIKit

class SetVC: BaseController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("加载了 ============== SetVC")
    }
    
    private lazy var mobileNameView: UILabel = {
        let mobileNameView = UILabel()
        mobileNameView.backgroundColor = .white
        mobileNameView.textColor = UIColor.black
        mobileNameView.font = UIFont.systemFont(ofSize: 15~)
        mobileNameView.isUserInteractionEnabled = false
        mobileNameView.text = "  手机号"
        return mobileNameView
    }()
    
    private lazy var mobileValueView: UILabel = {
        let mobileValueView = UILabel()
        mobileValueView.backgroundColor = .white
        mobileValueView.textColor = UIColor.black
        mobileValueView.font = UIFont.systemFont(ofSize: 15~)
        mobileValueView.isUserInteractionEnabled = false
        return mobileValueView
    }()
    
    private lazy var aboutNameView: UILabel = {
        let aboutNameView = UILabel()
        aboutNameView.backgroundColor = .white
        aboutNameView.textColor = UIColor.black
        aboutNameView.font = UIFont.systemFont(ofSize: 15~)
        aboutNameView.isUserInteractionEnabled = false
        aboutNameView.text = "  关于"
        return aboutNameView
    }()
    
    private lazy var fbNameView: UILabel = {
        let fbNameView = UILabel()
        fbNameView.backgroundColor = .white
        fbNameView.textColor = UIColor.black
        fbNameView.font = UIFont.systemFont(ofSize: 15~)
        fbNameView.isUserInteractionEnabled = false
        fbNameView.text = "  意见反馈"
        return fbNameView
    }()
    
    private lazy var fbMoreView: UIImageView = {
        let fbMoreView = UIImageView()
        fbMoreView.contentMode = .scaleAspectFill
        fbMoreView.clipsToBounds = true
        fbMoreView.image = UIImage(named: "about_more")
        return fbMoreView
    }()
    
    private lazy var aboutMoreView: UIImageView = {
        let aboutMoreView = UIImageView()
        aboutMoreView.contentMode = .scaleAspectFill
        aboutMoreView.clipsToBounds = true
        aboutMoreView.image = UIImage(named: "about_more")
        return aboutMoreView
    }()
    
    private lazy var logouBtn: UIButton = {
        let logouBtn = UIButton()
        logouBtn.setTitle("退出登录", for: UIControl.State.normal)
        logouBtn.backgroundColor = UIColor.init(r: 68, g: 142, b: 247)
        logouBtn.layer.cornerRadius = 20~
        logouBtn.titleLabel?.font = UIFont.systemFont(ofSize: 16)
        logouBtn.titleLabel?.textColor = UIColor.white
        return logouBtn
    }()
    
    private lazy var versionView: UILabel = {
        let versionView = UILabel()
        versionView.backgroundColor = .clear
        versionView.textColor = UIColor.init(r: 102, g: 102, b: 102)
        versionView.font = UIFont.systemFont(ofSize: 15~)
        versionView.isUserInteractionEnabled = false
        versionView.textAlignment = NSTextAlignment.center
        return versionView
    }()
    
    override func setupLayout() {
        view.addSubview(mobileNameView)

        mobileNameView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalToSuperview().offset(10~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(aboutNameView)
        aboutNameView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalTo(mobileNameView.snp_bottom).offset(1~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(fbNameView)
        fbNameView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalTo(aboutNameView.snp_bottom).offset(1~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(aboutMoreView)
        aboutMoreView.snp.makeConstraints { (make) in
            make.right.equalToSuperview().offset(-10~)
            make.top.equalTo(aboutNameView.snp_top).offset(17~)
            make.height.equalTo(16~)
            make.width.equalTo(11~)
        }
        
        view.addSubview(fbMoreView)
        fbMoreView.snp.makeConstraints { (make) in
            make.right.equalToSuperview().offset(-10~)
            make.top.equalTo(fbNameView.snp_top).offset(17~)
            make.height.equalTo(16~)
            make.width.equalTo(11~)
        }
        
        view.addSubview(mobileValueView)
        mobileValueView.snp.makeConstraints { (make) in
            make.right.equalToSuperview().offset(-10~)
            make.top.equalToSuperview().offset(10~)
            make.height.equalTo(100~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(logouBtn)
        logouBtn.snp.makeConstraints { (make) in
            make.bottom.equalToSuperview().offset(-40~)
            make.width.equalTo(290~)
            make.height.equalTo(50~)
            make.centerX.equalToSuperview()
        }
        
        view.addSubview(versionView)
        versionView.snp.makeConstraints { (make) in
            make.bottom.equalTo(logouBtn.snp_top).offset(10~)
            make.left.right.equalToSuperview()
            make.height.equalTo(50~)
        }
        
        let tapGestureAbout = UITapGestureRecognizer.init(target: self, action: #selector(didAboutTapView(_:)))
        aboutNameView.isUserInteractionEnabled = true
        aboutNameView.addGestureRecognizer(tapGestureAbout)
        
        let tapGestureFb = UITapGestureRecognizer.init(target: self, action: #selector(didFbTapView(_:)))
        fbNameView.isUserInteractionEnabled = true
        fbNameView.addGestureRecognizer(tapGestureFb)
        
        let tapGestureLogout = UITapGestureRecognizer.init(target: self, action: #selector(didLogoutTapView(_:)))
        logouBtn.isUserInteractionEnabled = true
        logouBtn.addGestureRecognizer(tapGestureLogout)
        
        mobileValueView.text = DimRuntimeData.shared.getUserMobile()
        
        let infoDictionary : [String : Any] = Bundle.main.infoDictionary!
        let mainVersion : Any = infoDictionary["CFBundleShortVersionString"] as Any // 主程序版本号
        
        aboutNameView.text = "  关于" + PONTUS_APP_NAME
        versionView.text = "版本号：v" + (mainVersion as! String)
    }
    
    @objc private func didAboutTapView(_ sender: UITapGestureRecognizer) {
        print("didAboutTapView 被点击")
        let vc = AboutVC()
        topVC?.navigationController?.pushViewController(vc , animated: true)
    }
    
    @objc private func didFbTapView(_ sender: UITapGestureRecognizer) {
        print("didFbTapView 被点击")
        gotoNewWebviewController(url: DimRuntimeData.shared.appConfig?.base_data?.feedback_url)
    }
    
    @objc private func didLogoutTapView(_ sender: UITapGestureRecognizer) {
        print("didLogoutTapView 被点击")
        
        AppPreferences.shared.setUserLogout()
        gotoLoginController(fromLoading: false)
        dismiss(animated: false)
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }

    override func configNavigationBar() {
        super.configNavigationBar()
        navigationController?.navigationBar.isHidden = false
        navigationController?.barStyle(.white)
        navigationItem.title = "设置"
    }
    
}
