package com.qm.lib.utils;

import java.lang.reflect.Method;

/**
 * http://blog.csdn.net/hpccn/article/details/22684953
 *
 * @author 作者 : davidtps
 * @version 创建时间：2015年12月4日 下午8:41:10
 */
public class SerialNumUtils {
    static Method systemProperties_get = null;

    public static String get() {
        return getAndroidOsSystemProperties("ro.boot.serialno");
    }


    static String getAndroidOsSystemProperties(String key) {
        String ret;
        try {
            systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
            if ((ret = (String) systemProperties_get.invoke(null, key)) != null)
                return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return "";
    }

}
