# APP使用模块化搭建
# 具体直接看代码，或联系我 QQ：271756926

## APP层级
* APP壳层
* 各module业务层
* 基础库

![APP层级](https://zzjtest.oss-cn-hangzhou.aliyuncs.com/11.png "APP层级")


## 项目所引用三方库及基础配置在config.gradle文件中，各module需要引用该文件及配置ARouter框架
## 项目gradle.properties中增加是否是模块化编译参数
`# 是否是模块化编译
isBuildModule=false`

### 各module需要增加配置
```
apply from: "../module.build.gradle"
... 
android {
    defaultConfig {
        //如果是独立模块，则使用当前组件的包名
        if (isBuildModule.toBoolean()) {
            applicationId "com.jy.xxx"
        }
    }
    //统一资源前缀，规范资源引用
    resourcePrefix "xxx_"
}
dependencies {
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    //组件依赖基础库
    api project(':baselib')
    //组件中依赖阿里路由编译框架
    kapt "com.alibaba:arouter-compiler:1.2.2"
    api ('com.alibaba.android:vlayout:1.2.8@aar') {
        transitive = true
    }
    ...
}
```

## 路由框架
### APP使用ARouter框架，作为路由跳转，目前实现的路由如下：

#### 通用host
● arouter://qm.valuechain.com
#### 主页面（页面点击跳转至主页面的配置方法）

● /main/index
● 参数：choseKey   home：首页 ks:快手  tokenCenter:通证中心  school:商学院  mine:我的(个人中心)
● 例如：arouter://qm.valuechain.com/main/index?choseKey=home
#### 快手 一级页面
● arouter://qm.valuechain.com/ValueChain/ks

#### 通用配置显示页面（一级页面）

● /JuggleFra/index
● 参数：页面地址 page_key=？
● 例如：arouter://qm.valuechain.com/JuggleFra/index?page_key=1603970996444_994378592
#### 通用配置显示页面（二级页面）

● /Juggle/index
● 参数：页面地址 page_key=？
● 例如：arouter://qm.valuechain.com/Juggle/index?page_key=1603970996444_994378592
#### 弹窗页面

● /Juggle/dialog
● 参数：页面地址 page_key=？
● 例如：arouter://qm.valuechain.com/Juggle/dialog?page_key=1603970996444_994378592
#### Webview 二级页面

● /webact/index
● 参数：url ：h5地址
● 参数：title：默认显示的title
● 例如：arouter://qm.valuechain.com/webact/index?url=xxx&title=xxx
#### Webview 一级页面

● /webFragment/index
● 参数：url ：h5地址
● 参数：title：默认显示的title
● 例如：arouter://qm.valuechain.com/webFragment/index?url=xxx&title=xxx
#### 二维码扫描页

● /Juggle/qr

#### 视屏播放页

● /Juggle/player
● 参数：视屏地址 video_url=？
● 例如：arouter://qm.valuechain/Juggle/player?video_url=http://alvideo.ippzone.com/zyvd/98/90/b753-55fe-11e9-b0d8-00163e0c0248
#### 分享页面

● /umshare/main
● 例如：arouter://qm.valuechain.com/umshare/main

#### 查看大图（支持多张）

● /Juggle/bigpic
● 参数：bigPic 多张图片用“,”分割
● position：当前显示的位置，默认从0开始
● 例如：arouter://qm.valuechain.com/Juggle/bigpic?position=0&bigPic=A,B,C,D

#### 意见反馈页面（阿里的）

● /commin/feedback
● 例如：arouter://qm.valuechain.com/commin/feedback
#### 跳转到第三方页面（接入SDK）

● /main/third
● 参数 third_type : 第三方的类型  
● 参数 date：健康派激励视频专用，为三方视频ID

值 |说明
---|:--:
luckydraw	|幸运转盘
dati|	答题
ccy|	猜成语
ggk	|刮刮卡
downloadOrOpen	|打开或下载对应APP
video|	激励视屏，需要配置third_key为VIDEO ID
videoAll|	健康派专属激励视屏
read	|微信阅读，需要配置third_key为微信阅读第三方需要的物料ID
MGCGameCenter	|梦工厂游戏主页
MGCGame	|梦工厂单个游戏页面，需要配置third_key为游戏ID
● 参数 third_key : 激励视屏的id，只有third_type = video的时候设置
● 参数 third_key : wreadid 微信阅读第三方需要的物料ID，只有third_type = read的时候设置
● 参数 third_key : 打开或下载对应APP的时候，为对应APP的包名
● 健康派激励视频：arouter://qm.valuechain.com/main/third?third_type=videoAll&data={"CSJ_VIDEO_ID":"946148155","GDT_VIDEO_ID":"","KS_VIDEO_ID":""}
● 例如：arouter://qm.valuechain.com/main/third?third_type=video&third_key="{video_channel_id}"

#### 设置页面

● /mineAct/set
● 例如：arouter://qm.valuechain.com/mineSet/set
#### 个人信息

● /mineAct/info
● 例如：arouter://qm.valuechain.com/mineSet/info
#### 登录注册

● /login/login
● 例如：arouter://qm.valuechain.com/login/login



## 项目JSON配置文件

### 项目基础配置
● 项目基础配置 app/pontus-config-base.json
● 广告环境配置 app/pontus-config-ad.json
● 其他环境配置 app/pontus-config-um.json

### 读取配置文件
##### Grdle编译时，读取对应的配置文件
```
  def getQmConfig(String fileName) {
    println("获取" + fileName + "start!")
    File jsonFile = file(fileName)
    def parsedJson = new groovy.json.JsonSlurper().parseText(jsonFile.text)
    println("获取" + fileName + "end!")
    return parsedJson
  }
```
##### 解析出的值,一一对应的赋值
```
 def baseConfig = getQmConfig("pontus-config-base.json")
 def umConfig = getQmConfig("pontus-config-um.json")
 def adConfig = getQmConfig("pontus-config-ad.json")
 
 applicationId baseConfig.packagename
        versionCode baseConfig.version_code
        versionName baseConfig.version_name
        appName = baseConfig.app_name
        ...

        manifestPlaceholders = [
                appName                : baseConfig.app_name,
                scheme_host            : baseConfig.scheme_host,
                
                ...
        ]
```
#### 针对编译，区分正式与测试环境的域名，对应配置见项目基础配置
```
 productFlavors {
        // 测试环境
        develop {
            appType = "TEST"

            manifestPlaceholders = [
                BASE_SERVER_URL        : baseConfig.base_server_url_test,
                BASE_WEB_URL           : baseConfig.base_web_url_test,
            ]
        }
        // 官方渠道APP
        valuechain {
            appType = "OL"

        }

    }
```

## module_juggle 页面搭建模块
### 只需后台搭建一个页面，编译成对应的json文件，app解析对应的文件，渲染出原生的页面，暂时APP支持的页面组件类型有：

* 基础组件

  ![基础组件.png](https://zzjtest.oss-cn-hangzhou.aliyuncs.com/jc1.png)
* 业务组件

  ![业务组件.png](https://zzjtest.oss-cn-hangzhou.aliyuncs.com/yw1.png)
* 自定义组件
    1. 后台设置几个基础组件的宽高和背景；
    2. 后台通过添加不同的基础组件（文字，图片，按钮等），并设置对应的渲染方式；
    3. APP通过自定义组件，绘制出对应的组件；

## APP渲染（JuggleFragment，JuggleFraViewModel）
* 根据页面唯一标识，获取对应的页面文件内容；
* 解析文件，过滤对应的黑白名单（身份和用户双重过滤）
* 根据返回数据，遍历渲染页面，并显示弹窗等其他页面定制化配置；


