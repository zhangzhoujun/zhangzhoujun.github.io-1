package com.gos.nodetransfer.utils;

import android.content.SharedPreferences;

/**
 * @author dim
 * @create at 2019-06-14 14:54
 * @description:
 */
public class GosNodeUtils {
    private static SPUtils spUtils;

    public static void initSp() {
        spUtils = SPUtils.getInstance("node_transfer");
    }

    private static SharedPreferences sp;

    private static GosNodeUtils instance = null;

    public static GosNodeUtils getInstance() {
        if (instance == null) {
            synchronized (GosNodeUtils.class) {
                if (instance == null) {
                    instance = new GosNodeUtils();
                }
            }
        }
        return instance;
    }

    private GosNodeUtils() {
        initSp();
    }

    public static String getToken() {
        if (spUtils == null) {
            initSp();
        }
        String token = spUtils.getString("token");
        return token;
    }

    public static void setToken(String token) {
        if (spUtils == null) {
            initSp();
        }
        spUtils.put("token", token);
    }

    public static boolean isAutoLogin() {
        if (spUtils == null) {
            initSp();
        }
        return spUtils.getBoolean("autoLogin", false);
    }

    public static void setAutoLogin(boolean auto) {
        if (spUtils == null) {
            initSp();
        }
        spUtils.put("autoLogin", auto);
    }

}
