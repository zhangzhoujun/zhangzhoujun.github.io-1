import 'package:flutter/material.dart';
import 'package:testapp/CustomPainterSample.dart';

class Test1 extends StatefulWidget {

 @override
  State<Test1> createState() => new _Test1State();
}

class _Test1State extends State<Test1> with TickerProviderStateMixin{
 double progress = 0.0;

 @override
  void initState() {
    AnimationController ac = new AnimationController(
       vsync: this, 
       duration: const Duration(milliseconds: 4000)
    );
    ac.addListener(() {
      this.setState(() {
        this.progress = ac.value;
      });
    });
    ac.forward();
  }

  @override
  Widget build(BuildContext context) {
    return new Container(
      color: Colors.white,
      child: new CustomPaint(
        painter: new CustomPainterSample(progress: this.progress),
      )
    );
  }

}