import 'package:flutter/material.dart';
import '../global_config.dart';
import '../mvp/presenter/home_presenter.dart';
import '../mvp/presenter/home_presenter_impl.dart';
import '../model/base_model.dart';
import '../model/home/home_model.dart';
import '../model/home/home_bean_item.dart';
import '../utils/common_retry.dart';
import '../utils/common_loading_dialog.dart';
import '../utils//date_util.dart';
import 'dart:async';
import 'home_banner.dart';
import '../common/constant.dart';
import 'package:device_info/device_info.dart';
import 'dart:io';

class HomePage extends StatefulWidget {

  @override
  _HomePageState createState(){
    _HomePageState view = new _HomePageState();
    HomePresenter presenter = new HomePresenterImpl(view);
    presenter.init();
    return view;
  } 
}

class _HomePageState extends State<HomePage>  implements HomeView{
  final GlobalKey<RefreshIndicatorState> _refreshIndicatorKey =
      new GlobalKey<RefreshIndicatorState>();

  HomePresenter _homePresenter;
  HomeModel homeModel;  
  
  bool _isSlideUp = false;

  String _curDate;

  DateTime _curDateTime;

  bool _isShowRetry = false;

  ScrollController _scrollController;

  void _scrollListener() {
    // _computeShowtTitle(_scrollController.offset);

    //滑到最底部刷新
    // if (_scrollController.position.pixels ==
    //     _scrollController.position.maxScrollExtent) {
    //   _loadData();
    // }
    if (_scrollController.position.pixels ==
        _scrollController.position.maxScrollExtent) {
      
    }
    double size = _scrollController.position.pixels;
    print('_scrollController.position.pixels == $size');
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      theme: GlobalConfig.themeData,
      home: new Scaffold(
        body: _buildList(context),
      )
    );
  }

  @override
    void initState() {
      super.initState();
      _scrollController = new ScrollController()..addListener(_scrollListener);
      _refreshData();
      print("开始加载。。。。。。");

      getDeviceInfo();
    }

    /// 请求成功
  Future<dynamic> getDeviceInfo() async {
    DeviceInfoPlugin deviceInfo = DeviceInfoPlugin();
      if(Platform.isIOS){
        IosDeviceInfo iosInfo = await deviceInfo.iosInfo;
        print("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        print('Running on ${iosInfo.utsname.machine}');
        print('Running on ${iosInfo.utsname.nodename}');
        print('Running on ${iosInfo.utsname.release}');
        print('Running on ${iosInfo.utsname.sysname}');
        print('Running on ${iosInfo.utsname.version}');
        print('Running on ${iosInfo.name}');
        print('Running on ${iosInfo.model}');
        print('Running on ${iosInfo.localizedModel}');
        print('Running on ${iosInfo.isPhysicalDevice}');
        print('Running on ${iosInfo.identifierForVendor}');
        print('Running on ${iosInfo.systemName}');
        print('Running on ${iosInfo.systemVersion}');
        print("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        return iosInfo;
      } else if(Platform.isAndroid){
        AndroidDeviceInfo androidInfo = await deviceInfo.androidInfo;
        print("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        print('Running on ${androidInfo.board}');
        print('Running on ${androidInfo.bootloader}');
        print('Running on ${androidInfo.brand}');
        print('Running on ${androidInfo.device}');
        print('Running on ${androidInfo.display}');
        print('Running on ${androidInfo.fingerprint}');
        print('Running on ${androidInfo.hardware}');
        print('Running on ${androidInfo.host}');
        print('Running on ${androidInfo.id}');
        print('Running on ${androidInfo.isPhysicalDevice}');
        print('Running on ${androidInfo.manufacturer}');
        print('Running on ${androidInfo.model}');
        print('Running on ${androidInfo.product}');
        print('Running on ${androidInfo.tags}');
        print('Running on ${androidInfo.type}');
        print('Running on ${androidInfo.version.baseOS}');
        print('Running on ${androidInfo.version.codename}');
        print('Running on ${androidInfo.version.incremental}');
        print('Running on ${androidInfo.version.previewSdkInt}');
        print('Running on ${androidInfo.version.release}');
        print('Running on ${androidInfo.version.sdkInt}');
        print('Running on ${androidInfo.version.securityPatch}');
        print("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
        return androidInfo;
      }
  }

  @override
  void onLoadFail() {
    print("onLoadFail ....");
    setState(() {});
  }

  @override
  void dispose() {
    super.dispose();
    _scrollController.removeListener(_scrollListener);
  }

  @override
  void onLoadSuc(BaseModel model) {
    print("onLoadSuc .... model = $model");

    if(model.success()){
      homeModel = HomeModel();
      homeModel.json(model.data);
      print("============================ $homeModel");
      setState(() {});
    }
  }

  @override
  setPresenter(HomePresenter presenter) {
    _homePresenter = presenter;
  }

  Widget _buildList(BuildContext context) {
    var content;

    if (null == homeModel || homeModel.pageConfigs.isEmpty) {
      if (_isShowRetry) {
        _isShowRetry = false;
        content = CommonRetry.buildRetry(_refreshData);
      } else {
        content = ProgressDialog.buildProgressDialog();
      }
    } else {
      content = new ListView.builder(
        //设置physics属性总是可滚动
        physics: AlwaysScrollableScrollPhysics(),
        controller: _scrollController,
        itemCount: homeModel.pageConfigs.length,
        itemBuilder: _buildItem,
      );
    }

    var _refreshIndicator = new NotificationListener(
      onNotification: _onNotification,
      child: new RefreshIndicator(
        key: _refreshIndicatorKey,
        onRefresh: _refreshData,
        child: content,
      ),
    );

    return _refreshIndicator;
  }

  bool _onNotification<Notification>(Notification n) {
    if (n is ScrollEndNotification) {
      //滑动结束刷新？体验不是很好，待定
      //setState(() {});
    }

    return true;
  }

  //根据type组装数据
  Widget _buildItem(BuildContext context, int index) {
    final HomeBeanItem item = homeModel.pageConfigs[index];

    Widget widget;

    switch (item.configType) {
      case "focus":
        widget = new HomeBanner(item.pageConfigItems, Constant.bannerHeight);
        break;
    }
    return widget;
  }

  Future<Null> _refreshData() {
    _isSlideUp = false;

    final Completer<Null> completer = new Completer<Null>();

    _curDateTime = new DateTime.now();

    _curDate = DateUtil.formatDateSimple(_curDateTime);

    _homePresenter.loadNews(null);

    completer.complete(null);

    return completer.future;
  }

  Future<Null> _loadData() {
    _isSlideUp = true;

    final Completer<Null> completer = new Completer<Null>();

    _curDateTime = _curDateTime.subtract(new Duration(days: 1));

    _curDate = DateUtil.formatDateSimple(_curDateTime);

    _homePresenter.loadNews(_curDate);

    setState(() {});

    completer.complete(null);

    return completer.future;
  }

}