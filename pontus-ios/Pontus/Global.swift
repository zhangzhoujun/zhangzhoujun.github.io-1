//
//  Global.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/23.
//

import Foundation
import UIKit
import Kingfisher
import SnapKit
import MJRefresh

//MRAK: 应用默认颜色
extension UIColor {
    class var background: UIColor {
        return UIColor(r: 245, g: 245, b: 245)
    }
    
    class var theme: UIColor {
        return UIColor(r: 27, g: 221, b: 142)
    }
}

extension String {
    static let searchHistoryKey = "searchHistoryKey"
    static let sexTypeKey = "sexTypeKey"
}

extension NSNotification.Name {
    static let USexTypeDidChange = NSNotification.Name("USexTypeDidChange")
}

let screenWidth = UIScreen.main.bounds.width
let screenHeight = UIScreen.main.bounds.height

var topVC: UIViewController? {
    var resultVC: UIViewController?
    resultVC = _topVC(UIApplication.shared.keyWindow?.rootViewController)
    while resultVC?.presentedViewController != nil {
        resultVC = _topVC(resultVC?.presentedViewController)
    }
    return resultVC
}

var isIphoneX: Bool {
    return UI_USER_INTERFACE_IDIOM() == .phone
        && (max(UIScreen.main.bounds.height, UIScreen.main.bounds.width) == 812
            || max(UIScreen.main.bounds.height, UIScreen.main.bounds.width) == 896)
}

private func _topVC(_ vc: UIViewController?) -> UIViewController? {
    if vc is UINavigationController {
        return _topVC((vc as? UINavigationController)?.topViewController)
    } else if vc is UITabBarController {
        return _topVC((vc as? UITabBarController)?.selectedViewController)
    } else {
        return vc
    }
}

//MARK: print
func uLog<T>(_ message: T, file: String = #file, function: String = #function, lineNumber: Int = #line) {
    #if DEBUG
    let fileName = (file as NSString).lastPathComponent
    print("[\(fileName):funciton:\(function):line:\(lineNumber)]- \(message)")
    #endif
}

//MARK: Kingfisher
extension Kingfisher where Base: ImageView {
    @discardableResult
    public func setImage(urlString: String?, placeholder: Placeholder? = UIImage(named: "normal_placeholder_h"), radius: Int = 0) -> RetrieveImageTask {
        return setImage(with: URL(string: urlString ?? ""),
                        placeholder: placeholder,
                        options:[.transition(.fade(0.5)), .processor(RoundCornerImageProcessor(cornerRadius: CGFloat(radius)))])
    }
}

extension Kingfisher where Base: Button {
    @discardableResult
    public func setImage(urlString: String?, for state: UIControl.State, placeholder: UIImage? = UIImage(named: "normal_placeholder_h")) -> RetrieveImageTask {
        return setImage(with: URL(string: urlString ?? ""),
                        for: state,
                        placeholder: placeholder,
                        options: [.transition(.fade(0.5))])
        
    }
}
//MARK: SnapKit
extension ConstraintView {
    
    var usnp: ConstraintBasicAttributesDSL {
        if #available(iOS 11.0, *) {
            return self.safeAreaLayoutGuide.snp
        } else {
            return self.snp
        }
    }
}

extension UICollectionView {
    
    func reloadData(animation: Bool = true) {
        if animation {
            reloadData()
        } else {
            UIView.performWithoutAnimation {
                reloadData()
            }
        }
    }
}


extension UIButton {

    //MARK: -定义button相对label的位置
    enum ButtonImagePosition {
            case top          //图片在上，文字在下，垂直居中对齐
            case bottom       //图片在下，文字在上，垂直居中对齐
            case left         //图片在左，文字在右，水平居中对齐
            case right        //图片在右，文字在左，水平居中对齐
    }
    /// - Description 设置Button图片的位置
    /// - Parameters:
    ///   - style: 图片位置
    ///   - spacing: 按钮图片与文字之间的间隔
    func imagePosition(style: ButtonImagePosition, spacing: CGFloat) {
        //得到imageView和titleLabel的宽高
        let imageWidth = self.imageView?.frame.size.width
        let imageHeight = self.imageView?.frame.size.height
        
        var labelWidth: CGFloat! = 0.0
        var labelHeight: CGFloat! = 0.0
        
        labelWidth = self.titleLabel?.intrinsicContentSize.width
        labelHeight = self.titleLabel?.intrinsicContentSize.height
        
        //初始化imageEdgeInsets和labelEdgeInsets
        var imageEdgeInsets = UIEdgeInsets.zero
        var labelEdgeInsets = UIEdgeInsets.zero
        
        //根据style和space得到imageEdgeInsets和labelEdgeInsets的值
        switch style {
        case .top:
            //上 左 下 右
            imageEdgeInsets = UIEdgeInsets(top: -labelHeight-spacing/2, left: 0, bottom: 0, right: -labelWidth)
            labelEdgeInsets = UIEdgeInsets(top: 0, left: -imageWidth!, bottom: -imageHeight!-spacing/2, right: 0)
            break;
            
        case .left:
            imageEdgeInsets = UIEdgeInsets(top: 0, left: -spacing/2, bottom: 0, right: spacing)
            labelEdgeInsets = UIEdgeInsets(top: 0, left: spacing/2, bottom: 0, right: -spacing/2)
            break;
            
        case .bottom:
            imageEdgeInsets = UIEdgeInsets(top: 0, left: 0, bottom: -labelHeight!-spacing/2, right: -labelWidth)
            labelEdgeInsets = UIEdgeInsets(top: -imageHeight!-spacing/2, left: -imageWidth!, bottom: 0, right: 0)
            break;
            
        case .right:
            imageEdgeInsets = UIEdgeInsets(top: 0, left: labelWidth+spacing/2, bottom: 0, right: -labelWidth-spacing/2)
            labelEdgeInsets = UIEdgeInsets(top: 0, left: -imageWidth!-spacing/2, bottom: 0, right: imageWidth!+spacing/2)
            break;
            
        }
        
        self.titleEdgeInsets = labelEdgeInsets
        self.imageEdgeInsets = imageEdgeInsets
        
    }
}

extension UIViewController {
    class func currentViewController(base: UIViewController? = UIApplication.shared.keyWindow?.rootViewController) -> UIViewController? {
        if let nav = base as? UINavigationController {
            return currentViewController(base: nav.visibleViewController)
        }
        if let tab = base as? UITabBarController {
            return currentViewController(base: tab.selectedViewController)
        }
        if let presented = base?.presentedViewController {
            return currentViewController(base: presented)
        }
        return base
    }
}

typealias CCornersRadius = (topLeft: CGFloat, topRight: CGFloat, bottomLeft: CGFloat, bottomRight: CGFloat)

extension UIView {
    // 自定义圆角

    //创建Path
    func createPath(bounds:CGRect, cornersRadius:CCornersRadius) -> CGPath {
            let minX = bounds.minX
            let minY = bounds.minY
            let maxX = bounds.maxX
            let maxY = bounds.maxY

            let topLeftCenterX = minX + cornersRadius.topLeft
            let topLeftCenterY = minY + cornersRadius.topLeft
            let topRightCenterX = maxX - cornersRadius.topRight
            let topRightCenterY = minY + cornersRadius.topRight
            let bottomLeftCenterX = minX + cornersRadius.bottomLeft
            let bottomLeftCenterY = maxY - cornersRadius.bottomLeft
            let bottomRightCenterX = maxX - cornersRadius.bottomRight
            let bottomRightCenterY = maxY - cornersRadius.bottomRight
            
            let path = CGMutablePath()
            path.addArc(center: CGPoint(x: topLeftCenterX, y: topLeftCenterY), radius: cornersRadius.topLeft, startAngle: CGFloat(Double.pi), endAngle: CGFloat(3 * Double.pi / 2.0), clockwise: false)
            path.addArc(center: CGPoint(x: topRightCenterX, y: topRightCenterY), radius: cornersRadius.topRight, startAngle: CGFloat(3 * Double.pi / 2.0), endAngle: 0, clockwise: false)
            path.addArc(center: CGPoint(x: bottomRightCenterX, y: bottomRightCenterY), radius: cornersRadius.bottomRight, startAngle: 0, endAngle: CGFloat(Double.pi / 2.0), clockwise: false)
            path.addArc(center: CGPoint(x: bottomLeftCenterX, y: bottomLeftCenterY), radius: cornersRadius.bottomLeft, startAngle: CGFloat(Double.pi / 2.0), endAngle: CGFloat(Double.pi), clockwise: false)
            path.closeSubpath()
            
            return path
    }
}

func getDateBytimeStamp(_ timeStamp: Int) -> String {
    //转换为时间
    let timeInterval:TimeInterval = TimeInterval(timeStamp)
    let date = NSDate(timeIntervalSince1970: timeInterval)
     
    //格式话输出
    let dformatter = DateFormatter()
    dformatter.dateFormat = "yyyy-MM-dd"
    let dateStr = dformatter.string(from: date as Date)
    return dateStr
}

func getNewUIViewController(pagePath: String?, showHead: Bool = true, fromLoading: Bool = false) -> UIViewController? {
    if(pagePath?.isEmpty == true){
        return nil
    }
    var vc: UIViewController? = nil
    if((pagePath?.starts(with: "http")) == true){
        vc = WebViewController(url: pagePath)
    } else if((pagePath?.starts(with: "/JuggleFra/index")) == true || (pagePath?.starts(with: "/Juggle/index")) == true){
        let pageArray: Array = pagePath!.split(separator: "=")
        if(pageArray.count == 2){
            vc = JuggleVC(key: String(pageArray[1]), showHead: showHead, fromLoading: fromLoading)
        }
    } else if(pagePath == "/mineAct/set") {
        vc = SetVC()
    } else if(pagePath == "/mineAct/info") {
        vc = SelfInfoVC()
    } else {
        vc = nil
    }
    return vc
}

func gotoLoginController(fromLoading: Bool){
    let vc = JuggleVC(key: DimRuntimeData.shared.appConfig?.base_data?.login_path)
    topVC?.navigationController?.pushViewController(vc , animated: true)
}

func gotoNewWebviewController(url: String?){
    if(url?.isEmpty == true){
        return
    }
    let vc = WebViewController(url: url)
    topVC?.navigationController?.pushViewController(vc , animated: true)
}

func gotoLocalController(pagePath: String?){
    if(pagePath?.isEmpty == true){
        return
    }
    let vc = getNewUIViewController(pagePath: pagePath, showHead: true)
    if(vc != nil){
        topVC?.navigationController?.pushViewController(vc!, animated: true)
    }
}

/**
 颜色值转换
 */
func getUIColorByColor(color: String) -> UIColor{
    if(color.isEmpty){
        return UIColor.background
    }
    if(color.count < 7){
        return UIColor.background
    }
    if(color.starts(with: "#")){
        return colorWithHexString(hex: color)
    }
    return getUIColorWithARGB(color: color)
}

private func colorWithHexString(hex: String, alpha: Double = 1.0) -> UIColor {
    var cString = hex.trimmingCharacters(in:CharacterSet.whitespacesAndNewlines).uppercased()
    if (cString.hasPrefix("#")) {
        let index = cString.index(cString.startIndex, offsetBy:1)
        cString = String(cString[index...])
    }
    if (cString.count != 6) {
        return UIColor.background
    }
    let rIndex = cString.index(cString.startIndex, offsetBy: 2)
    let rString = String(cString[..<rIndex])
    let otherString = String(cString[rIndex...])
    let gIndex = otherString.index(otherString.startIndex, offsetBy: 2)
    let gString = String(otherString[..<gIndex])
    let bIndex = cString.index(cString.endIndex, offsetBy: -2)
    let bString = String(cString[bIndex...])
    var r:UInt64 = 0, g:UInt64 = 0, b:UInt64 = 0;
    Scanner(string: rString).scanHexInt64(&r)
    Scanner(string: gString).scanHexInt64(&g)
    Scanner(string: bString).scanHexInt64(&b)
    return UIColor(red: CGFloat(r) / 255.0, green: CGFloat(g) / 255.0, blue: CGFloat(b) / 255.0, alpha: CGFloat(alpha))
}

private func getUIColorWithARGB(color: String) -> UIColor{
    if(color.starts(with: "rgba(") == false){
        return UIColor.background
    }
    let colorEnd = color.replacingOccurrences(of: "rgba(", with: "").replacingOccurrences(of: ")", with: "")
        .replacingOccurrences(of: " ", with: "")
    let colorArray = colorEnd.split(separator: ",")
    if(colorArray.count < 4){
        return UIColor.background
    }
    let rString = String(colorArray[0])
    let gString = String(colorArray[1])
    let bString = String(colorArray[2])
    let aString = String(colorArray[3])
    
    let rInt = Int(rString)
    let gInt = Int(gString)
    let bInt = Int(bString)
    let aDouble = Double(aString) ?? 1.0
    
    let rhex = String(rInt!,radix:16)
    let ghex = String(gInt!,radix:16)
    let bhex = String(bInt!,radix:16)
    
    return colorWithHexString(hex: "#" + getHexByHex(hex: rhex) +  getHexByHex(hex: ghex) +  getHexByHex(hex: bhex), alpha: aDouble)
}

private func getHexByHex(hex: String) -> String{
    if(hex.count == 1){
        return "0" + hex
    }
    return hex
}

func userLogout(){
    DimRuntimeData.shared.userInfo = nil
    DimRuntimeData.shared.setUserToken(token: "")
    
    AppPreferences.shared.setUserLogout()
        
    NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_USERINFO_LIGOUT), object: nil, userInfo:  nil)
    
    gotoLoginController(fromLoading: false)
}
