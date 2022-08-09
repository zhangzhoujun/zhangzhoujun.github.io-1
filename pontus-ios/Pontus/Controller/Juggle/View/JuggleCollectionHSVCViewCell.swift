//
//  JuggleCollectionHSVCViewCell.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/28.
//

import UIKit

class JuggleCollectionHSVCViewCell: BaseCollectionViewCell {
    
    private lazy var iconView: UIImageView = {
        let iconView = UIImageView()
        iconView.contentMode = .scaleAspectFill
        iconView.clipsToBounds = true
        return iconView
    }()
    
    // 继承父类方法 布局
    override func setupLayout() {
        clipsToBounds = true
        
        contentView.addSubview(iconView)
        iconView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
        
        let tapGesture = UITapGestureRecognizer.init(target: self, action: #selector(didTapView(_:)))
        iconView.isUserInteractionEnabled = true
        iconView.addGestureRecognizer(tapGesture)
    }
    
    @objc private func didTapView(_ sender: UITapGestureRecognizer) {
        print("JuggleCollectionHSVCViewCell 图片被点击")
        onJuggleItemClick(data: model!)
    }
    
    var model: JugglePageRowsData? {
        didSet {
            guard let model = model else { return }
            iconView.kf.setImage(urlString: model.img_url,
                                 placeholder: UIImage(named: "normal_placeholder_h"))
        }
    }
}
