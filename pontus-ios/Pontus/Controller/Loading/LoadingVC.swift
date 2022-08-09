//
//  LoadingVC.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import Foundation
import SwiftyFitsize
import UIKit

class LoadingVC: BaseController {
    
    var limitTime: Int = 3+1;
    var hasDataBack = false
    var showUpdate = false
    var storeUrlString = ""
    
    private let onBoarding = OnBoarding()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("加载了 ============== LoadingVC")
    }
    
    var startClosure: ((String) -> ())?
    
    override func setupLayout() {
        let img = UIImageView()
        img.image = UIImage(named: "img_loading")
        img.contentMode = .scaleAspectFill

        view.addSubview(img)

        img.snp.makeConstraints { (make) in
            make.width.equalTo(Screen_Width)
            make.height.equalTo(Screen_Height)
            make.center.equalTo(view)
        }
        initVersionUpdate()
        getUserInfo()
        getVersion()
        startCountDown()
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }

    override func configNavigationBar() {
        super.configNavigationBar()
        navigationController?.navigationBar.isHidden = true
        navigationController?.barStyle(.white)
        navigationItem.title = ""
    }
    
    func startCountDown() {
        performSelector(inBackground: #selector(timerThread), with: nil)
    }
    
    @objc func timerThread() {
        let timeCount = limitTime
        for _ in 0..<timeCount {
            limitTime = limitTime - 1
            self.performSelector(onMainThread: #selector(updateJumpBtn), with: self, waitUntilDone: true)
            sleep(1)
        }
    }
    
    @objc func updateJumpBtn() {
        print("limitTime = " + String(limitTime))
        if (limitTime <= 0) {
            gotoNext()
        }
    }
    
    func gotoNext(){
        print("跳转到下一个页面")
        if (!showUpdate && limitTime <= 0 && hasDataBack){
            OnBoarding.shared.processNextState()
        }
    }
    
    private func getUserInfo(){
        if(AppPreferences.shared.isLogined && DimRuntimeData.shared.userInfo == nil){
            NetWorkRequest(API.userData, modelType: [UserBean].self, successCallback: { (config, responseModel) in
                config.forEach({ (item) in
                    if(item.id == 0){
                        userLogout()
                    } else {
                        DimRuntimeData.shared.userInfo = item
                        AppPreferences.shared.setUserId(userId: String(item.id))
                    }
                })
            }, failureCallback: { (responseModel) in
                print("网络请求失败 包括服务器错误和网络异常\(responseModel.error_code)__\(responseModel.error)")
            })
        }
    }
    
    func getAppConfig(){
        NetWorkRequest(API.appConfig, modelType: [AppConfig].self, successCallback: { (config, responseModel) in
            config.forEach({ (item) in
                DimRuntimeData.shared.appConfig = item
                self.hasDataBack = true
                self.gotoNext()
        })
        }, failureCallback: { (responseModel) in
            print("网络请求失败 包括服务器错误和网络异常\(responseModel.error_code)__\(responseModel.error)")
        })
    }
    
    func getVersion(){
        NetWorkRequest(API.versionCheck, modelType: [VersionModel].self, successCallback: { (config, responseModel) in
            config.forEach({ (bean) in
                if(bean.update){
                    print("需要版本更新啦～～～～～～～～")
                    self.showUpdate = true
                    self.showVersionUpdate(version: bean)
                    self.getAppConfig()
                } else {
                    self.getAppConfig()
                }
            })
        }, failureCallback: { (responseModel) in
            print("getVersion failed")
            self.getAppConfig()
        })
    }
    
    private func showVersionUpdate(version: VersionModel){
        storeUrlString = version.url ?? ""
        versionShow.text = "版本 " + (version.version ?? "")
        versionDes.text = version.comment
        
        versionTop.isHidden = false
        versionView.isHidden = false
        versionShow.isHidden = false
        
        if(version.forceUpdate){
            cancelBtn.isHidden = false
        } else {
            cancelBtn.isHidden = true
        }
    }
    
    private func initVersionUpdate(){
        view.addSubview(versionView)
        versionView.snp.makeConstraints { (make) in
            make.width.equalTo(280~)
            make.height.equalTo(460~)
            make.top.equalToSuperview().offset(200)
            make.centerX.equalToSuperview()
        }
        
        view.addSubview(versionTop)
        versionTop.snp.makeConstraints { (make) in
            make.width.equalTo(316~)
            make.height.equalTo(200~)
            make.top.equalToSuperview().offset(100~)
            make.centerX.equalToSuperview()
        }
        
        view.addSubview(versionShow)
        versionShow.snp.makeConstraints { (make) in
            make.width.equalTo(316~)
            make.height.equalTo(50~)
            make.top.equalTo(versionTop.snp_top).offset(140~)
            make.centerX.equalTo(view)
        }
        
        versionView.addSubview(versionDes)
        versionDes.snp.makeConstraints { (make) in
            make.width.equalTo(250~)
            make.height.equalTo(200~)
            make.top.equalTo(versionTop.snp_bottom)
            make.centerX.equalToSuperview()
        }
        
        versionView.addSubview(cancelBtn)
        cancelBtn.snp.makeConstraints { (make) in
            make.width.equalTo(200~)
            make.height.equalTo(50~)
            make.top.equalTo(versionDes.snp_bottom)
            make.centerX.equalTo(view)
        }
        
        versionView.addSubview(updateBtn)
        updateBtn.snp.makeConstraints { (make) in
            make.width.equalTo(200~)
            make.height.equalTo(50~)
            make.top.equalTo(cancelBtn.snp_bottom).offset(10~)
            make.centerX.equalTo(view)
        }
        
        versionView.isHidden = true
        versionTop.isHidden = true
        versionShow.isHidden = true
        
        let tapGestureUpdate = UITapGestureRecognizer.init(target: self, action: #selector(didTapUpdateView(_:)))
        updateBtn.isUserInteractionEnabled = true
        updateBtn.addGestureRecognizer(tapGestureUpdate)
        
        let tapGestureCancel = UITapGestureRecognizer.init(target: self, action: #selector(didTapCancelView(_:)))
        cancelBtn.isUserInteractionEnabled = true
        cancelBtn.addGestureRecognizer(tapGestureCancel)
    }

    @objc private func didTapUpdateView(_ sender: UITapGestureRecognizer) {
        print("didTapUpdateView 被点击")
        if let url = URL(string: storeUrlString) {
            //根据iOS系统版本，分别处理
            if #available(iOS 10, *) {
                UIApplication.shared.open(url, options: [:],completionHandler: {
                    (success) in
                })
            } else {
                UIApplication.shared.openURL(url)
            }
        }
    }

    @objc private func didTapCancelView(_ sender: UITapGestureRecognizer) {
        print("didTapCancelView 被点击")
        versionView.isHidden = false
        showUpdate = false
        gotoNext()
    }
    
    private lazy var versionView: UIView = {
        let versionView = UIView()
        versionView.backgroundColor = UIColor.init(r: 255, g: 255, b: 255)
        return versionView
    }()
    
    private lazy var cancelBtn: UIButton = {
        let cancelBtn = UIButton()
        cancelBtn.setTitle("稍后", for: UIControl.State.normal)
        cancelBtn.backgroundColor = UIColor.init(r: 68, g: 142, b: 247, a: 0.3)
        cancelBtn.layer.cornerRadius = 10~
        cancelBtn.titleLabel?.font = UIFont.systemFont(ofSize: 16)
        cancelBtn.titleLabel?.textColor = UIColor.white
        return cancelBtn
    }()
    
    private lazy var updateBtn: UIButton = {
        let updateBtn = UIButton()
        updateBtn.setTitle("立即升级", for: UIControl.State.normal)
        updateBtn.backgroundColor = UIColor.init(r: 68, g: 142, b: 247)
        updateBtn.layer.cornerRadius = 10~
        updateBtn.titleLabel?.font = UIFont.systemFont(ofSize: 16)
        updateBtn.titleLabel?.textColor = UIColor.white
        return updateBtn
    }()
    
    private lazy var versionDes: UILabel = {
        let versionDes = UILabel()
        versionDes.textAlignment = NSTextAlignment.left
        versionDes.textColor = UIColor.black
        versionDes.font = UIFont.systemFont(ofSize: 20~)
        return versionDes
    }()
    
    private lazy var versionShow: UILabel = {
        let versionShow = UILabel()
        versionShow.textAlignment = NSTextAlignment.center
        versionShow.textColor = UIColor.white
        versionShow.font = UIFont.systemFont(ofSize: 20~)
        return versionShow
    }()
    
    private lazy var versionTop: UIImageView = {
        let versionTop = UIImageView()
        versionTop.image = UIImage(named: "loading_veirsion_top")
        return versionTop
    }()
}
