import 'package:flutter/material.dart';
import '../global_config.dart';
import 'top_bar.dart';
import 'be_vip.dart';
import 'score_info.dart';
import 'order_info.dart';
import 'item_line.dart';

class MyPage extends StatefulWidget {

  @override
  _MyPageState createState() => new _MyPageState();

}

class _MyPageState extends State<MyPage> {
  String name = "我的名字";
  bool isLogin = true;
  
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      theme: GlobalConfig.themeData,
      home: new Scaffold(
        body: new SingleChildScrollView(
          child: new Container(
            child: new Column(
              children: <Widget>[
                TopBar(name,isLogin),
                BeVip(),
                ScoreInfo(),
                OrderInfo(),

                new Column(
                  children: <Widget>[
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    Divider(height: 10.0,color: Color(0xFFF5F5F5),),
                    ItemLine("Personal Data","",1),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    Divider(height: 10.0,color: Color(0xFFF5F5F5),),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    ItemLine("To Be a Retailer","",1),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    ItemLine("Shipping Address","",1),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    ItemLine("Contact Seller","",1),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    ItemLine("Feedback","",1),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    Divider(height: 10.0,color: Color(0xFFF5F5F5),),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    ItemLine("Seetings","language",1),
                    Divider(height: 1.0,color: Color(0xFFE5E5E5),),
                    Divider(height: 10.0,color: Color(0xFFF5F5F5),),
                  ],
                ),
                

                new FlatButton(
                  onPressed: (){
                    name = "wode hgudyakfsh";
                    setState(() {});
                    },
                  child: Text("改变"),
                ),

                new FlatButton(
                  onPressed: (){
                    name = "";
                    isLogin = false;
                    setState(() {});
                    },
                  child: Text("退出"),
                ),

                new FlatButton(
                  onPressed: (){
                    name = "我的名字";
                    isLogin = true;
                    setState(() {});
                    },
                  child: Text("登录"),
                ),
                // myServiceCard(),
                // settingCard(),
                // videoCard(),
                // ideaCard()
              ],
            ),
          ),
        )
      ),
    );
  }
}