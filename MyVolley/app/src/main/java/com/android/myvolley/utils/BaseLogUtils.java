package com.android.myvolley.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.util.Log;

import com.android.myvolley.volley.Request;
import com.android.myvolley.volley.VolleyError;

public class BaseLogUtils {

    private static final String TAG     = "Log";
    private static final String URL_TAG = "http_Log";

    /** 日志输出函数 **/
    public static void log(String msg) {
        if (getEnableLog()) {
            log(TAG, msg);
        }
    }

    public static void log(String tag, String msg) {
        if (getEnableLog()) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (getEnableLog()) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, IOException e) {
        if (getEnableLog()) {
            Log.d(tag, msg, e);
        }
    }

    public static void i(String tag, String msg) {
        if (getEnableLog()) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, IOException e) {
        if (getEnableLog()) {
            Log.i(tag, msg, e);
        }
    }

    public static void log(String format, Object... args) {
        if (getEnableLog()) {
            log(TAG, format, args);
        }
    }

    public static void e(String tag, String msg) {
        if (getEnableLog()) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg, IOException e) {
        if (getEnableLog()) {
            Log.i(tag, msg, e);
        }
    }

    public static void log(String tag, String format, Object... args) {
        if (getEnableLog()) {
            Log.d(tag, String.format(format, args));
        }
    }

    public static void log(Throwable e) {
        if (getEnableLog()) {
            e.printStackTrace();
        }
    }

    /**
     * 发送的请求log
     *
     */
    public static <T> void logRequest(Request<T> request) {
        if (getEnableLog()) {
            d(URL_TAG, "请求开始======================");
            logRequestSelf(request);
            d(URL_TAG, "请求结束======================");
        }
    }

    private static <T> void logRequestSelf(Request<T> request) {
        d(URL_TAG, "METHED == " + request.getMethod());
        d(URL_TAG, "url == " + request.getUrl());
        try {
            d(URL_TAG, "url tag   == " + request.getTag());
            Map<String, String> heads = request.getHeaders(request.getHeadType());
            Set set = heads.keySet();
            for (Iterator iter = set.iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                String value = (String) heads.get(key);
                d(URL_TAG, "url key   == " + key);
                d(URL_TAG, "url value == " + value);
                d(URL_TAG, "body == " + new String(request.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            d(URL_TAG, "请求结束,解析报错======================");
        }
    }

    public static <T> void logOnResponse(JSONObject response, Object tag) {
        if (getEnableLog()) {
            d(URL_TAG, "返回开始======================");
            try {
                Request mRequest = (Request) tag;
                logRequestSelf(mRequest);
                d(URL_TAG, "--");
                d(URL_TAG, "TAG == " + String.valueOf(tag));
                d(URL_TAG, "--");
                if (response != null) {
                    d(URL_TAG, response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                d(URL_TAG, "请求结束,解析报错======================");
            }
            d(URL_TAG, "返回结束======================");
        }
    }

    public static <T> void logOnResponseError(VolleyError error, Object tag) {
        if (getEnableLog()) {
            d(URL_TAG, "返回开始======================");
            try {
                Request mRequest = (Request) tag;
                logRequestSelf(mRequest);
                d(URL_TAG, "--");
                d(URL_TAG, "TAG == " + String.valueOf(tag));
                d(URL_TAG, "--");
                if (error.getMessage() != null) {
                    d(URL_TAG, error.getMessage());
                }
                if (error.getStackTrace().toString() != null) {
                    d(URL_TAG, error.getStackTrace().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                d(URL_TAG, "请求结束,解析报错======================");
            }
            d(URL_TAG, "返回结束======================");
        }
    }

    /**
     * 自己可以动态设置日志的开关
     * @return
     */
    private static boolean getEnableLog() {
        return true;
    }

}
