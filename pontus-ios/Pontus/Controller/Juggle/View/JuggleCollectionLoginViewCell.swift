//
//  JuggleCollectionLoginViewCell.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/29.
//

import UIKit
import SwiftyFitsize

class JuggleCollectionLoginViewCell: BaseCollectionViewCell {
    
    var loginDataChange: ((JuggleLoginLocalData) -> ())?
    private var loginData = JuggleLoginLocalData()
    
    private var choseClick = false
    
    private lazy var bgView: UIImageView = {
        let bgView = UIImageView()
        bgView.contentMode = .scaleAspectFill
        bgView.clipsToBounds = true
        return bgView
    }()
    
    private lazy var accountView: UITextField = {
        let accountView = UITextField()
        // 圆角
        accountView.layer.cornerRadius = 25~
        // 背景颜色
        accountView.backgroundColor = UIColor.white
        // 边框样式
        accountView.borderStyle = UITextField.BorderStyle.none
        // 提示语
        // accountView.placeholder = "请输入您的邮箱地址"
        // 自动更正功能
        accountView.autocorrectionType = UITextAutocorrectionType.no
        // 完成按钮样式
        accountView.returnKeyType = UIReturnKeyType.done
        // 清除按钮显示状态
        accountView.clearButtonMode = UITextField.ViewMode.whileEditing
        // 键盘样式
        accountView.keyboardType = UIKeyboardType.emailAddress
        // 键盘外观
        accountView.keyboardAppearance = UIKeyboardAppearance.alert
        // 安全文本输入，（输入密码）
        accountView.isSecureTextEntry = false
        
        accountView.addTarget(self, action: #selector(textValueChanged), for: UIControl.Event.editingChanged)
        
        return accountView
    }()
    
    @objc func textValueChanged() {
        print("textValueChanged")
        loginData.account = accountView.text ?? ""
        loginData.password = passwordView.text ?? ""
        loginDataChange!(loginData)
    }
    
    private lazy var passwordView: UITextField = {
        let passwordView = UITextField()
        // 圆角
        passwordView.layer.cornerRadius = 25~
        // 背景颜色
        passwordView.backgroundColor = UIColor.white
        // 边框样式
        passwordView.borderStyle = UITextField.BorderStyle.none
        // 提示语
        // passwordView.placeholder = "请输入您的邮箱地址"
        // 自动更正功能
        passwordView.autocorrectionType = UITextAutocorrectionType.no
        // 完成按钮样式
        passwordView.returnKeyType = UIReturnKeyType.done
        // 清除按钮显示状态
        passwordView.clearButtonMode = UITextField.ViewMode.whileEditing
        // 键盘样式
        passwordView.keyboardType = UIKeyboardType.emailAddress
        // 键盘外观
        passwordView.keyboardAppearance = UIKeyboardAppearance.alert
        // 安全文本输入，（输入密码）
        passwordView.isSecureTextEntry = true
        passwordView.addTarget(self, action: #selector(textValueChanged), for: UIControl.Event.editingChanged)
        
        return passwordView
    }()
    
    private lazy var choseView: UILabel = {
        let choseView = UILabel()
        choseView.font = UIFont.init(name: "iconfont", size: CGFloat(15~))
        
        return choseView
    }()
    
    private lazy var desView: UIView = {
        let desView = UIView()
        desView.backgroundColor = .clear
        return desView
    }()
    private lazy var des1View: UILabel = {
        let des1View = UILabel()
        des1View.font = UIFont.systemFont(ofSize: 12~)
        des1View.text = "我已阅读并同意"
        return des1View
    }()
    private lazy var des2View: UILabel = {
        let des2View = UILabel()
        des2View.font = UIFont.systemFont(ofSize: 12~)
        des2View.text = "\"隐私政策\""
        return des2View
    }()
    private lazy var des3View: UILabel = {
        let des3View = UILabel()
        des3View.font = UIFont.systemFont(ofSize: 12~)
        des3View.text = "和"
        return des3View
    }()
    private lazy var des4View: UILabel = {
        let des4View = UILabel()
        des4View.font = UIFont.systemFont(ofSize: 12~)
        des4View.text = "\"用户协议\""
        return des4View
    }()
    
    // 继承父类方法 布局
    override func setupLayout() {
        clipsToBounds = true
        
        addSubview(bgView)
        bgView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
        
        addSubview(accountView)
        accountView.snp.makeConstraints { make in
            make.left.equalToSuperview().offset(20~)
            make.right.equalToSuperview().offset(-20~)
            make.top.equalToSuperview().offset(40~)
            make.width.equalTo(screenWidth - 40~)
            make.height.equalTo(50~)
        }
        
        addSubview(passwordView)
        passwordView.snp.makeConstraints { make in
            make.left.equalToSuperview().offset(20~)
            make.right.equalToSuperview().offset(-20~)
            make.top.equalTo(accountView.snp_bottom).offset(20~)
            make.width.equalTo(screenWidth - 40~)
            make.height.equalTo(50~)
        }

        addSubview(desView)
        desView.snp.makeConstraints { make in
            make.left.equalToSuperview()
            make.right.equalToSuperview()
            make.bottom.equalToSuperview().offset(10~)
            make.width.equalTo(screenWidth)
            make.height.equalTo(50~)
        }
        
        desView.addSubview(choseView)
        choseView.snp.makeConstraints { make in
            make.left.equalToSuperview().offset(60~)
            make.centerY.equalToSuperview()
            make.width.equalTo(20~)
            make.height.equalTo(20~)
        }
        
        let tapGesture = UITapGestureRecognizer.init(target: self, action: #selector(didTapChoseView(_:)))
        choseView.isUserInteractionEnabled = true
        choseView.addGestureRecognizer(tapGesture)
        
        desView.addSubview(des1View)
        des1View.setContentCompressionResistancePriority(UILayoutPriority(rawValue: 1000), for: .horizontal)
        des1View.snp.makeConstraints { make in
            make.left.equalTo(choseView.snp_right)
            make.centerY.equalToSuperview()
            make.height.equalTo(50~)
        }
        
        desView.addSubview(des2View)
        des2View.snp.makeConstraints { make in
            make.left.equalTo(des1View.snp_right)
            make.centerY.equalToSuperview()
            make.height.equalTo(50~)
        }
        
        desView.addSubview(des3View)
        des3View.snp.makeConstraints { make in
            make.left.equalTo(des2View.snp_right)
            make.centerY.equalToSuperview()
            make.height.equalTo(50~)
        }
        
        desView.addSubview(des4View)
        des4View.snp.makeConstraints { make in
            make.left.equalTo(des3View.snp_right)
            make.centerY.equalToSuperview()
            make.height.equalTo(50~)
        }
    }
    
    @objc private func didTapChoseView(_ sender: UITapGestureRecognizer) {
        print("按钮被点击")
        if(choseClick){
            choseView.textColor = getUIColorByColor(color: model!.checkbox_bg_color!)
            choseView.text = "\u{e65c}"
        } else {
            choseView.textColor = getUIColorByColor(color: model!.checkbox_line_color!)
            choseView.text = "\u{e6ce}"
        }
        choseClick = !choseClick
        
        loginData.isAgree = choseClick
        loginDataChange!(loginData)
    }
    
    var model: JugglePageRows? {
        didSet {
            guard let model = model else { return }
            
            bgView.kf.setImage(urlString: model.retouch,
                                 placeholder: UIImage(named: "normal_placeholder_h"))
            accountView.setValue(10~, forKey: "paddingLeft")
            passwordView.setValue(10~, forKey: "paddingLeft")
            accountView.placeholder = "   " + (model.login_name_text ?? "")
            passwordView.placeholder = "   " + (model.login_password_text ?? "")
            loginData.accountHint = (model.login_name_text ?? "")
            loginData.passwordHint = (model.login_password_text ?? "")
            
            choseView.textColor = getUIColorByColor(color: model.checkbox_bg_color!)
            choseView.text = "\u{e65c}"
            
            des1View.textColor = getUIColorByColor(color: model.description_text_color!)
            des2View.textColor = getUIColorByColor(color: model.link_text_color!)
            des3View.textColor = getUIColorByColor(color: model.description_text_color!)
            des4View.textColor = getUIColorByColor(color: model.link_text_color!)
            
            loginDataChange?(loginData)
        }
    }
}
