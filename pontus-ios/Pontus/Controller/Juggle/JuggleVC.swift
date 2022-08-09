//
//  JuggleVC.swift
//  Pontus
//
//  Created by 张舟俊 on 2021/11/26.
//

import UIKit
import SwiftyFitsize
import SwiftyJSON

class JuggleVC: BaseController{
    
    private var pageKey: String!
    private var pagetag: String!
    private var isShowHead = false
    private var hasLoginData = false
    private var loginData = JuggleLoginLocalData()
    private var fromLoading = false
    private var modules = [JugglePageRows]()
    private var needRefreshList = [IndexPath : JuggleServerLocalData]()
    private var viewWid = screenWidth
    // dialog类型的时候，需要处理的值
    private var isDialogStyle = false
    private var dialogShowPagetag = ""
    private var dialogBgColor: String?
    private var leftMargint = 0.0
    private var rightmargint = 0.0
    private var topMargint = 0.0
    private var isCanOutDismiss = true
    private var isShowByServerMap = [String : Bool]()
    // 刷新的时候，只有配置的数据发生改变 才会刷新UI
    private var mHomeDadaMD5 = ""
    private var hasUserData = false
    private var userInfoChanged = false

    var style: UIStatusBarStyle = .lightContent
    
    // 构造器
    convenience init(key: String?, showHead: Bool = false, fromLoading: Bool = false, isDialogStyle: Bool = false, dialogShowPagetag: String = "") {
        self.init()
        self.pageKey = key ?? ""
        self.isShowHead = showHead
        self.fromLoading = fromLoading
        
        self.isDialogStyle = isDialogStyle
        self.dialogShowPagetag = dialogShowPagetag
        pagetag = self.pageKey + String(Date().milliStamp) + String(Int(arc4random()))
    }

    // 重写statusBar相关方法
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return self.style
    }

    override func viewWillAppear(_ animated: Bool) {
        if(isShowHead == true){
            super.viewWillAppear(animated)
        } else {
            navigationController?.navigationBar.isHidden = true
        }
    }

    override func viewDidLoad() {
        if(isDialogStyle){
            // self.modalPresentationStyle = UIModalPresentationStyle.fullScreen
        }
        super.viewDidLoad()
        setupLoadData()
        print("add observer")
        NotificationCenter.default.addObserver(self, selector: #selector(onJuggleItemClick), name: NSNotification.Name(rawValue: JUGGLE_ITEM_CLICK), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(onJuggleDialogDismiss), name: NSNotification.Name(rawValue: JUGGLE_DIALOG_DISMISS), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(onUserInfoChanged), name: NSNotification.Name(rawValue: JUGGLE_INFO_CHANGED), object: nil)
    }
    
    @objc func onUserInfoChanged(noti: Notification) {
        print("onUserInfoChanged")
        userInfoChanged = true
    }
    
    override func viewDidAppear(_ animated: Bool) {
        print("ADADADAD viewDidAppear")
        if(userInfoChanged && hasUserData){
            reload()
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        print("ADADADAD viewDidDisappear")
        if(isDialogStyle){
            NotificationCenter.default.post(name: NSNotification.Name(JUGGLE_DIALOG_DISMISS), object: nil, userInfo:  [JUGGLE_PAGE_TAG : dialogShowPagetag])
        }
    }
    
    @objc func onJuggleDialogDismiss(noti: Notification) {
        print("onJuggleDialogDismiss")
        let page_tag = noti.userInfo?[JUGGLE_PAGE_TAG] as? String
        if(page_tag == pagetag && needShowDialog){
            startShowOperateDialog()
        }
    }
    
    @objc func onJuggleItemClick(noti: Notification) {
        print("onJuggleItemClick")
        let page_tag = noti.userInfo?[JUGGLE_PAGE_TAG] as? String
        let clickType = noti.userInfo?[JUGGLE_ITEM_TYPE] as? String
        if(page_tag == pagetag){
            if(clickType == JUGGLE_CLOSE_WEBVIEW){
                print("JUGGLE_CLOSE_WEBVIEW")
                closeController()
            }
            if(clickType == JUGGLE_RELOAD_WEBVIEW){
                print("JUGGLE_RELOAD_WEBVIEW")
                reload()
            }
            if(clickType == JUGGLE_OPEN_DIALOG){
                print("JUGGLE_OPEN_DIALOG")
                let dialogKey = noti.userInfo?[JUGGLE_OPEN_DIALOG_KEY] as? String
                print("dialogKey = " + dialogKey!)
                opeDialogView(dialogKey: dialogKey!)
            }
            if(clickType == JUGGLE_SUBMIT_FROM){
                print("JUGGLE_SUBMIT_FROM")
                if(hasLoginData){
                    doLogin()
                }
            }
            if(clickType == JUGGLE_REFRESH_USER){
                print("JUGGLE_REFRESH_USER")
                getUserInfo(isMust: true)
            }
            if(clickType == JUGGLE_GET_OPEN_URL){
                print("JUGGLE_GET_OPEN_URL")
                let urlPath = noti.userInfo?[JUGGLE_DATA] as? String
                
                NetWorkRequest(API.serverData(urlPath: ("api/" + (urlPath ?? ""))), modelType: [JuggleServerData].self, successCallback: { (config, responseModel) in
                    config.forEach({ (item) in
                        gotoNewWebviewController(url: item.url)
                    })
                }, failureCallback: { (responseModel) in
                    print("getDataByServer dialog failed")
                    self.dialogCountChange(needNext: true)
                    return
                })
            }
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(JUGGLE_ITEM_CLICK), object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(JUGGLE_DIALOG_CLICK), object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(JUGGLE_DIALOG_DISMISS), object: nil)
        NotificationCenter.default.removeObserver(self, name: NSNotification.Name(JUGGLE_INFO_CHANGED), object: nil)
    }
    
    private func getUserInfo(isMust: Bool = false){
        if((isMust == true) || (AppPreferences.shared.isLogined && DimRuntimeData.shared.userInfo == nil)){
            NetWorkRequest(API.userData, modelType: [UserBean].self, successCallback: { (config, responseModel) in
                config.forEach({ (item) in
                    if(item.id == 0){
                        userLogout()
                    } else {
                        DimRuntimeData.shared.userInfo = item
                        AppPreferences.shared.setUserId(userId: String(item.id))
                    }
                })
            }, failureCallback: { (responseModel) in
                print("网络请求失败 包括服务器错误和网络异常\(responseModel.error_code)__\(responseModel.error)")
            })
        }
    }

    private lazy var collectionView: UICollectionView = {
        let lt = UCollectionViewSectionBackgroundLayout()
        lt.minimumInteritemSpacing = 0
        lt.minimumLineSpacing = 0
        let collectionView = UICollectionView(frame: CGRect.zero, collectionViewLayout: lt)
        if(isDialogStyle){
            collectionView.backgroundColor = UIColor.clear
        } else {
            collectionView.backgroundColor = UIColor.background
        }
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.alwaysBounceVertical = true
        collectionView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
        collectionView.scrollIndicatorInsets = collectionView.contentInset
        // 注册cell
        collectionView.register(cellType: JuggleCollectionPicViewCell.self)
        collectionView.register(cellType: JuggleCollectionBannerViewCell.self)
        collectionView.register(cellType: JuggleCollectionGoodViewCell.self)
        collectionView.register(cellType: JuggleCollectionHSViewCell.self)
        collectionView.register(cellType: JuggleCollectionDIYViewCell.self)
        collectionView.register(cellType: JuggleCollectionLoginViewCell.self)
        // 注册头部 尾部
        collectionView.register(supplementaryViewType: JuggleCollectionHeaderView.self, ofKind: UICollectionView.elementKindSectionHeader)
        collectionView.register(supplementaryViewType: JuggleCollectionFooterView.self, ofKind: UICollectionView.elementKindSectionFooter)
        // 刷新控件
        collectionView.uHead = URefreshHeader { [weak self] in self?.setupLoadData() }
        collectionView.uFoot = URefreshDiscoverFooter()
        collectionView.uempty = UEmptyView(verticalOffset: -(collectionView.contentInset.top)) { self.reload() }
        return collectionView
    }()
    
    private func initpageBase(pageBase: JugglePageBase?){
        if(pageBase == nil){
            return
        }
        if(!isDialogStyle){
            collectionView.backgroundColor = getUIColorByColor(color: (pageBase?.bg_color)!)
            if(pageBase?.is_pull_refresh == true){
                collectionView.bounces = true
                collectionView.alwaysBounceVertical = true
            } else {
                collectionView.bounces = false
                collectionView.alwaysBounceVertical = false
            }
    //        if(pageBase?.is_fulls_screen == true){
    //            isShowHead = false
    //        }
            isShowHead = pageBase?.has_head ?? false
            // dialog
            self.dialogs = pageBase?.dialogs
            self.currentShowDialogCount = 0
            startShowOperateDialog()
        } else {
            self.isDialogView(pageBase: pageBase!)
            collectionView.bounces = false
            collectionView.alwaysBounceVertical = false
        }
        setNavigationBar(pageBase: pageBase)
    }
    
    private var dialogs: [JugglePageBaseDialog]?
    private var currentShowDialogCount = 0
    private var needShowDialog = false
    
    private func startShowOperateDialog(){
        if(dialogs == nil || dialogs?.count == 0){
            return
        }
        if(currentShowDialogCount >= dialogs?.count ?? 0){
            return
        }
        needPopOperateDialog(dialog: dialogs![currentShowDialogCount])
    }
    
    private func needPopOperateDialog(dialog: JugglePageBaseDialog){
        if(dialog.type == "interval"){// 设置的评率
            if(dialog.interval_type == "day"){ // 天为单位
                let currentTime = Date().milliStamp
                let lastTime = AppPreferences.shared.getDialogTime(dialogId: dialog.value ?? "")
                let betweenDays = Date().daysBetweenDate(startDate: currentTime, endDate: lastTime)
                if(lastTime == 0 || betweenDays >= dialog.interval_time){
                    AppPreferences.shared.setDialogTime(dialogId: dialog.value ?? "", time: currentTime)
                    dialogToShow(dialogKey: dialog.value!)
                    return
                } else {
                    dialogCountChange(needNext: true)
                    return
                }
            } else if(dialog.interval_type == "hour"){// 小时为单位
                let currentTime = Date().milliStamp
                let lastTime = AppPreferences.shared.getDialogTime(dialogId: dialog.value ?? "")
                let betweenDays = Date().hoursBetweenDate(startDate: currentTime, endDate: lastTime)
                if(lastTime == 0 || betweenDays >= dialog.interval_time){
                    AppPreferences.shared.setDialogTime(dialogId: dialog.value ?? "", time: currentTime)
                    dialogToShow(dialogKey: dialog.value!)
                    return
                } else {
                    dialogCountChange(needNext: true)
                    return
                }
            }
        } else if (dialog.type == "service") { // 请求服务端控制频率
            NetWorkRequest(API.serverData(urlPath: ("api/" + (dialog.server_link ?? ""))), modelType: [JuggleServerData].self, successCallback: { (config, responseModel) in
                config.forEach({ (item) in
                    if(item.is_show == true){
                        self.dialogToShow(dialogKey: dialog.value!)
                        return
                    } else {
                        self.dialogCountChange(needNext: true)
                        return
                    }
                })
            }, failureCallback: { (responseModel) in
                print("getDataByServer dialog failed")
                self.dialogCountChange(needNext: true)
                return
            })
        }
    }
    
    private func dialogCountChange(needNext: Bool){
        currentShowDialogCount += 1
        needShowDialog = currentShowDialogCount < dialogs!.count
        if(needNext && needShowDialog){
            startShowOperateDialog()
        }
    }
    
    private func dialogToShow(dialogKey: String) {
        opeDialogView(dialogKey: dialogKey)
        dialogCountChange(needNext: false)
    }
    
    private func opeDialogView(dialogKey: String){
        let dialogVC = JuggleVC(key: dialogKey, isDialogStyle: true, dialogShowPagetag: pagetag)
        dialogVC.view.backgroundColor = UIColor(red: 0, green: 0, blue: 0, alpha: 0.5)
        dialogVC.modalPresentationStyle = UIModalPresentationStyle.fullScreen
        
        let navVC = NaviController(rootViewController: dialogVC)
        navVC.view.backgroundColor = UIColor(red: 0, green: 0, blue: 0, alpha: 0.5)
        navVC.modalPresentationStyle = UIModalPresentationStyle.custom
        self.present(navVC, animated: false, completion: nil)
    }
    
    private func setNavigationBar(pageBase: JugglePageBase?){
        if(!isDialogStyle && isShowHead){
            configNavigationBar()
            navigationController?.navigationBar.backgroundColor = UIColor.background
            navigationItem.title = pageBase?.title
        } else {
            navigationController?.navigationBar.isHidden = true
        }
    }
    
    override func configNavigationBar() {
        super.configNavigationBar()
        navigationController?.navigationBar.isHidden = false
        navigationController?.barStyle(.white)
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(image: UIImage(named: "nav_reload"),
                                                            target: self,
                                                            action: #selector(reload))
    }
    
    @objc func reload() {
        self.setupLoadData()
    }
    
    private func isDialogView(pageBase: JugglePageBase){
        if(isDialogStyle){
            isCanOutDismiss = pageBase.mask_close
            view.backgroundColor = getUIColorByColor(color: (pageBase.mask_color)!)
            topMargint = Double(pageBase.offset_top) * Double(screenHeight) / 100
            leftMargint = Double(pageBase.percentage[0]) * Double(screenWidth) / 100
            rightmargint = Double((100 - pageBase.percentage[1])) * Double(screenWidth) / 100
            
            viewWid = screenWidth - leftMargint - rightmargint
            initCollectionViewSnp()
        }
    }
    
    private func initDataBack(item: JuggleMain){
        if(item.page_base?.server_links != nil && (item.page_base?.server_links?.count ?? 0) > 0 && isShowByServerMap.count == 0){
            getItemShowByServer(item: item)
            return
        }
        if(mHomeDadaMD5 == ""){
            mHomeDadaMD5 = JSON(item).stringValue.md5
            print("第一次获取数据 mHomeDadaMD5 = " + mHomeDadaMD5)
        } else {
            if((item.page_base?.no_cache == false) && (mHomeDadaMD5 == (JSON(item).stringValue.md5))){
                print("刷新数据一样，不用更新UI")
                return
            }
        }
        
        self.initpageBase(pageBase: item.page_base)
        
        self.needRefreshList.removeAll()
        self.hasLoginData = false
        self.loginData = JuggleLoginLocalData()
    
        self.modules = self.filterData(list: item.rows ?? [])
        
        self.collectionView.reloadData()
    }
    
    private func getItemShowByServer(item: JuggleMain) {
        isShowByServerMap.removeAll()
        
        let count = item.page_base?.server_links?.count ?? 0
        for i in 0..<count {
            let path = item.page_base?.server_links![i] ?? ""
            NetWorkRequest(API.serverData(urlPath: ("api/" + path)), modelType: [JuggleServerData].self, successCallback: { (config, responseModel) in
                config.forEach({ (bean) in
                    if(bean.is_show == true){
                        self.isShowByServerMap[path] = true
                    } else {
                        self.isShowByServerMap[path] = false
                    }
                    self.getItemShowByServerBack(item: item)
                })
            }, failureCallback: { (responseModel) in
                print("getPagelinkServer failed")
                self.isShowByServerMap[path] = false
                self.getItemShowByServerBack(item: item)
            })
        }
    }
    
    private func getItemShowByServerBack(item: JuggleMain) {
        if (isShowByServerMap.count == item.page_base?.server_links?.count) {
            initDataBack(item: item)
        }
    }

    private func setupLoadData() {
        NetWorkRequest(API.lokiData(key: pageKey), modelType: [JuggleMain].self, successCallback: { (config, responseModel) in
            config.forEach({ (item) in
                self.initDataBack(item: item)
                self.collectionView.uHead.endRefreshing()
                self.collectionView.uempty?.allowShow = false
            })
        }, failureCallback: { (responseModel) in
            print("网络请求失败 包括服务器错误和网络异常\(responseModel.error_code)__\(responseModel.error)")
            self.collectionView.uHead.endRefreshing()
            if(self.modules.count == 0){
                self.collectionView.uempty?.allowShow = true
            } else {
                self.collectionView.uempty?.allowShow = false
            }
        })
    }
    
    private func filterData(list: [JugglePageRows]) -> [JugglePageRows]{
        let size = list.count - 1
        if(size < 0){
            return list
        }
        
        for i in stride(from:size,through:0,by: -1) {
            let row = list[i]
            row.pagetag = pagetag
            // 自定义组建本地标识
            if(row.is_diy_components == true){
                row.model_type = "-999"
            }
            // 过滤时间
            let dataCountTime = row.data?.count ?? 0
            if(dataCountTime > 0){
                for k in stride(from:(dataCountTime - 1),through:0,by: -1) {
                    let item = (row.data?[k])!
                    item.pagetag = pagetag
                    if(isNeedDeleteTime(item: item) == true){
                        row.data?.remove(at: k)
                    }
                }
            }
            // 黑白名单过滤 -> 身份级别
            let dataCount = row.data?.count ?? 0
            if(dataCount > 0){
                for k in stride(from:(dataCount - 1),through:0,by: -1) {
                    let item = (row.data?[k])!
                    if(isNeedDeleteLevel(whiteLevel: item.white_level, blackLevel: item.black_level) == true){
                        row.data?.remove(at: k)
                    }
                }
            }
            // 黑白名单过滤 -> 单个用户级别
            let dataCountEx = row.data?.count ?? 0
            if(dataCountEx > 0){
                for k in stride(from:(dataCountEx - 1),through:0,by: -1) {
                    let item = (row.data?[k])!
                    if(isNeedDeleteUsers(whiteUsers: item.white_users ?? "", blackUsers: item.black_users ?? "") == true){
                        row.data?.remove(at: k)
                    }
                }
            }
            // 过滤服务端控制返回的数据
            let dataCountServer = row.data?.count ?? 0
            if(dataCountServer > 0){
                for k in stride(from:(dataCountServer - 1),through:0,by: -1) {
                    let item = (row.data?[k])!
                    if(item.is_show_to_server_link != nil && item.is_show_to_server_link != "" && isShowByServerMap[item.is_show_to_server_link!] == false){
                        row.data?.remove(at: k)
                    }
                }
            }
        }
        
        return list
    }
    
    private func getDIYData(key: String, indexPath: IndexPath){
        NetWorkRequest(API.lokiData(key: key), modelType: [JuggleMain].self, successCallback: { (config, responseModel) in
            config.forEach({ (item) in
                
                if(self.needRefreshList.count == 0 || !self.needRefreshList.has(indexPath) || self.needRefreshList[indexPath] == nil){
                    let localData = JuggleServerLocalData()
                    localData.jugglemain = item
                    self.needRefreshList[indexPath] = localData
                }
                
                let count = self.modules.count
                
                for i in 0..<count {
                    let module = self.modules[i]
                    if(module.is_diy_components == true && module.key == key){
                        let dataCount = item.page_base?.server_links?.count ?? 0
                        self.needRefreshList[indexPath]?.dataCount = dataCount
                        if(dataCount == 0){
                            self.collectionView.reloadItems(at: [indexPath])
                        } else {
                            for j in 0..<dataCount {
                                self.getDataByServer(pos: j,key: key,indexPath: indexPath,path: (item.page_base?.server_links![j])!)
                            }
                        }
                    }
                }
            })
        }, failureCallback: { (responseModel) in
            print("getDIYData failed")
        })
    }
    
    private func getDataByServer(pos: Int, key: String, indexPath: IndexPath, path: String){
        if(path == "/get_user_data"){
            hasUserData = true
        }
        NetWorkRequest(API.serverData(urlPath: ("api/" + path)), modelType: [JuggleServerData].self, successCallback: { (config, responseModel) in
            let data = (responseModel?.data)!.data(using: String.Encoding.utf8, allowLossyConversion: false) ?? Data()
            
            self.needRefreshList[indexPath]?.dataList[path] = data
            self.needRefreshList[indexPath]?.dataAddCount += 1
            
            if(self.needRefreshList[indexPath]?.dataAddCount == self.needRefreshList[indexPath]?.dataCount){
                self.collectionView.reloadItems(at: [indexPath])
            }
        }, failureCallback: { (responseModel) in
            print("getDataByServer failed")
        })
    }

    // 继承了父类
    override func setupLayout() {
        view.addSubview(collectionView)
        collectionView.snp.makeConstraints{ make in
            make.top.equalToSuperview().offset(topMargint)
            make.bottom.equalToSuperview()
            make.left.equalToSuperview().offset(leftMargint)
            make.right.equalToSuperview().offset(rightmargint)
        }
        
        if(isDialogStyle){
            let tapGestureCopus = UITapGestureRecognizer.init(target: self, action: #selector(didTapRootView(_:)))
            view.isUserInteractionEnabled = true
            view.addGestureRecognizer(tapGestureCopus)
        }
    }
    
    @objc private func didTapRootView(_ sender: UITapGestureRecognizer) {
        print("view 被点击")
        closeController()
    }
    
    private func closeController(){
        if(isDialogStyle){
            if(isCanOutDismiss){
                self.dismiss(animated: false, completion: nil)
            }
        } else {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    private func initCollectionViewSnp(){
        collectionView.snp.updateConstraints{ make in
            make.top.equalToSuperview().offset(topMargint)
            make.bottom.equalToSuperview()
            make.left.equalToSuperview()
            make.right.equalToSuperview()
        }
    }
    
    private func doLogin(){
        print("登录登录登录")
        let account = loginData.account
        let password = loginData.password
        let accountHint = loginData.accountHint
        let passwordHint = loginData.passwordHint
        let isAgree = loginData.isAgree
        if(!isAgree){
            DToast.showToastActionBottom(message: "请先同意隐私政策与用户协议")
            return
        }
        if(account.isEmpty){
            DToast.showToastActionBottom(message: accountHint + "不能为空")
            return
        }
        if(password.isEmpty){
            DToast.showToastActionBottom(message: passwordHint + "不能为空")
            return
        }
        NetWorkRequest(API.login(account: account, password: password), modelType: [UserBean].self, successCallback: { (config, responseModel) in
            config.forEach({ (item) in
                DimRuntimeData.shared.userInfo = item
                AppPreferences.shared.setUserToken(token: item.token ?? "")
                AppPreferences.shared.setUserId(userId: String(item.id))
                if(self.fromLoading){
                    OnBoarding.shared.processNextState()
                }
            })
        }, failureCallback: { (responseModel) in
            print("网络请求失败 包括服务器错误和网络异常\(responseModel.error_code)__\(responseModel.error)")
        })
        
    }
}

extension JuggleVC: UCollectionViewSectionBackgroundLayoutDelegateLayout, UICollectionViewDataSource{

    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return modules.count
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, backgroundColorForSectionAt section: Int) -> UIColor {
        if(isDialogStyle){
            return UIColor.clear
        } else {
            return UIColor.background
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
    }

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        let model = modules[section]
        switch model.model_type{
        case JuggleType.BANNER.rawValue,
             JuggleType.BANNER_EX.rawValue,
             JuggleType.LOGIN_DATA.rawValue,
             JuggleType.H_SCROLL.rawValue:
            return 1
        default:
            return model.data?.count ?? 1
        }
    }

    // 头尾视图
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        if kind == UICollectionView.elementKindSectionHeader {
            let headerView = collectionView.dequeueReusableSupplementaryView(ofKind: UICollectionView.elementKindSectionHeader, for: indexPath, viewType: JuggleCollectionHeaderView.self)
            return headerView
        } else {
            let footerView = collectionView.dequeueReusableSupplementaryView(ofKind: UICollectionView.elementKindSectionFooter, for: indexPath, viewType: JuggleCollectionFooterView.self)
            return footerView
        }
    }

    // 头部高度
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        return CGSize(width: viewWid, height: 0)
    }

    // 尾部高度
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForFooterInSection section: Int) -> CGSize {
        return modules.count - 1 != section ? CGSize(width: viewWid, height: 0) : CGSize.zero
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let model = modules[indexPath.section]
        switch model.model_type{
        case JuggleType.LINE_ONE.rawValue,
            JuggleType.LINE_CROP.rawValue,
            JuggleType.LINE_CROP_DIY.rawValue:
            let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: JuggleCollectionPicViewCell.self)
            cell.model = (model.data?[indexPath.row])
            return cell
        case JuggleType.BANNER.rawValue,
            JuggleType.BANNER_EX.rawValue:
            let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: JuggleCollectionBannerViewCell.self)
            cell.model = (model)
            return cell
        case JuggleType.DATA_SKIN.rawValue:
            let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: JuggleCollectionGoodViewCell.self)
            cell.model = (model.data?[indexPath.row])
            if(model.skin == "2"){
                cell.cellStyle = .grid
            } else {
                cell.cellStyle = .line
            }
            return cell
        case JuggleType.H_SCROLL.rawValue:
            let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: JuggleCollectionHSViewCell.self)
            cell.model = (model)
            return cell
        case JuggleType.DATA_DIY.rawValue:
            if(!self.needRefreshList.has(indexPath) || self.needRefreshList[indexPath] == nil){
                self.needRefreshList[indexPath] = nil
                self.getDIYData(key: (model.key)!, indexPath: indexPath)
            }
            let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: JuggleCollectionDIYViewCell.self)
            if(needRefreshList.has(indexPath) && needRefreshList[indexPath] != nil){
                let itemData = needRefreshList[indexPath]
                cell.model = (itemData)
            }
            return cell
        case JuggleType.LOGIN_DATA.rawValue:
            hasLoginData = true
            let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: JuggleCollectionLoginViewCell.self)
            cell.model = (model)
            
            cell.loginDataChange = {
                print("loginDataChange")
                self.loginData = $0 as JuggleLoginLocalData
            }
            return cell
        default:
            let cell = collectionView.dequeueReusableCell(for: indexPath, cellType: JuggleCollectionPicViewCell.self)
            cell.model = (model.data?[indexPath.row])
            return cell
        }
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let model = modules[indexPath.section]
        switch model.model_type{
        case JuggleType.LINE_ONE.rawValue,
             JuggleType.BANNER.rawValue,
             JuggleType.BANNER_EX.rawValue:
            let widHei = String((model.data?[indexPath.row].width_height)!)
            let array: Array = widHei.split(separator: "*")
            let width = floor(Double(viewWid))
            let hei = width * Double(array[1])! / Double(array[0])!
            
            return CGSize(width: width, height: hei)
        case JuggleType.LINE_CROP.rawValue:
            let widHei = String((model.data?[indexPath.row].width_height)!)
            let array: Array = widHei.split(separator: "*")
            let width = floor(Double(viewWid) / Double((model.data?.count ?? 1)))
            let hei = width * Double(array[1])! / Double(array[0])!
            
            return CGSize(width: width, height: hei)
        case JuggleType.LINE_CROP_DIY.rawValue:
            let widHei = String((model.data?[indexPath.row].width_height)!)
            let array: Array = widHei.split(separator: "*")
            let width = floor(Double(viewWid) * ((model.data?[indexPath.row].item_width) ?? 50) / 100)
            let hei = width * Double(array[1])! / Double(array[0])!
            
            return CGSize(width: width, height: hei)
        case JuggleType.DATA_SKIN.rawValue:
            let width = floor(Double(viewWid))
            if(model.skin == "2"){
                return CGSize(width: ((width / 2) - 5~), height: 330~)
            } else {
                return CGSize(width: width, height: 160~)
            }
        case JuggleType.H_SCROLL.rawValue:
            let widHei = String((model.data?[indexPath.row].width_height)!)
            let array: Array = widHei.split(separator: "*")
            let width = floor(Double(viewWid) * Double(model.item_width) / 100)
            let hei = width * Double(array[1])! / Double(array[0])!
            return CGSize(width: viewWid, height: hei)
        case JuggleType.DATA_DIY.rawValue:
            if(needRefreshList.count == 0 || !needRefreshList.has(indexPath) || needRefreshList[indexPath] == nil){
                return CGSize(width: viewWid, height: 1)
            }
            let itemData = needRefreshList[indexPath]
            let widHei = String((itemData?.jugglemain?.page_base?.width_height)!)
            let array: Array = widHei.split(separator: "*")
            let width = floor(Double(viewWid))
            let hei = width * Double(array[1])! / Double(array[0])!
            return CGSize(width: viewWid, height: hei)
        case JuggleType.LOGIN_DATA.rawValue:
            return CGSize(width: viewWid, height: 300~)
        default:
            let width = floor(Double(viewWid - 30.0) / 3.0)
            return CGSize(width: width, height: width * 1.75)
        }
    }

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let model = modules[indexPath.section]
        if(model.model_type == JuggleType.DATA_SKIN.rawValue){
            let itemData = model.data?[indexPath.row]
            gotoNewWebviewController(url: itemData?.link)
        }
    }

    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if scrollView.contentOffset.y >= -200 {
            self.style = .default
        } else {
            self.style = .lightContent
        }
        setNeedsStatusBarAppearanceUpdate()
    }

    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        if scrollView == collectionView {
            UIView.animate(withDuration: 0.5, animations: {

            })
        }
    }

    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if scrollView == collectionView {
            UIView.animate(withDuration: 0.5, animations: {

            })
        }
    }
}
