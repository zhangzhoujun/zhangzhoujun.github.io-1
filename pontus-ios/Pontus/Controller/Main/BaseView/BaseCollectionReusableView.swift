//
//  BaseCollectionReusableView.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/24.
//

import Reusable

class BaseCollectionReusableView: UICollectionReusableView,Reusable {
    override init(frame: CGRect) {
        super.init(frame: frame)
        setupLayout()
    }
    
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    open func setupLayout() {}
}
