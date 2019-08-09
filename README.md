# zhangzhoujun.github.io
张舟俊的个人博客

# PieView

## PieView 是一个，圆形的显示不同类型，不同颜色的自定义控件。

![演示Gif](https://github.com/dim1989/zhangzhoujun.github.io/blob/master/PieView/yanshi.gif?raw=true)


# BrandSort

## BrandSort是一个品牌排序。增加了一些动画，可以根据sortid排序，默认sordid越大越排在前面。

![演示Gif](https://github.com/dim1989/zhangzhoujun.github.io/blob/master/BrandSort/yanshi.gif?raw=true)


# DimDialog

## DimDIalog是一个简单的对话框，就是普通的对话框，第一个按钮，默认确定，第二个按钮默认不显示中间文本区域如果不满全屏，那么显示对应的大小，如果超过全屏，那么可滑动

# KLMLibrary

## 自己抽出来的一个通用的项目lib库，可以直接使用；主要包含以下几点内容
### 1. 基类的处理，
包含BaseActivity.kt 和 BaseFragment.kt；使用Kotlin语言封装，基类中包含6.0以后的权限处理代码，直接调用即可；以及一个管理Activity的ActivityManager；
### 2. 一些自定义的控件，主要包含：
  #### RecyclerView HeadAndFoot
      简单的为RecyclerView增加Header和Footer的工具类；代码中仅仅需要用HeaderAndFooterAdapter来代替RecyclerView.Adapter，然后就可以addHeader或者addFooter；
   #### Topbar
      一个用Kotlin写的通用的头部，XML中可以配制各种属性，具体参考res/values/atts中的Topbar配制属性；Topbar中左右按钮的icon使用的是IconFont字体；具体使用可以见控件KLMEduSohoIconTextView；
 #### 还有一些网络中寻来的自定义控件见代码；
### 3. 网络请求Http的封装：
   网络请求使用的是retrofit 2.0+OKHttp+MVP模式，兼容Https；
   具体示例代码等空些了再上传；
### 4. 图片加载框架Glide的封装
   引用了@doudou的对于Glide的封装，而后自己将图片控件定义为KLMImageView和KLMCircleImageView，使用的时候只需要在XML 中定义该控件，然后setImageUrl即可，参数支持多种格式，具 体参考代码；
### 5.其他的一些工具类
   日志管理LogUtils；下拉控件SuperSwipeRefreshLayout，使用方法和SwipeRefreshLayout一样，就是可以自定义下拉显图案；还有一些基础Toast类或者PackageUtil包管理类等；
   
## 由于当时项目时间紧急的关系，部分代码都是直接引用的第三方，后续会找到原作者记录，并且对于原来代码有一些修改之处也会记录，若有冒犯，请见谅！


