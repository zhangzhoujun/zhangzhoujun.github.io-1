class HomeBeanImage{

  String imgUrl;
  String linkUrl;
  int    height;
  int    width;

  HomeBeanImage(Map<String, dynamic> json){
    imgUrl = json['img_url'];
    linkUrl = json['link_url'];
    height = json['height'];
    width = json['width'];
  }

}