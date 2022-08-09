//
//  ViewController.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        print("当前展示的页面 = ViewController")
        let img = UITextView()
        img.text = "ViewController"

        view.addSubview(img)

        img.snp.makeConstraints { (make) in
            make.width.equalTo(230)
            make.height.equalTo(252)
            make.center.equalTo(view)
        }
    }


}

