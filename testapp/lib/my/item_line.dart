import 'package:flutter/material.dart';
import '../global_config.dart';

class ItemLine extends StatelessWidget{

final String name;
final String des;
final int type;

ItemLine(this.name,this.des,this.type);

@override
  Widget build(BuildContext context) {

    Widget line = new Container(
      width: MediaQuery.of(context).size.width,
      height: 60.0,
      child : new FlatButton (
        onPressed: (){},
        child: new Container(
          child: new Row(
            children: [
              new Expanded(
                child: new Text(name, style: new TextStyle(fontSize: 16.0, color: Color(0xFF333333))),
              ),
              new Container(
                child: new Row(
                  mainAxisAlignment: MainAxisAlignment.spaceAround,
                  children: [
                    new Container(
                      padding: const EdgeInsets.only(right: 5.0),
                      child: new Text(
                        des, 
                        style: new TextStyle(fontSize: 12.0, color: Color(0xFF666666)),
                      ),
                    ),
                    new Image(
                      width: 10.0,
                      height: 10.0,
                      image: new AssetImage('images/img_more.png',), 
                    ),
                  ]
                )
              ),
            ]
          )
        ),
      ),  
    );

    return new Container(
      color: GlobalConfig.cardBackgroundColor,
      width: MediaQuery.of(context).size.width,
      height: 60.0,
      child: new Column(
        children: <Widget>[
          line,
        ],
      ),
    );
  }

}