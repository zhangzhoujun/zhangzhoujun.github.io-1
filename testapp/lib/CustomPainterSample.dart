import 'package:flutter/material.dart';


class CustomPainterSample extends CustomPainter {

  double progress;

  CustomPainterSample({this.progress: 0.0});

  @override
  void paint(Canvas canvas, Size size) {
    Paint p = new Paint();
    p.color = Colors.green;
    p.isAntiAlias = true;
    p.style = PaintingStyle.fill;
    canvas.drawCircle(size.center(const Offset(0.0, 0.0)), size.width / 2 * progress, p);
  }

  @override
  bool shouldRepaint(CustomPainter oldDelegate) {
    return true;
  }

}