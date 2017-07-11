package com.kalemao.library.logutils.log;

import android.util.Log;

import com.kalemao.library.logutils.LogUtil;
import com.kalemao.library.logutils.LogUtilHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dim on 2017/5/22 15:05
 * 邮箱：271756926@qq.com
 */

public class JsonLog {
    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(LogUtil.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(LogUtil.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        LogUtilHelper.printLine(tag, true);
        message = headString + LogUtil.LINE_SEPARATOR + message;
        String[] lines = message.split(LogUtil.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        LogUtilHelper.printLine(tag, false);
    }
}
