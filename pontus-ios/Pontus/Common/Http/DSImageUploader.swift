////
////  DSImageUploader.swift
////  Pontus
////
////  Created by 张舟俊 on 2021/12/8.
////
//
//import Foundation
//import AliyunOSSiOS
//
//typealias StringBoolCallback = (_ text:String, _ value:Bool) -> Void
//
//class DSImageUploader {
//
//    static let shared = DSImageUploader.init()
//
//    let config = OSSClientConfiguration()
//
//    let credantial = OSSFederationCredentialProvider { () -> OSSFederationToken? in
//
//        let token = OSSFederationToken()
//        token.tAccessKey = DSPublicKeys.OSS_KEY
//        token.tSecretKey = DSPublicKeys.OSS_SECRECT
//        return token
//    }
//
//    let client: OSSClient
//
//    init() {
//        OSSLog.enable()
//        config.maxRetryCount = 2
//        config.timeoutIntervalForRequest = 30
//        config.timeoutIntervalForResource = 120
//        client = OSSClient(endpoint: DSPublicKeys.OSS_END_POINT, credentialProvider:credantial, clientConfiguration:config)
//    }
//}
//
//extension DSImageUploader {
//
//    func uploadImages(_ image:Data, completionHandler:@escaping StringBoolCallback) {
//         let interval = Date().timeIntervalSince1970
//         let put = OSSPutObjectRequest()
//         put.bucketName = "dasyun-public-statics"
//         put.objectKey="IOSYunPark\(interval).jpg"
//         put.uploadingData = image
//         // put.uploadProgress = { bytesSent; totalByteSent; totalByteExpectedToSendin}
//        //            print(bytesSent, totalByteSent, totalByteExpectedToSend)
//     
//
//         let putTask = client.putObject(put)
//
//         putTask.continue({ (task:OSSTask) ->Any?in
//             if task.error == nil{
//                 print("upload object success!")
//                 let str = DSPublicKeys.OSS_END_POINT.components(separatedBy:CharacterSet.init(charactersIn:"//")).last
//                 let url = String(format:"http://%@.%@/%@", put.bucketName, str!, put.objectKey)
//                 print("上传图片成功：", url)
//                 DispatchQueue.main.async{
//                    completionHandler(url, true)
//                 }
//             } else {
//                 let error: NSError = (task.error)! as NSError
//                 print("图片上传错误：",error.description)
//                 completionHandler("", false)
//             }
//             return nil
//             
//        }).waitUntilFinished()
//    }
//}
