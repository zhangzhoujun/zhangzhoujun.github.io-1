当前项目引用了一个Module作为lib，该lib值提供了“armeabi”，“armebi-v7”和“x86”的对应的so文件，由于没有“armeabi-v8”的so文件，导致编译的时候报“NDK integration is deprecated in the current plugin.”。
解决方法是：
在自己的app下得build.gradle文件的defaultConfig下加上以下代码：
// 由于百川不提供arm x86支持,打包的时候指定提供so文件的平台打包
        ndk{
            moduleName "xxx"  //设置库(so)文件名称
            ldLibs "log"
            abiFilters  "armeabi","armeabi-v7a", "x86"
        }
指定该module对应的so文件的支持。
然后在工程的gradle.properties文件中加上以下代码：
android.useDeprecatedNdk=true
即可解决。