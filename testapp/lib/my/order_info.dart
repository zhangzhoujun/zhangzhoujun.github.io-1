import 'package:flutter/material.dart';
import '../global_config.dart';
import 'order_info_item.dart';

class OrderInfo extends StatelessWidget{

@override
  Widget build(BuildContext context) {

    Widget titleSection = new Container(
      width: MediaQuery.of(context).size.width,
      height: 40.0,
      child : new FlatButton (
        onPressed: (){},
        child: new Container(
          child: new Row(
            children: [
              new Expanded(
                child: new Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    new Text("View All Orders", style: new TextStyle(fontSize: 10.0, color: Color(0xFF333333))),
                  ]
                )
              ),
              new Container(
                child: new Image(
                  width: 10.0,
                  height: 10.0,
                  image: new AssetImage('images/img_more.png',), 
                ),
              ),
            ]
          )
        ),
      ),  
    );

    return new Container(
      color: GlobalConfig.cardBackgroundColor,
      width: MediaQuery.of(context).size.width,
      height: 131.0,
      child: new Column(
        children: <Widget>[
          new Container(
            height: 90.0,
            child: new Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                OrderInfoItem("Awaiting Payment","images/img_car.png"),
                OrderInfoItem("Awaiting Shipment","images/img_fahuo.png"),
                OrderInfoItem("Awaiting Confirmation","images/img_car.png"),
              ],
            ),
          ),
          new Divider(
            color: Color(0xFFE5E5E5),
            height: 1.0,
          ),

          titleSection,
        ],
      ),
 
    );
  }

}