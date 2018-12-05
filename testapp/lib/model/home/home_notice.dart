class HomeNotice{

   int     id;
   String  title;
   String  content;
   String  releaseTime;
   String  state;

  HomeNotice(Map<String, dynamic> json){
    if(null != json){
        id = json['id'];
        title = json['title'];
        content = json['content'];
        releaseTime = json['release_time'].toString();
        state = json['state'];
    }
  }
}