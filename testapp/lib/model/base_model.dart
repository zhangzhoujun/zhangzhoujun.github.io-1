class BaseModel{

  int bizAction;
  String bizMsg;
  int returnStatus;
  String serverTime;
  Map data;

  BaseModel(){}

  json(Map<String, dynamic> json){
    bizAction = json['biz_action'];
    bizMsg = json['biz_msg'];
    returnStatus = json['return_status'];
    serverTime = json['server_time'].toString();
    data = json['data'];
  }

  /// 请求返回biz_action 为0的时候是成功的
  bool success(){
    return bizAction == 0;
  }
}
