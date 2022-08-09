//
//  JuggleCollectionBannerViewCell.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/26.
//

import UIKit
import LLCycleScrollView

class JuggleCollectionBannerViewCell: BaseCollectionViewCell {
    
    private lazy var iconView: UIImageView = {
        let iconView = UIImageView()
        iconView.contentMode = .scaleAspectFill
        iconView.clipsToBounds = true
        return iconView
    }()
    
    private lazy var bannerView: LLCycleScrollView = {
        let cycleScrollView = LLCycleScrollView()
        cycleScrollView.backgroundColor = UIColor.clear
        cycleScrollView.autoScrollTimeInterval = 3
        cycleScrollView.placeHolderImage = UIImage(named: "normal_placeholder_h")
        cycleScrollView.coverImage = UIImage(named: "normal_placeholder_h")
        cycleScrollView.pageControlBottom = 20
        cycleScrollView.titleBackgroundColor = UIColor.clear
        cycleScrollView.customPageControlStyle = .snake
        cycleScrollView.pageControlPosition = .center
//        cycleScrollView.pageControlActiveImage = UIImage(named: "emojiCommunity")
        cycleScrollView.pageControlInActiveImage = UIImage(named: "finishobj")

        // 点击 item 回调
        cycleScrollView.lldidSelectItemAtIndex = didSelectBanner(index:)
        return cycleScrollView
    }()
    
    private func didSelectBanner(index: NSInteger) {
        print("轮播图被点击了...")
        onJuggleItemClick(data: model!.data![index])
    }
    
    // 继承父类方法 布局
    override func setupLayout() {
        clipsToBounds = true
        
        contentView.addSubview(iconView)
        iconView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
        
        contentView.addSubview(bannerView)
        bannerView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
    
    var model: JugglePageRows? {
        didSet {
            guard let model = model else { return }
            iconView.kf.setImage(urlString: model.retouch,
                                 placeholder: UIImage(named: "normal_placeholder_h"))
            
            bannerView.imagePaths = model.data?.filter { $0.img_url != nil }.map { $0.img_url! } ?? []
            
        }
    }
}
