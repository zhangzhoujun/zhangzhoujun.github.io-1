//
//  JuggleCollectionGoodViewCell.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/26.
//

import UIKit
import SwiftyFitsize

// 设置样式
enum JuggleCollectionGoodViewCellStyle {
    case line
    case grid
}

class JuggleCollectionGoodViewCell: BaseCollectionViewCell {
    
    private lazy var bgView: UIView = {
        let bgView = UIView()
        bgView.backgroundColor = UIColor.white
        return bgView
    }()
    
    private lazy var bottomLineView: UIView = {
        let bottomLineView = UIView()
        bottomLineView.backgroundColor = UIColor.background
        return bottomLineView
    }()
    
    private lazy var iconView: UIImageView = {
        let iconView = UIImageView()
        iconView.contentMode = .scaleAspectFill
        iconView.clipsToBounds = true
        return iconView
    }()
    
    private lazy var nameView: UILabel = {
        let nameView = UILabel()
        nameView.textColor = UIColor.black
        nameView.font = UIFont.systemFont(ofSize: 20)
        nameView.numberOfLines = 2
        nameView.lineBreakMode = NSLineBreakMode.byTruncatingTail
        nameView.isUserInteractionEnabled = false
        return nameView
    }()
    
    private lazy var priceView: UILabel = {
        let priceView = UILabel()
        priceView.textColor = UIColor.red
        priceView.font = UIFont.boldSystemFont(ofSize: 20)
        priceView.isUserInteractionEnabled = false
        return priceView
    }()
    
    private lazy var buyBtn: UIButton = {
        let buyBtn = UIButton()
        buyBtn.setTitle("立即购买", for: UIControl.State.normal)
        buyBtn.backgroundColor = UIColor.red
        buyBtn.layer.cornerRadius = 15~
        buyBtn.contentMode = ContentMode.center
        buyBtn.titleLabel?.font = UIFont.systemFont(ofSize: 16)
        buyBtn.titleLabel?.textColor = UIColor.white
        return buyBtn
    }()
    
    private lazy var copusBtn: UIButton = {
        let buyBtn = UIButton()
        buyBtn.setTitle("领取优惠卷", for: UIControl.State.normal)
        buyBtn.backgroundColor = UIColor.red
        buyBtn.layer.cornerRadius = 15~
        buyBtn.contentMode = ContentMode.center
        buyBtn.titleLabel?.font = UIFont.systemFont(ofSize: 16)
        buyBtn.titleLabel?.textColor = UIColor.white
        return buyBtn
    }()
    
    // 继承父类方法 布局
    override func setupLayout() {
        clipsToBounds = true
        
        contentView.addSubview(bgView)
        bgView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
        
        contentView.addSubview(bottomLineView)
        bottomLineView.snp.makeConstraints { make in
            make.left.right.bottom.equalToSuperview()
            make.height.equalTo(10~)
        }
        
        contentView.addSubview(iconView)
        
        contentView.addSubview(nameView)

        contentView.addSubview(priceView)
        
        contentView.addSubview(buyBtn)
        
        contentView.addSubview(copusBtn)
        
        let tapGestureBuy = UITapGestureRecognizer.init(target: self, action: #selector(didTapBuyView(_:)))
        buyBtn.isUserInteractionEnabled = true
        buyBtn.addGestureRecognizer(tapGestureBuy)
        
        let tapGestureCopus = UITapGestureRecognizer.init(target: self, action: #selector(didTapCopusView(_:)))
        copusBtn.isUserInteractionEnabled = true
        copusBtn.addGestureRecognizer(tapGestureCopus)
    }
    
    @objc private func didTapBuyView(_ sender: UITapGestureRecognizer) {
        print("didTapBuyView 图片被点击")
        gotoNewWebviewController(url: model?.link)
    }
    
    @objc private func didTapCopusView(_ sender: UITapGestureRecognizer) {
        print("didTapCopusView 图片被点击")
        gotoNewWebviewController(url: model?.coupon_link)
    }
    
    // 设置样式
    var cellStyle: JuggleCollectionGoodViewCellStyle = .line {
        didSet {
            switch cellStyle {
            case .line:
                iconView.snp.makeConstraints { make in
                    make.top.left.equalToSuperview().offset(10~)
                    make.width.equalTo(130~)
                    make.height.equalTo(130~)
                }
                
                nameView.snp.makeConstraints { make in
                    make.top.equalToSuperview().offset(10~)
                    make.left.equalTo(iconView.snp.right).offset(10~)
                    make.width.equalTo(220~)
                    make.height.equalTo(55~)
                }

                priceView.snp.makeConstraints { make in
                    make.top.equalTo(nameView.snp.bottom)
                    make.left.equalTo(iconView.snp.right).offset(10~)
                    make.width.equalTo(200~)
                    make.height.equalTo(35~)
                }
                
                buyBtn.snp.makeConstraints { make in
                    make.bottom.equalToSuperview().offset(-20~)
                    make.right.equalToSuperview().offset(-10~)
                    make.width.equalTo(80~)
                    make.height.equalTo(35~)
                }
                
                copusBtn.snp.makeConstraints { make in
                    make.bottom.equalToSuperview().offset(-20~)
                    make.right.equalTo(buyBtn.snp_left).offset(-10~)
                    make.width.equalTo(100~)
                    make.height.equalTo(35~)
                }
            case .grid:
                iconView.snp.makeConstraints { make in
                    make.top.left.equalToSuperview().offset(10~)
                    make.width.equalTo(screenWidth / 2 - 25~)
                    make.height.equalTo(screenWidth / 2 - 25~)
                }
                
                nameView.snp.makeConstraints { make in
                    make.top.equalTo(iconView.snp_bottom).offset(5~)
                    make.left.equalToSuperview().offset(10~)
                    make.width.equalTo(screenWidth / 2 - 25~)
                    make.height.equalTo(55~)
                }

                priceView.snp.makeConstraints { make in
                    make.top.equalTo(nameView.snp_bottom)
                    make.left.equalToSuperview().offset(5~)
                    make.width.equalTo(160~)
                    make.height.equalTo(35~)
                }
                
                buyBtn.snp.makeConstraints { make in
                    make.bottom.equalToSuperview().offset(-20~)
                    make.right.equalToSuperview().offset(-5~)
                    make.width.equalTo(70~)
                    make.height.equalTo(35~)
                }
                
                copusBtn.snp.makeConstraints { make in
                    make.bottom.equalToSuperview().offset(-20~)
                    make.right.equalTo(buyBtn.snp_left).offset(-10~)
                    make.width.equalTo(90~)
                    make.height.equalTo(35~)
                    
                }
            }
        }
    }
    
    var model: JugglePageRowsData? {
        didSet {
            guard let model = model else { return }
            iconView.kf.setImage(urlString: model.main_img,
                                 placeholder: UIImage(named: "normal_placeholder_h"))
            
            nameView.text = model.goods_name ?? ""
            priceView.text = "¥ " + (model.price ?? "")
            
            if(model.coupon_link == nil || model.coupon_link?.isEmpty == true){
                copusBtn.isHidden = true
            } else {
                copusBtn.isHidden = false
            }
        }
    }
}
