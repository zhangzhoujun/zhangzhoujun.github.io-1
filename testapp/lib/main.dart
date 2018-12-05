import 'package:flutter/material.dart';
import 'index/index.dart';
import 'net/dio_instance.dart';

void main() => runApp(new App());

class App extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    DioInstance.getInstance();

    return new MaterialApp(
      title: "测试AAA",
      home: new Index(),
    );
  }

}
