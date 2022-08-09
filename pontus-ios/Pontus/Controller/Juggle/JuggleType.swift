//
//  JuggleType.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/26.
//

import Foundation

enum JuggleType:String {
    // 宽度撑满的轮播图
    case BANNER = "1"
    // 宽度不撑满的轮播图
    case BANNER_EX = "2"
    // 通栏
    case LINE_ONE = "3"
    // 横向滚动
    case H_SCROLL = "4"
    // 等分
    case LINE_CROP = "5"
    // 自定义宽度
    case LINE_CROP_DIY = "9"
    // 数据组件
    case DATA_SKIN = "17"
    // 登录表单组件
    case LOGIN_DATA = "30"
    // DIY组件
    case DATA_DIY = "-999"
}
