import '../mvp.dart';
import '../../model/base_model.dart';

abstract class HomePresenter implements IPresenter{
  loadNews(String date);
}


abstract class HomeView implements IView<HomePresenter>{

  void onLoadSuc(BaseModel model);
  void onLoadFail();

}