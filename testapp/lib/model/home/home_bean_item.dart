import 'home_bean_image.dart';

class HomeBeanItem{

  String name;
  String configType;
  List<HomeBeanImage> pageConfigItems;

  HomeBeanItem(Map<String, dynamic> json){
    name = json['name'];
    configType = json['config_type'];

    List pageMap = json['page_config_items'];
    pageConfigItems = new List();
    pageMap.forEach((item) {
      print('${pageMap.indexOf(item)}: $item');
      HomeBeanImage beanItem = HomeBeanImage(item);
      pageConfigItems.add(beanItem);
    });
  }
}