import '../repository/home_repository.dart';
import 'dart:async';
import '../../net/dio_instance.dart';
import '../../model/base_model.dart';

class HomeRepositoryImpl implements HomeRepository {
  @override
    Future<BaseModel> loadNews(String date) {
      print("22222222222");
    return _getNews(date);
  }
}

Future<BaseModel> _getNews(String date) async {
  print("333");
  String url = "http://www.wsmall.com.my/service/api/home";
  print("url = " + url);
  BaseModel model = await DioInstance.getInstance().getMethod(url);
  print("============================ $model");

  return model;
}
