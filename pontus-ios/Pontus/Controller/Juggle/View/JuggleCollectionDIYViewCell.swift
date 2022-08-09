//
//  JuggleCollectionDIYViewCell.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/29.
//

import UIKit
import SwiftyJSON


class JuggleCollectionDIYViewCell: BaseCollectionViewCell {
    
    private lazy var iconView: UIImageView = {
        let iconView = UIImageView()
        iconView.contentMode = .scaleAspectFill
        iconView.clipsToBounds = true
        return iconView
    }()
    
    private func onDIYInit() {
        print("DIY --- INIT --- ...")
        let widHei = String((model?.jugglemain?.page_base?.width_height)!)
        let array: Array = widHei.split(separator: "*")
        let width = floor(Double(screenWidth))
        let hei = width * Double(array[1])! / Double(array[0])!
        
        let count = model?.jugglemain?.rows?.count ?? 0
        for i in 0..<count {
            let item = model?.jugglemain?.rows?[i]
            if(item?.data_type == "text"){
                createTextView(item: item!, rootWid: width, rootHei: hei, pos: i)
            } else if(item?.data_type == "button"){
                createButtonViewData(item: item!, rootWid: width, rootHei: hei, data: (model?.dataList[(item?.data_server_link!)!]), pos: i)
            }  else if(item?.data_type == "data"){
                createTextViewData(item: item!, rootWid: width, rootHei: hei, data: (model?.dataList[(item?.data_server_link!)!]), pos: i)
            }
        }
    }
    
    private func createTextView(item: JugglePageRows, rootWid: Double, rootHei: Double, pos: Int) {
        let textView = UILabel()
        textView.tag = pos
        textView.backgroundColor = .clear
        if(item.font_weight == true){
            textView.font = UIFont.boldSystemFont(ofSize: CGFloat(Int(item.font_size!)!))
        } else {
            textView.font = UIFont.systemFont(ofSize: CGFloat(Int(item.font_size!)!))
        }
        
        if (item.text_align == "left") {
            textView.textAlignment = .left
        } else if (item.text_align == "center") {
            textView.textAlignment = .center
        } else if (item.text_align == "right") {
            textView.textAlignment = .right
        }
        
        textView.textColor = getUIColorByColor(color: item.font_color!)
        
        addSubview(textView)
        
        let viewWid = rootWid * item.relatively!.width! / 100
        let viewHei = rootHei * item.relatively!.height! / 100
        let leftMargint = rootWid * item.relatively!.left! / 100
        let topMarging = rootHei * item.relatively!.top! / 100
        
        textView.snp.makeConstraints { make in
            make.width.equalTo(viewWid)
            make.height.equalTo(viewHei)
            make.top.equalToSuperview().offset(topMarging)
            make.left.equalToSuperview().offset(leftMargint)
        }
        
        textView.text = item.text
        
        let tapGestureCopus = UITapGestureRecognizer.init(target: self, action: #selector(didTapView(_:)))
        textView.isUserInteractionEnabled = true
        textView.addGestureRecognizer(tapGestureCopus)
    }
    
    // 继承父类方法 布局
    override func setupLayout() {
        clipsToBounds = true
        
        contentView.addSubview(iconView)
        iconView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
    
    var model: JuggleServerLocalData? {
        didSet {
            guard let model = model else { return }
            
            iconView.kf.setImage(urlString: model.jugglemain?.page_base?.bg_url,
                                 placeholder: UIImage(named: "normal_placeholder_h"))
            
            onDIYInit()
        }
    }
    
    private func createTextViewData(item: JugglePageRows, rootWid: Double, rootHei: Double,data: Data?, pos: Int) {
        let json = JSON(data)
        let textView = UILabel()
        textView.tag = pos
        textView.backgroundColor = .clear
        if(item.font_weight == true){
            textView.font = UIFont.boldSystemFont(ofSize: CGFloat((Int(item.font_size!)! / 2)))
        } else {
            textView.font = UIFont.systemFont(ofSize: CGFloat((Int(item.font_size!)! / 2)))
        }
        
        if (item.text_align == "left") {
            textView.textAlignment = .left
        } else if (item.text_align == "center") {
            textView.textAlignment = .center
        } else if (item.text_align == "right") {
            textView.textAlignment = .right
        }
        
        textView.textColor = getUIColorByColor(color: item.font_color!)
        
        addSubview(textView)
        
        let viewWid = rootWid * item.relatively!.width! / 100
        let viewHei = rootHei * item.relatively!.height! / 100
        let leftMargint = rootWid * item.relatively!.left! / 100
        let topMarging = rootHei * item.relatively!.top! / 100
        
        textView.snp.makeConstraints { make in
            make.width.equalTo(viewWid)
            make.height.equalTo(viewHei)
            make.top.equalToSuperview().offset(topMarging)
            make.left.equalToSuperview().offset(leftMargint)
        }
        var showString = item.text ?? ""
        for eachData in json {
            let key = String(eachData.0)
            let value = eachData.1.rawString() ?? ""
            showString = showString.replacingOccurrences(of: ("{" + key + "}"), with: value)
        }
        
        textView.text = showString
        
        let tapGestureCopus = UITapGestureRecognizer.init(target: self, action: #selector(didTapView(_:)))
        textView.isUserInteractionEnabled = true
        textView.addGestureRecognizer(tapGestureCopus)
    }
    
    private func createButtonViewData(item: JugglePageRows, rootWid: Double, rootHei: Double,data: Data?, pos: Int) {
        let json = JSON(data)
        let imgView = UIImageView()
        imgView.tag = pos
        contentView.addSubview(imgView)
        let viewWid = rootWid * item.relatively!.width! / 100
        let viewHei = rootHei * item.relatively!.height! / 100
        let leftMargint = rootWid * item.relatively!.left! / 100
        let topMarging = rootHei * item.relatively!.top! / 100
        
        imgView.snp.makeConstraints { make in
            make.width.equalTo(viewWid)
            make.height.equalTo(viewHei)
            make.top.equalToSuperview().offset(topMarging)
            make.left.equalToSuperview().offset(leftMargint)
        }
        
        let radius = item.radius![0] * 2
        
        if(item.data_key?.isEmpty == true){
            imgView.kf.setImage(urlString: item.trigger_img,
                                placeholder: UIImage(named: "normal_placeholder_h"), radius: radius)
        } else {
            imgView.kf.setImage(urlString: json[item.data_key!].rawString(),
                                 placeholder: UIImage(named: "normal_placeholder_h"), radius: radius)
        }
        let tapGestureCopus = UITapGestureRecognizer.init(target: self, action: #selector(didTapView(_:)))
        imgView.isUserInteractionEnabled = true
        imgView.addGestureRecognizer(tapGestureCopus)
    }
    
    @objc private func didTapView(_ sender: UITapGestureRecognizer) {
        print("didTapView 图片被点击")
        let index = sender.view?.tag ?? -1
        if(index != -1){
            onJuggleItemClick(data: (model!.jugglemain?.rows![index])!)
        }
    }
    
}
