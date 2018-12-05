import 'package:flutter/material.dart';
import '../global_config.dart';

class BeVip extends StatelessWidget{

@override
  Widget build(BuildContext context) {
    return new Container(
      color: GlobalConfig.cardBackgroundColor,
      width: MediaQuery.of(context).size.width,
      height: 70.0,
        child: new FlatButton(
          onPressed: (){},
          child: new Container(
            child: new ListTile(
              contentPadding: const EdgeInsets.only(left: 0.0,right: 0.0),
              leading: new Container(
                width: 20.0,
                height: 20.0,
                child: new Image(
                  image: new AssetImage('images/vip_icon.png',), 
                ),
              ),
              title: new Container(
                child: new Text(
                  "VIP Center",
                  style: new TextStyle(fontWeight: FontWeight.normal, fontSize: 16.0, height: 1.3, color: Color(0XFFBF8F30))
                  ),
              ),
              subtitle: new Container(
                child: new Text(
                  "Upgrade to VIP, earn shopping and rebate",
                  style: new TextStyle(fontWeight: FontWeight.normal, fontSize: 12.0, height: 1.3, color: Color(0XFF666666))
                  ),
              ),
              trailing: new Container(
                width: 8.0,
                height: 8.0,
                child: new Image(
                  image: new AssetImage('images/img_more.png',), 
                ),
              ),
            ),
          )
        ),
    );
  }

}