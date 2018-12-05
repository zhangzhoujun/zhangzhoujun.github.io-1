import 'home_bean_item.dart';
import 'home_shop.dart';
import 'home_notice.dart';
import 'home_detault.dart';

class HomeModel {

  List<HomeBeanItem> pageConfigs;
  HomeBeanShop shop;
  HomeNotice notice;
  HomeDefault defaultContent;

  HomeModel();

  json(Map<String, dynamic> json){
    shop = HomeBeanShop(json['shop']);
    notice = HomeNotice(json['notice']);
    defaultContent = HomeDefault(json['default_content']);
    List pageMap = json['page_configs'];
    pageConfigs = new List();
    pageMap.forEach((item) {
      print('${pageMap.indexOf(item)}: $item');
      HomeBeanItem beanItem = HomeBeanItem(item);
      pageConfigs.add(beanItem);
    });
    print('1111');
  }
}
