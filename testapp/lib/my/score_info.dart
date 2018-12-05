import 'package:flutter/material.dart';
import '../global_config.dart';

class ScoreInfo extends StatelessWidget{

@override
  Widget build(BuildContext context) {
    return new Container(
      color: GlobalConfig.cardBackgroundColor,
      width: MediaQuery.of(context).size.width,
      height: 75.0,
      margin: const EdgeInsets.only(top: 10.0),
      child: new Column(
        children: <Widget>[
          
          new Container(
            child: new Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                new Container(
                  width: (MediaQuery.of(context).size.width) / 2,
                  child: new FlatButton (
                      onPressed: (){},
                      child: new Container(
                        height: 75.0,
                        child: new Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            new Container(
                              child: new Text("12", style: new TextStyle(fontSize: 16.0, color: Color(0xFF333333))),
                            ),
                            new Container(
                              child: new Text("Wish List", style: new TextStyle(fontSize: 12.0, color: Color(0xFF65696F)),),
                            ),
                          ],
                        ),
                      )
                  ),
                ),
                new Container(
                  width: (MediaQuery.of(context).size.width) / 2,
                  child: new FlatButton(
                      onPressed: (){},
                      child: new Container(
                        height: 75.0,
                        child: new Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            new Container(
                              child: new Text("3208", style: new TextStyle(fontSize: 16.0, color: Color(0xFF333333))),
                            ),
                            new Container(
                              child: new Text("Shopping Score", style: new TextStyle(fontSize: 12.0, color: Color(0xFF65696F)),),
                            )
                          ],
                        ),
                      )
                  ),
                ),
              ],
            ),
          )
        ],
      ),

    );
  }

}