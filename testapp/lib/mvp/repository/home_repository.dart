import 'dart:async';
import '../../model/base_model.dart';

abstract class HomeRepository {

  Future<BaseModel> loadNews(String data);

}
