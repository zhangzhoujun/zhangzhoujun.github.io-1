import 'package:flutter/material.dart';
import '../global_config.dart';

class OrderInfoItem extends StatelessWidget{

  final String title;
  final String iconSource;

  OrderInfoItem(this.title,this.iconSource);

  @override
  Widget build(BuildContext context) {
    return  new Container(
            color: GlobalConfig.cardBackgroundColor,
            height: 90.0,
            width: MediaQuery.of(context).size.width / 3,
            child: new Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                new Container(
                  width: (MediaQuery.of(context).size.width) / 3,
                  child: new FlatButton (
                      onPressed: (){},
                      child: new Container(
                        height: 90.0,
                        child: new Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            new Container(
                              width: 37.0,
                              height: 26.0,
                              child: new Image(image: AssetImage(iconSource),),
                            ),
                            new Container(
                              padding: const EdgeInsets.only(top: 5.0),
                              child: new Text(title, style: new TextStyle(fontSize: 10.0, color: Color(0xFF333333)),),
                            ),
                          ],
                        ),
                      )
                  ),
                ),
              ],
            ),
    );
  }

}