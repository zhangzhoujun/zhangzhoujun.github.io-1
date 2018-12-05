import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'dart:io';

class MyHomeGallery extends StatefulWidget {
  @override
  _MyHomeGalleryState createState() => _MyHomeGalleryState();
}

class _MyHomeGalleryState extends State<MyHomeGallery> {
  Future<File> _imageFile;

  void _selectedImage() {
    setState(() {
      _imageFile = ImagePicker.pickImage(source: ImageSource.gallery);
    });
  }

  Widget _previewImage() {
    return FutureBuilder<File>(
        future: _imageFile,
        builder: (BuildContext context, AsyncSnapshot<File> snapshot) {
          if (snapshot.connectionState == ConnectionState.done &&
              snapshot.data != null) {

            return new Image.file(snapshot.data);
            // return new ClipOval(
            //   child: SizedBox(
            //     width: 70.0,
            //     height: 70.0,
            //     child: Image.file(snapshot.data, fit: BoxFit.cover)
            //   ),
            // );
          } else {
            return Text('No image selected.');
          }
        });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Image Picker Example'),
      ),
      body: Center(
        child: _previewImage(),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _selectedImage,
        tooltip: 'take Image',
        child: Icon(Icons.add_a_photo),
      ),
    );
  }
}