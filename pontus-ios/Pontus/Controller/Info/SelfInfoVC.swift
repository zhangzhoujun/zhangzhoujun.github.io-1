//
//  SelfInfoVC.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/12/7.
//

import Foundation
import SwiftyFitsize
import UIKit
import Alamofire
import SwiftyXMLParser

class SelfInfoVC: BaseController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    private var isShowModift = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        print("加载了 ============== SelfInfoVC")
    }
    
    private lazy var headView: UIImageView = {
        let headView = UIImageView()
        headView.contentMode = .scaleAspectFill
        headView.clipsToBounds = true
        return headView
    }()
    
    private lazy var headNameView: UILabel = {
        let headNameView = UILabel()
        headNameView.backgroundColor = .white
        headNameView.textColor = UIColor.black
        headNameView.font = UIFont.systemFont(ofSize: 15~)
        headNameView.isUserInteractionEnabled = false
        headNameView.text = "  头像"
        return headNameView
    }()
    
    private lazy var nameNameView: UILabel = {
        let nameNameView = UILabel()
        nameNameView.backgroundColor = .white
        nameNameView.textColor = UIColor.black
        nameNameView.font = UIFont.systemFont(ofSize: 15~)
        nameNameView.isUserInteractionEnabled = false
        nameNameView.text = "  昵称"
        return nameNameView
    }()
    
    private lazy var headMoreView: UIImageView = {
        let headMoreView = UIImageView()
        headMoreView.contentMode = .scaleAspectFill
        headMoreView.clipsToBounds = true
        headMoreView.image = UIImage(named: "about_more")
        return headMoreView
    }()
    
    private lazy var nameMoreView: UIImageView = {
        let nameMoreView = UIImageView()
        nameMoreView.contentMode = .scaleAspectFill
        nameMoreView.clipsToBounds = true
        nameMoreView.image = UIImage(named: "about_more")
        return nameMoreView
    }()
    
    private lazy var nameValueView: UILabel = {
        let nameValueView = UILabel()
        nameValueView.backgroundColor = .clear
        nameValueView.textColor = UIColor.init(r: 102, g: 102, b: 102)
        nameValueView.font = UIFont.systemFont(ofSize: 15~)
        nameValueView.isUserInteractionEnabled = false
        nameValueView.textAlignment = NSTextAlignment.center
        return nameValueView
    }()
    
    private lazy var nameModifyView: UIView = {
        let nameModifyView = UIView()
        nameModifyView.backgroundColor = .background
        return nameModifyView
    }()
    
    private lazy var nameTFView: UITextField = {
        let nameTFView = UITextField()
//        nameTFView.layer.cornerRadius = 25~
        nameTFView.backgroundColor = UIColor.white
        nameTFView.borderStyle = UITextField.BorderStyle.bezel
        nameTFView.autocorrectionType = UITextAutocorrectionType.no
        nameTFView.returnKeyType = UIReturnKeyType.done
        nameTFView.clearButtonMode = UITextField.ViewMode.whileEditing
        nameTFView.keyboardType = UIKeyboardType.emailAddress
        nameTFView.keyboardAppearance = UIKeyboardAppearance.alert
        nameTFView.isSecureTextEntry = false
        
        return nameTFView
    }()
    
    private lazy var nameModifyBtn: UIButton = {
        let nameModifyBtn = UIButton()
        nameModifyBtn.setTitle("确定", for: UIControl.State.normal)
        nameModifyBtn.backgroundColor = UIColor.init(r: 68, g: 142, b: 247)
        nameModifyBtn.layer.cornerRadius = 20~
        nameModifyBtn.titleLabel?.font = UIFont.systemFont(ofSize: 16)
        nameModifyBtn.titleLabel?.textColor = UIColor.white
        return nameModifyBtn
    }()
    
    override func setupLayout() {
        
        view.addSubview(headNameView)
        headNameView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalToSuperview().offset(10~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(headMoreView)
        headMoreView.snp.makeConstraints { (make) in
            make.right.equalToSuperview().offset(-10~)
            make.top.equalTo(headNameView.snp_top).offset(17~)
            make.height.equalTo(16~)
            make.width.equalTo(11~)
        }
        
        view.addSubview(headView)
        headView.snp.makeConstraints { (make) in
            make.right.equalTo(headMoreView.snp_left).offset(-10~)
            make.top.equalTo(headNameView.snp_top).offset(8~)
            make.width.height.equalTo(34~)
        }
        
        view.addSubview(nameNameView)
        nameNameView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalTo(headNameView.snp_bottom).offset(1~)
            make.height.equalTo(50~)
        }
        
        view.addSubview(nameMoreView)
        nameMoreView.snp.makeConstraints { (make) in
            make.right.equalToSuperview().offset(-10~)
            make.top.equalTo(nameNameView.snp_top).offset(17~)
            make.height.equalTo(16~)
            make.width.equalTo(11~)
        }
        
        view.addSubview(nameValueView)
        nameValueView.snp.makeConstraints { (make) in
            make.right.equalTo(nameMoreView.snp_left).offset(-10~)
            make.top.equalTo(nameNameView.snp_top)
            make.height.equalTo(50~)
            make.width.equalTo(100~)
        }
        
        let tapGestureAbout = UITapGestureRecognizer.init(target: self, action: #selector(getImage(_:)))
        headNameView.isUserInteractionEnabled = true
        headNameView.addGestureRecognizer(tapGestureAbout)
        
        let tapGestureFb = UITapGestureRecognizer.init(target: self, action: #selector(didNameTapView(_:)))
        nameNameView.isUserInteractionEnabled = true
        nameNameView.addGestureRecognizer(tapGestureFb)
        
        nameValueView.text = DimRuntimeData.shared.userInfo?.nick_name
        headView.kf.setImage(urlString: DimRuntimeData.shared.userInfo?.avatar)
        
        // modify view
        view.addSubview(nameModifyView)
        nameModifyView.snp.makeConstraints { (make) in
            make.left.right.equalToSuperview()
            make.top.equalToSuperview().offset(10~)
            make.height.equalTo(200~)
        }
        
        nameModifyView.addSubview(nameTFView)
        nameTFView.snp.makeConstraints { (make) in
            make.left.equalToSuperview().offset(10~)
            make.right.equalToSuperview().offset(-10~)
            make.top.equalToSuperview().offset(20~)
            make.height.equalTo(50~)
        }
        
        nameModifyView.addSubview(nameModifyBtn)
        nameModifyBtn.snp.makeConstraints { (make) in
            make.top.equalTo(nameTFView.snp_bottom).offset(40~)
            make.width.equalTo(290~)
            make.height.equalTo(50~)
            make.centerX.equalToSuperview()
        }
        
        nameModifyView.isHidden = true
        
        let tapGestureModify = UITapGestureRecognizer.init(target: self, action: #selector(didModifyTapView(_:)))
        nameModifyBtn.isUserInteractionEnabled = true
        nameModifyBtn.addGestureRecognizer(tapGestureModify)
    }
    
    @objc private func didModifyTapView(_ sender: UITapGestureRecognizer) {
        print("didModifyTapView 被点击")
        let modifyName = (nameTFView.text as String?) ?? ""
        if(modifyName == ""){
            DToast.showToastAction(message: "昵称不能为空")
            return
        }
        if(modifyName == DimRuntimeData.shared.userInfo?.nick_name){
            DToast.showToastAction(message: "昵称与之前一致")
            return
        }
        sendModifyToServer(nick_name: modifyName, avatar: "")
    }
    
    @objc private func didHeadTapView(_ sender: UITapGestureRecognizer) {
        print("didHeadTapView 被点击")
        
    }
    
    @objc private func didNameTapView(_ sender: UITapGestureRecognizer) {
        print("didYSTapView 被点击")
        isShowModift = true
        nameTFView.text = DimRuntimeData.shared.userInfo?.nick_name
        nameModifyView.isHidden = false
        navigationItem.title = "修改昵称"
    }

    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .default
    }

    override func configNavigationBar() {
        super.configNavigationBar()
        navigationController?.navigationBar.isHidden = false
        navigationController?.barStyle(.white)
        navigationItem.title = "个人信息"
    }
    
    override func pressBack() {
        if(isShowModift){
            showSelfInfo()
        } else {
            super.pressBack()
        }
    }
    
    private func showSelfInfo(){
        isShowModift = false
        nameModifyView.isHidden = true
        navigationItem.title = "个人信息"
    }
    
    // photo
    var takingPicture:UIImagePickerController!
    
    //点击按钮弹出拍照、相册的选择框
    @IBAction func getImage(_ sender: Any) {
        print("didHeadTapView 被点击")
        let actionSheetController = UIAlertController()
           
        let cancelAction = UIAlertAction(title: "取消", style: UIAlertAction.Style.cancel) { (alertAction) -> Void in
            print("Tap 取消 Button")
        }
           
        let takingPicturesAction = UIAlertAction(title: "拍照", style: UIAlertAction.Style.destructive) { (alertAction) -> Void in
            self.getImageGo(type: 1)
        }
           
        let photoAlbumAction = UIAlertAction(title: "相册", style: UIAlertAction.Style.default) { (alertAction) -> Void in
            self.getImageGo(type: 2)
        }
                   
        actionSheetController.addAction(cancelAction)
        actionSheetController.addAction(takingPicturesAction)
        actionSheetController.addAction(photoAlbumAction)
           
        //iPad设备浮动层设置锚点
        actionSheetController.popoverPresentationController?.sourceView = sender as? UIView
        //显示
        self.present(actionSheetController, animated: true, completion: nil)
    }

    //去拍照或者去相册选择图片
    func getImageGo(type:Int){
        takingPicture =  UIImagePickerController.init()
        if(type==1){
            takingPicture.sourceType = .camera
            //拍照时是否显示工具栏
            //takingPicture.showsCameraControls = true
        }else if(type==2){
            takingPicture.sourceType = .photoLibrary
        }
        //是否截取，设置为true在获取图片后可以将其截取成正方形
        takingPicture.allowsEditing = true
        takingPicture.delegate = self
        present(takingPicture, animated: true, completion: nil)
    }
        
    //拍照或是相册选择返回的图片
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        takingPicture.dismiss(animated: true, completion: nil)
        if(takingPicture.allowsEditing == false){
            //原图
            let img = info[UIImagePickerController.InfoKey.originalImage] as? UIImage
            getOssConfig(img: img!)
        }else{
            //截图
            let img = info[UIImagePickerController.InfoKey.editedImage] as? UIImage
            getOssConfig(img: img!)
        }
    }
    
    func getOssConfig(img: UIImage){
        NetWorkRequest(API.ossConfig, modelType: [OSSConfigModel].self, successCallback: { (config, responseModel) in
            config.forEach({ (item) in
                self.drawRountaPic(orgImag: img, ossConfig: item)
                // self.sendImgToServer(img: img, ossConfig: item)
            })
        }, failureCallback: { (responseModel) in
            print("网络请求失败 getOssConfig")
        })
    }
    
    private func drawRountaPic(orgImag: UIImage, ossConfig: OSSConfigModel){
        let shotest = min(orgImag.size.width, orgImag.size.height)
        let size = CGSize(width: shotest, height: shotest)
        orgImag.createImageCorner(size: size, backgroundColor: UIColor.clear) { image in
            self.sendImgToServer(img: image, ossConfig: ossConfig)
        }
    }
    
    private func sendImgToServer(img: UIImage, ossConfig: OSSConfigModel){
        let fileName = PONTUS_APP_KEY + "_" + String(DimRuntimeData.shared.userInfo?.id ?? 0) + "_" + String(Date().milliStamp) + ".png"
        
        var params = [String: String]()
        params["OSSAccessKeyId"] = ossConfig.AccessKeyId
        params["policy"] = ossConfig.Policy
        params["Signature"] = ossConfig.Signature
        params["success_action_status"] = "201"
        params["key"] = ossConfig.Directory + "/" + fileName
        
        let originData = img.jpegData(compressionQuality: 1.0)
        var imageData: Data?
        if ((originData?.count ?? 0) / 1024) > 5000 {
            imageData = img.jpegData(compressionQuality: 0.1)
        } else {
            imageData = img.jpegData(compressionQuality: 0.3)
        }
        
        if(imageData == nil){
            return
        }
        upload(urlPath: ossConfig.Host, params: params, imageDate: imageData!)
    }
    
    func upload(urlPath: String, params: [String: String], imageDate:Data){
            // 请求url
            let urlString = urlPath
            
            Alamofire.upload(multipartFormData: { (formData) in
                
                // 普通参数 - 拼接到表单
                for (key, value) in params {
                    formData.append(value.data(using: .utf8)!, withName: key)
                }
                /*
                 - 参数01 上传的二进制文件
                 - 参数02 服务器指定的名字 pic  mp4 mp3 区分客户端上传的是什么文件描述
                 - 参数03 文件路径名字 一般可以随意些 及时你写了 服务器也不用
                 - 参数04 告知服务器我们上传的文件的类型 一般可以传入application/octet-stream
                 */
                formData.append(imageDate, withName: "file", fileName: "test.png", mimeType: "image/png")
                
            }, to: urlString) { (result) in
                
                switch result {
                case .success(let upload, _, _):
                    upload.uploadProgress { (progress) in
                        print("上传进度\(progress)")
                    }.responseString(queue: DispatchQueue.default, encoding: String.Encoding.utf8) { (resultString) in
                        let dataString = resultString.data
                        print(dataString)
                        let string = String(decoding: dataString!, as: UTF8.self)
                        let xml = try! XML.parse(string)
                        print(xml["PostResponse"]["Location"].text)
                        let location = xml["PostResponse"]["Location"].text
                        if(location != nil && location != ""){
                            print("上传成功")
                            self.sendModifyToServer(nick_name: "", avatar: location!)
                        } else {
                            print("上传失败")
                            DToast.showToastAction(message: "上传失败，请重试")
                        }
                    }
                    break
                case .failure(let error):
                    print("上传失败2")
                    DToast.showToastAction(message: "上传失败，请重试")
                    break
                }
            }
        }
    
    private func sendModifyToServer(nick_name: String, avatar: String){
        NetWorkRequest(API.updateUserData(nick_name: nick_name, avatar: avatar), modelType: [JuggleServerData].self, successCallback: { (config, responseModel) in
            if(nick_name != ""){
                DimRuntimeData.shared.userInfo?.nick_name = nick_name
                self.nameValueView.text = nick_name
                self.showSelfInfo()
                DToast.showToastAction(message: "昵称修改成功")
            }
            if(avatar != ""){
                DimRuntimeData.shared.userInfo?.avatar = avatar
                self.headView.kf.setImage(urlString: avatar)
                DToast.showToastAction(message: "头像修改成功")
            }
            
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_INFO_CHANGED), object: nil, userInfo:  nil)
        }, failureCallback: { (responseModel) in
            print("getDataByServer failed")
        })
    }
}
