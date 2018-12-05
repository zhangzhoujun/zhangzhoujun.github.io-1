import 'package:dio/dio.dart';
import 'dart:async';
import '../model/base_model.dart';

class DioInstance {
  static Dio _dio;

  static DioInstance _instance;

  static Options _options;

  static DioInstance getInstance() {
    if (_instance == null) {
      _instance = new DioInstance._();
      _instance._init();
    }
    return _instance;
  }

  DioInstance._();

  /// 初始化
  _init(){
    // 请求头设置
    FormData formData = new FormData.from({
      "X-App-Token": "a8c53e1b39e038d0ce2995652e55899f05c971051d7fd609",
      "X-Auth-Token": "",
      "X-Session-Id": "xxxx",
      "X-App-Type": "aphrodite_android",
      "X-Locale": "en",
      "X-Version": "1.0.0",
      "X-Shop-Token": "",
      "Content-Type": "application/json",
    });
    // 请求基础配置 
    _options = new Options(
      baseUrl: 'http://www.wsmall.com.my/service/api/', //服务器
      connectTimeout: 15000, //15s
      receiveTimeout: 5000, //5s
      headers: formData,
    );
    _dio = new Dio(_options);
  }

  /// 获取dio对象
  getDio() {
    return _dio;
  }

  /// 请求成功
  Future<dynamic> onResponseSuccess(Response response) async {
    int code = response.data["biz_action"];
    String msg = response.data["biz_msg"];
    int status = response.data["return_status"];
    String serverTime = response.data["server_time"].toString();
    Map data = response.data["data"];
    print('====================================');
    print('code : $code');
    print('msg : $msg');
    print('status : $status');
    print('serverTime : $serverTime');
    print('data : $data');
    print('====================================');

    BaseModel baseModel = BaseModel();
    baseModel.json(response.data);
    return baseModel;
  }

  /// 请求失败
  Future<dynamic> onResponseError(DioError e) async{
    BaseModel baseModel = BaseModel();
    baseModel.returnStatus = e.response.statusCode;
    baseModel.bizAction = 1;
    baseModel.bizMsg = e.message;
    if(e.response.statusCode == 401) {
      print('HTTP ---- 401 啦啦啦啦啦  需要重新登录了');
    } else{
      // Something happened in setting up or sending the request that triggered an Error  
      print(e.message);
    }  
    return baseModel;
  }

  /// get请求
  Future<dynamic> getMethod(String path ,{dynamic data}) async {
    print('fetch: get=$path, data=$data');
    try {
      Response response = await _dio.get(path, data: data);
      return await onResponseSuccess(response);
    } on DioError catch(e) {
      return await onResponseError(e);
    }
  }

  /// post请求
  Future<dynamic> postMethod(String path, {dynamic data}) async {
    print('fetch: post=$path, data=$data');
    try {
      Response response = await _dio.post(path, data: data);
      return await onResponseSuccess(response);
    } on DioError catch(e) {
      return await onResponseError(e);
    }
  }
}

/// 测试是否是单例
void main() {
  print(DioInstance.getInstance().getDio()  == DioInstance.getInstance().getDio());
}