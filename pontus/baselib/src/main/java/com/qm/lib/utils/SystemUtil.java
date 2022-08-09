package com.qm.lib.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.TouchDelegate;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.dim.library.base.AppManager;
import com.dim.library.http.interceptor.logging.Level;
import com.dim.library.http.interceptor.logging.LoggingInterceptor;
import com.dim.library.utils.DLog;
import com.dim.library.utils.StringUtils;
import com.dim.library.utils.ToastUtils;
import com.dim.library.utils.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qm.lib.entity.COssConfig;
import com.qm.lib.entity.MPostResponse;
import com.qm.lib.http.FileRequest;
import com.qm.lib.http.FileUploadInterface;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.internal.platform.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class SystemUtil {

    public SystemUtil() {
    }

    // 隐藏软键盘
    public static void hideSoftInput(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static Object cloneObject(Object from) {
        Object to = null;
        try {
            to = from.getClass().newInstance();
            Field[] fs = from.getClass().getDeclaredFields();
            for (Field field : fs) {
                field.setAccessible(true);
                Object value = field.get(from);
                try {
                    Field ff = to.getClass().getDeclaredField(field.getName());
                    ff.setAccessible(true);
                    ff.set(to, value);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }

            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return to;
    }

    /**
     * 播放系统提示音
     */
    public static void playNoticeVoice() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(Utils.getContext(), notification);
        r.play();
    }

    public static void expandTouchArea(final View view, final int size) {
        final View parentView = (View) view.getParent();
        parentView.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                view.getHitRect(rect);

                rect.top -= size;
                rect.bottom += size;
                rect.left -= size;
                rect.right += size;

                parentView.setTouchDelegate(new TouchDelegate(rect, view));
            }
        });
    }

    /**
     * 根据指定内容生成自定义宽高的二维码图片
     *
     * @param content   需要生成二维码的内容
     * @param QR_WIDTH  二维码宽度
     * @param QR_HEIGHT 二维码高度
     * @throws WriterException 生成二维码异常
     */
    public static Bitmap makeQRImage(String content, int QR_WIDTH, int QR_HEIGHT) throws WriterException {

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, "1");
        // 图像数据转换，使用了矩阵转换
        BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
        int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
        // 按照二维码的算法，逐个生成二维码的图片，两个for循环是图片横列扫描的结果
        for (int y = 0; y < QR_HEIGHT; y++) {
            for (int x = 0; x < QR_WIDTH; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * QR_WIDTH + x] = 0xff000000;
                } else {
                    pixels[y * QR_WIDTH + x] = 0xffffffff;
                }
            }
        }

        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        return bitmap;
    }

    public static Boolean isNetworkConnected(Context context) {
        try {
            context = context.getApplicationContext();
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 转换为 form-data
     *
     * @param requestDataMap
     * @return
     */
    public static Map<String, RequestBody> generateRequestBody(Map<String, String> requestDataMap) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requestDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    requestDataMap.get(key) == null ? "" : requestDataMap.get(key));
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        if (checkNavigationBarShow(context)) {
            int totalHeight = getDpi(context);
            int contentHeight = getScreenHeight(context);
            return totalHeight - contentHeight;
        } else {
            return 0;
        }
    }

    /**
     * 判断虚拟导航栏是否显示
     *
     * @param context 上下文对象
     * @return true(显示虚拟导航栏)，false(不显示或不支持虚拟导航栏)
     */
    public static boolean checkNavigationBarShow(@NonNull Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            //判断是否隐藏了底部虚拟导航
            int navigationBarIsMin = 0;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                navigationBarIsMin = Settings.System.getInt(context.getContentResolver(),
                        "navigationbar_is_min", 0);
            } else {
                navigationBarIsMin = Settings.Global.getInt(context.getContentResolver(),
                        "navigationbar_is_min", 0);
            }
            if ("1".equals(navBarOverride) || 1 == navigationBarIsMin) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }


    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    //获取屏幕高度 不包含虚拟按键=
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }


    /**
     * 发送上传文件网络请求
     *
     * @param url  请求地址
     * @param file 文件
     */
    public static void fileUpload(String url, File file, COssConfig config, FileUploadInterface listener) {
        MPostResponse mPostResponse = new MPostResponse();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(false) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build()
                )
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        FileRequest fileRequest = retrofit.create(FileRequest.class);

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), compressImage(BitmapFactory.decodeFile(file.getPath())));

        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", "android.jpg", photoRequestBody);

        Call<MPostResponse> call = fileRequest.postFile(url, getBody(config.getSuccess_action_status()), getBody(config.getOSSAccessKeyId()),
                getBody(config.getPolicy()), getBody(config.getSignature()), getBody(config.getKey()), photo);

        call.enqueue(new Callback<MPostResponse>() {
            @Override
            public void onResponse(Call<MPostResponse> call, Response<MPostResponse> response) {
                DLog.d("ICON", "response.isSuccessful() = " + response.isSuccessful());
                if (response.isSuccessful()) {
                    mPostResponse.setSuccess(true);
                    mPostResponse.setLocation(response.body().getLocation());
                    mPostResponse.setKey(response.body().getKey());
                    mPostResponse.setETag(response.body().getETag());
                    mPostResponse.setBucket(response.body().getBucket());
                    listener.onFileUploadResule(mPostResponse);
                } else {
                    DLog.d("OSS", "onResponse Failure = " + response.toString());
                    mPostResponse.setSuccess(false);
                    listener.onFileUploadResule(mPostResponse);
                }
            }

            @Override
            public void onFailure(Call<MPostResponse> call, Throwable t) {
                DLog.d("OSS", "onFailure = " + t.getMessage());
                mPostResponse.setSuccess(false);
                listener.onFileUploadResule(mPostResponse);
            }


        });
    }

    private static RequestBody getBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), value);
    }

    /**
     * 发送上传文件网络请求
     *
     * @param url  请求地址
     * @param file 文件
     */
    public static void fileAudioUpload(String url, File file, COssConfig config, FileUploadInterface listener) {

        MPostResponse mPostResponse = new MPostResponse();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(false) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build()
                )
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        FileRequest fileRequest = retrofit.create(FileRequest.class);

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", "android.amr", photoRequestBody);

        Call<MPostResponse> call = fileRequest.postFile(url, getBody(config.getSuccess_action_status()), getBody(config.getOSSAccessKeyId()),
                getBody(config.getPolicy()), getBody(config.getSignature()), getBody(config.getKey()), photo);

        call.enqueue(new Callback<MPostResponse>() {
            @Override
            public void onResponse(Call<MPostResponse> call, Response<MPostResponse> response) {
                DLog.d("ICON", "response.isSuccessful() = " + response.isSuccessful());
                if (response.isSuccessful()) {
                    mPostResponse.setSuccess(true);
                    mPostResponse.setLocation(response.body().getLocation());
                    mPostResponse.setKey(response.body().getKey());
                    mPostResponse.setETag(response.body().getETag());
                    mPostResponse.setBucket(response.body().getBucket());
                    listener.onFileUploadResule(mPostResponse);
                } else {
                    DLog.d("OSS", "onResponse Failure = " + response.toString());
                    mPostResponse.setSuccess(false);
                    listener.onFileUploadResule(mPostResponse);
                }
            }

            @Override
            public void onFailure(Call<MPostResponse> call, Throwable t) {
                DLog.d("OSS", "onFailure = " + t.getMessage());
                mPostResponse.setSuccess(false);
                listener.onFileUploadResule(mPostResponse);
            }
        });
    }

    /**
     * 发送上传文件网络请求
     *
     * @param url  请求地址
     * @param file 文件
     */
    public static void fileTxtUpload(String url, File file, COssConfig config, FileUploadInterface listener) {

        MPostResponse mPostResponse = new MPostResponse();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS))
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor
                        .Builder()//构建者模式
                        .loggable(false) //是否开启日志打印
                        .setLevel(Level.BASIC) //打印的等级
                        .log(Platform.INFO) // 打印类型
                        .request("Request") // request的Tag
                        .response("Response")// Response的Tag
                        .build()
                )
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        FileRequest fileRequest = retrofit.create(FileRequest.class);

        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);

        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", "android.txt", photoRequestBody);

        Call<MPostResponse> call = fileRequest.postFile(url, getBody(config.getSuccess_action_status()), getBody(config.getOSSAccessKeyId()),
                getBody(config.getPolicy()), getBody(config.getSignature()), getBody(config.getKey()), photo);

        call.enqueue(new Callback<MPostResponse>() {
            @Override
            public void onResponse(Call<MPostResponse> call, Response<MPostResponse> response) {
                DLog.d("ICON", "response.isSuccessful() = " + response.isSuccessful());
                DLog.d("ICON", "response.isSuccessful() = " + response.toString());
                if (response.isSuccessful()) {
                    mPostResponse.setSuccess(true);
                    mPostResponse.setLocation(response.body().getLocation());
                    mPostResponse.setKey(response.body().getKey());
                    mPostResponse.setETag(response.body().getETag());
                    mPostResponse.setBucket(response.body().getBucket());
                    listener.onFileUploadResule(mPostResponse);
                } else {
                    DLog.d("OSS", "onResponse Failure = " + response.toString());
                    mPostResponse.setSuccess(false);
                    listener.onFileUploadResule(mPostResponse);
                }
            }

            @Override
            public void onFailure(Call<MPostResponse> call, Throwable t) {
                DLog.d("OSS", "onFailure = " + t.getMessage());
                mPostResponse.setSuccess(false);
                listener.onFileUploadResule(mPostResponse);
            }
        });
    }


    /**
     * 添加多媒体类型
     *
     * @param paramMap 参数对
     * @param key      键
     * @param obj      值
     */
    private static void addMultiPart(Map<String, RequestBody> paramMap, String key, Object obj) {
        if (obj instanceof String) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), (String) obj);
            paramMap.put(key, body);
        } else if (obj instanceof File) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), (File) obj);
            paramMap.put(key + "\"; filename=\"" + ((File) obj).getName() + "", body);
        }
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, int options) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
        return baos.toByteArray();
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static byte[] compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (options > 1 && baos.toByteArray().length / 1024 > 3000) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            DLog.d("IMAGE", "IMAGE size == " + baos.toByteArray().length / 1024);
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options > 10) {
                options -= 10;// 每次都减少10
            } else {
                options -= 2;
            }
            if (options == 0) {
                options = 1;
            }
        }

        // ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        // return bitmap;

        return baos.toByteArray();
    }

    /**
     * 获取当前应用的版本号
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getVersionName(Context context) {
        String version = "1.0.0";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return version;
        }
    }


    /**
     * 获取设备唯一标识码
     */
    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && lacksPermission
                (context, Manifest.permission.READ_PHONE_STATE)) {
            RxPermissions rxPermissions = new RxPermissions((FragmentActivity) AppManager.getActivityStack().get(0));
            rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {

                        }
                    });
        } else {
            // IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }

        }

        // 序列号（sn）
        String sn = SerialNumUtils.get();
        if (!TextUtils.isEmpty(sn)) {
            deviceId.append("sn");
            deviceId.append(sn);
            return deviceId.toString();
        }
        // 如果上面都没有， 则生成一个id：随机码
        String uuid = UUID.randomUUID().toString();
        if (!TextUtils.isEmpty(uuid)) {
            deviceId.append("uuid");
            deviceId.append(uuid);
            return deviceId.toString();
        }
        return deviceId.toString();

    }


    /**
     * 判断是否缺少权限
     */
    private static boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) ==
                PackageManager.PERMISSION_DENIED;
    }

    /**
     * 获取虚拟按键的高度
     * 1. 全面屏下
     * 1.1 开启全面屏开关-返回0
     * 1.2 关闭全面屏开关-执行非全面屏下处理方式
     * 2. 非全面屏下
     * 2.1 没有虚拟键-返回0
     * 2.1 虚拟键隐藏-返回0
     * 2.2 虚拟键存在且未隐藏-返回虚拟键实际高度
     */
    public static int getNavigationBarHeightIfRoom(Context context) {
        if (navigationGestureEnabled(context)) {
            return 0;
        }
        return getCurrentNavigationBarHeight(((Activity) context));
    }

    /**
     * 全面屏（是否开启全面屏开关 0 关闭  1 开启）
     *
     * @param context
     * @return
     */
    public static boolean navigationGestureEnabled(Context context) {
        int val = Settings.Global.getInt(context.getContentResolver(), getDeviceInfo(), 0);
        return val != 0;
    }

    /**
     * 获取设备信息（目前支持几大主流的全面屏手机，亲测华为、小米、oppo、魅族、vivo都可以）
     *
     * @return
     */
    public static String getDeviceInfo() {
        String brand = Build.BRAND;
        if (TextUtils.isEmpty(brand)) return "navigationbar_is_min";

        if (brand.equalsIgnoreCase("HUAWEI")) {
            return "navigationbar_is_min";
        } else if (brand.equalsIgnoreCase("XIAOMI")) {
            return "force_fsg_nav_bar";
        } else if (brand.equalsIgnoreCase("VIVO")) {
            return "navigation_gesture_on";
        } else if (brand.equalsIgnoreCase("OPPO")) {
            return "navigation_gesture_on";
        } else {
            return "navigationbar_is_min";
        }
    }

    /**
     * 非全面屏下 虚拟键实际高度(隐藏后高度为0)
     *
     * @param activity
     * @return
     */
    public static int getCurrentNavigationBarHeight(Activity activity) {
        if (isNavigationBarShown(activity)) {
            return getNavigationBarHeight(activity);
        } else {
            return 0;
        }
    }

    /**
     * 非全面屏下 虚拟按键是否打开
     *
     * @param activity
     * @return
     */
    public static boolean isNavigationBarShown(Activity activity) {
        //虚拟键的view,为空或者不可见时是隐藏状态
        View view = activity.findViewById(android.R.id.navigationBarBackground);
        if (view == null) {
            return false;
        }
        int visible = view.getVisibility();
        if (visible == View.GONE || visible == View.INVISIBLE) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 非全面屏下 虚拟键高度(无论是否隐藏)
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    //身份证前三后四脱敏
    public static String idEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    //护照前2后3位脱敏，护照一般为8或9位
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

    public static void mobileVibrator(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);

    }

    public static void openApp(Context context, String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            ToastUtils.showShort("应用未安装");
            return;
        }

        Intent resolveIntent = context.getPackageManager().getLaunchIntentForPackage(packagename);// 这里的packname就是从上面得到的目标apk的包名
        context.startActivity(resolveIntent);// 启动目标应用
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
}
