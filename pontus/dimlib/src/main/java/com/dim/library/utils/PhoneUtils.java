package com.dim.library.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @ClassName PhoneUtils
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/5/15 4:46 PM
 * @Version 1.0
 */
public class PhoneUtils {
    public static boolean checkPhone(String phone) {
        boolean result = true;

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort("请先输入手机号");
            result = false;

            return result;
        }

        //if (phone.length() != 11) {
        //    ToastUtils.showShort("请输入11位数手机号");
        //    result = false;
        //
        //    return result;
        //}

        if (!isNumeric(phone)) {
            ToastUtils.showShort("请输入正确的手机号");
            result = false;

            return result;
        }

        return result;
    }

    /**
     * 字符串纯数字判断
     *
     * @param str
     * @return 纯数字组成返回true
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public static String getImei(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method = null;
        try {
            method = manager.getClass().getMethod("getImei", int.class);
            String imei = (String) method.invoke(manager, 0);
            return imei;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return ("请获取 imei ");
    }
}
