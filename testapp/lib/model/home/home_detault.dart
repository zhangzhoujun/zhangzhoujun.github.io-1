class HomeDefault{

   String  show;
   String  search;

  HomeDefault(Map<String, dynamic> json){
        show = json['show'];
        search = json['search'];
  }
}