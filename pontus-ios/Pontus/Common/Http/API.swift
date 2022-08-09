//
//  API.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/24.
//

import Foundation
import Moya

enum API{
    // APP基础配置
    case appConfig
    // 获取loki配置的juggle数据
    case lokiData(key: String)
    // 根据服务端配置的接口请求对应的数据
    case serverData(urlPath: String)
    // 登录
    case login(account: String, password: String)
    // 用户信息
    case userData
    // 版本更新接口
    case versionCheck
    // 修改头像昵称
    case updateUserData(nick_name: String, avatar: String)
    // OSS
    case ossConfig
    // 图片上传
    case uploadImg(urlPath: String, params: [String: String], imageDate:Data)
    
    case updateAPi(parameters:[String:Any])
    case register(email:String,password:String)
    //上传用户头像
    case uploadHeadImage(parameters: [String:Any],imageDate:Data)
    case easyRequset
}

extension API:TargetType{
    var baseURL: URL {
        switch self {
        case .easyRequset:
            return URL.init(string:(BASE_SERVER_URL))!
        case .uploadImg(let urlPath, _, _):
            return URL.init(string:(urlPath))!
        default:
            return URL.init(string:(BASE_SERVER_URL))!
        }
    }
    
    var path: String {
        switch self {
        case .appConfig:
            return "api/app_config"
        case .lokiData:
            return "api/get_loki_data"
        case let .serverData(urlPath):
            return urlPath
        case .login:
            return "api/login"
        case .userData:
            return "api/get_user_data"
        case .versionCheck:
            return "api/get_version"
        case .updateUserData:
            return "api/save_user_data"
        case .ossConfig:
            return "api/oss/getsign"
        case .uploadImg(let urlPath, _, _):
            return ""
            
            
        case .register:
            return "register"
        case .easyRequset:
            return "4/news/latest"
        case .updateAPi:
            return "versionService.getAppUpdateApi"
        case .uploadHeadImage( _):
            return "/file/user/upload.jhtml"
        }
    }
    
    var method: Moya.Method {
        switch self {
        case .easyRequset,
             .appConfig,
             .lokiData,
             .serverData,
             .versionCheck,
             .userData,
             .ossConfig:
            return .get
        case .login,
             .uploadImg,
             .updateUserData:
            return .post
        default:
            return .post
        }
    }

    //    这个是做单元测试模拟的数据，必须要实现，只在单元测试文件中有作用
    var sampleData: Data {
        return "".data(using: String.Encoding.utf8)!
    }

    //    该条请API求的方式,把参数之类的传进来
    var task: Task {
//        return .requestParameters(parameters: nil, encoding: JSONArrayEncoding.default)
        switch self {
        case .easyRequset,
             .appConfig,
             .userData,
             .versionCheck,
             .serverData,
             .ossConfig:
            return .requestPlain
        case let .lokiData(key):
            return .requestParameters(parameters: ["key": key], encoding: URLEncoding.default)
        case let .login(account, password):
            return .requestParameters(parameters: ["login_name": account, "login_password": password], encoding: JSONEncoding.default)
        case let .updateUserData(nick_name, avatar):
            return .requestParameters(parameters: ["nick_name": nick_name, "avatar": avatar], encoding: JSONEncoding.default)
        case .uploadImg(_, let params, let imageDate):
            let formDataimg = MultipartFormData(provider: .data(imageDate), name: "file",
                                              fileName: "test.png", mimeType: "image/png")
            
            var multipartData = [MultipartFormData]()
            
            for (key, value) in params {
                print("\(key): \(value)")
                let strData = value.data(using: .utf8)
                let formData = MultipartFormData(provider: .data(strData!), name: key)
                multipartData.append(formData)
            }
            multipartData.append(formDataimg)
            
            return .uploadCompositeMultipart(multipartData, urlParameters: [String:Any]())
            
        case let .register(email, password):
            return .requestParameters(parameters: ["email": email, "password": password], encoding: JSONEncoding.default)
        case let .updateAPi(parameters):
            return .requestParameters(parameters: parameters, encoding: URLEncoding.default)
        //图片上传
        case .uploadHeadImage(let parameters, let imageDate):
            ///name 和fileName 看后台怎么说，   mineType根据文件类型上百度查对应的mineType
            let formData = MultipartFormData(provider: .data(imageDate), name: "file",
                                              fileName: "hangge.png", mimeType: "image/png")
            return .uploadCompositeMultipart([formData], urlParameters: parameters)
        }
        //可选参数https://github.com/Moya/Moya/blob/master/docs_CN/Examples/OptionalParameters.md
//        case .users(let limit):
//        var params: [String: Any] = [:]
//        params["limit"] = limit
//        return .requestParameters(parameters: params, encoding: URLEncoding.default)
    }

    var headers: [String : String]? {
        let timestamp = String(Date().milliStamp)
        let appID = PONTUS_APP_ID
        let appKey = PONTUS_APP_KEY
        let appSecret = PONTUS_APP_SECRET
        let appVersion = DimRuntimeData.shared.getAppVersion()
        
        return [
            "Accept": "application/json",
            "appId": appID,
            "timestamp": timestamp,
            "appKey": appKey,
            "X-Auth-Token": DimRuntimeData.shared.getUserToken(),
            "X-App-Version": appVersion,
            "appsystem": "IOS",
            "sign": (appID + timestamp + appSecret).md5
        ]
    }
 
}
