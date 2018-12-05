class HomeBeanShop{

   int     id;
   String  token;
   String  shopName;
   int     shopType;
   String  topImg;
   String  mobile;
   String  wechatId;
   String  facebookId;
   String  sImId;

  HomeBeanShop(Map<String, dynamic> json){
        id = json['id'];
        token = json['token'];
        shopName = json['shop_name'];
        shopType = json['shop_type'];
        topImg = json['top_img'];
        mobile = json['mobile'];
        wechatId = json['wechat_id'];
        facebookId = json['facebook_id'];
        sImId = json['s_im_id'];
  }
}