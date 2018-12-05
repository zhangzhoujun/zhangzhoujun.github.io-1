import 'package:flutter/material.dart';
import '../global_config.dart';

class TopBar extends StatelessWidget{

final String name;
final bool isLogin;

TopBar(this.name,this.isLogin);

@override
  Widget build(BuildContext context) {
    return new Container(
      color: GlobalConfig.cardBackgroundColor,
      width: MediaQuery.of(context).size.width,
      height: 166.0,
      child:new Container(
        child: new Stack(
        children: <Widget>[ 
          new Image(
            width: MediaQuery.of(context).size.width,
            height: 166.0,
            image: new AssetImage('images/img_vip_head_bg.png'), 
            fit: BoxFit.fill,
          ),
          new Column(
              children: <Widget>[
                new Container(
                  width: 66.0,
                  height: 66.0,
                  child: new CircleAvatar(
                    backgroundImage: NetworkImage("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3920398476,1501488149&fm=27&gp=0.jpg"),
                    radius: 40.0,
                  ),
                  margin: new EdgeInsets.only(top: 47.0, bottom: 0.0),
                  alignment: Alignment.center,
                ),
                new Stack(
                  children: <Widget>[
                    new Offstage(
                      offstage: isLogin ? false : true,
                      child: new Container(
                        child: new Text(
                          name,
                          style: new TextStyle(fontWeight: FontWeight.bold, fontSize: 16.0, height: 1.3, color: Colors.black)
                        ),
                        margin: new EdgeInsets.only(top: 7.0),
                        alignment: Alignment.center,
                      ),
                    ),

                    new Offstage(
                      offstage: isLogin ? true : false,
                      child: new Container(
                        height: 30.0,
                        child: new OutlineButton(
                          borderSide:new BorderSide(color: Color(0xFFBF8F30)),
                          child: new Text('Login/Sign Up',style: new TextStyle(color: Color(0xFFBF8F30)),),
                          onPressed: (){},
                          highlightColor: Color(0x00000000), 
                        ),
                        margin: new EdgeInsets.only(top: 7.0),
                        alignment: Alignment.center,
                      ),
                    ),
                  ],
                ),
              ],
            ),
        ]
       ),
      ),
    );
  }

}