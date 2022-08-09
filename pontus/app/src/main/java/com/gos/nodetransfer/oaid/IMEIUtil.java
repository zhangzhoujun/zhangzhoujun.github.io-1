package com.gos.nodetransfer.oaid;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class IMEIUtil {
    public IMEIUtil() {
    }

    public static String getIMEI(Context context, int slotId) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService("phone");
            Method method = manager.getClass().getMethod("getImei", Integer.TYPE);
            String imei = (String) method.invoke(manager, slotId);
            return imei;
        } catch (Exception var5) {
            return "";
        }
    }

    @SuppressLint({"MissingPermission"})
    public static String getImeiOrMeid(Context ctx) {
        TelephonyManager manager = (TelephonyManager) ctx.getSystemService("phone");
        return manager != null ? manager.getDeviceId() : null;
    }

    @TargetApi(23)
    @SuppressLint({"MissingPermission"})
    public static Map getImeiAndMeid(Context ctx) {
        Map<String, String> map = new HashMap();
        TelephonyManager mTelephonyManager = (TelephonyManager) ctx.getSystemService("phone");
        Class<?> clazz = null;
        Method method = null;

        try {
            clazz = Class.forName("android.os.SystemProperties");
            method = clazz.getMethod("get", String.class, String.class);
            String gsm = (String) method.invoke((Object) null, "ril.gsm.imei", "");
            String meid = (String) method.invoke((Object) null, "ril.cdma.meid", "");
            map.put("meid", meid);
            if (!TextUtils.isEmpty(gsm)) {
                String[] imeiArray = gsm.split(",");
                if (imeiArray != null && imeiArray.length > 0) {
                    map.put("imei1", imeiArray[0]);
                    if (imeiArray.length > 1) {
                        map.put("imei2", imeiArray[1]);
                    } else {
                        map.put("imei2", mTelephonyManager.getDeviceId(1));
                    }
                } else {
                    map.put("imei1", mTelephonyManager.getDeviceId(0));
                    map.put("imei2", mTelephonyManager.getDeviceId(1));
                }
            } else {
                map.put("imei1", mTelephonyManager.getDeviceId(0));
                map.put("imei2", mTelephonyManager.getDeviceId(1));
            }
        } catch (ClassNotFoundException var8) {
            var8.printStackTrace();
        } catch (NoSuchMethodException var9) {
            var9.printStackTrace();
        } catch (IllegalAccessException var10) {
            var10.printStackTrace();
        } catch (InvocationTargetException var11) {
            var11.printStackTrace();
        }

        return map;
    }

    @TargetApi(26)
    @SuppressLint({"MissingPermission"})
    public static Map getIMEIforO(Context context) {
        HashMap map = new HashMap();

        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
            String imei1 = tm.getImei(0);
            String imei2 = tm.getImei(1);
            if (TextUtils.isEmpty(imei1) && TextUtils.isEmpty(imei2)) {
                map.put("imei1", tm.getMeid());
            } else {
                map.put("imei1", imei1);
                map.put("imei2", imei2);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return map;
    }

    public static String getIMEI(Context ctx) {
        String imei = "";
        if (VERSION.SDK_INT < 23) {
            imei = getIMEI(ctx, 0);
            if (TextUtils.isEmpty(imei)) {
                imei = getImeiOrMeid(ctx);
            }
        } else {
            Map imeiMaps;
            if (VERSION.SDK_INT < 26) {
                imeiMaps = getImeiAndMeid(ctx);
                imei = getImei1(imeiMaps);
            } else {
                imeiMaps = getIMEIforO(ctx);
                imei = getImei1(imeiMaps);
            }
        }

        return imei;
    }

    public static String getIMEI2(Context ctx) {
        String imei = "";
        if (VERSION.SDK_INT < 23) {
            imei = getIMEI(ctx, 1);
        } else {
            Map imeiMaps;
            if (VERSION.SDK_INT < 26) {
                imeiMaps = getImeiAndMeid(ctx);
                imei = getImei2(imeiMaps);
            } else {
                imeiMaps = getIMEIforO(ctx);
                imei = getImei2(imeiMaps);
            }
        }

        return imei;
    }

    private static String getImei1(Map imeiMaps) {
        String imei = "";
        return imeiMaps != null && imeiMaps.containsKey("imei1") ? (String) imeiMaps.get("imei1") : imei;
    }

    private static String getImei2(Map imeiMaps) {
        String imei = "";
        return imeiMaps != null && imeiMaps.containsKey("imei2") ? (String) imeiMaps.get("imei2") : imei;
    }
}
