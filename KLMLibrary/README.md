# KLMImageView
 Android 简便的图片库，封装了fresco,非常方便使用，支持本地图片，网络图片，支持webp, gif,支持支持圆形图。
 
##使用简单示例
<com.kalemao.library.imageview.KLMImageView
    android:id="@+id/demo_image_view"
    android:layout_width="150dp"
    android:layout_height="150dp" />
    
    
KLMImageView imageView = (KLMImageView) findViewById(R.id.demo_image_view);

## KLMImageView普通图片
 //直接设置url，其他都不用管
public void setImageUrl(String url);

//直接设置res id，其他都不用管
public void setImageRes(int resId);

//静态方法，用来手动清除内存里的缓存
public static void clearMemoryCaches();

//静态方法，用来手动清除文件里的缓存
public static void clearDiskCaches();

//静态方法，用来手动清除所有缓存
public static void clearCaches();

//静态方法，初始化，放在application的oncreate里 
public static void init(Context context);

## KLMCircleImageView圆形图片
包含KLMImageView接口，另外包含以下接口
//设置边框颜色
void setBorderColor(int color);

//设置边框宽度
void setBorderWidth(int width);