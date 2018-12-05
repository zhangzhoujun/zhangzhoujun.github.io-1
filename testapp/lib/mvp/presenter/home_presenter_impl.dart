import '../presenter/home_presenter.dart';
import '../repository/home_repository.dart';
import '../repository/home_repository_impl.dart';

class HomePresenterImpl implements HomePresenter {

  HomeView _view;

  HomeRepository _repository;

  HomePresenterImpl(this._view) {
    _view.setPresenter(this);
  }

  @override
  void loadNews(String date) {
    assert(_view != null);
    _repository.loadNews(date).then((data) {
      _view.onLoadSuc(data);
    }).catchError((error) {
      _view.onLoadFail();
    });
  }

  @override
  init() {
    _repository = new HomeRepositoryImpl();
  }
}