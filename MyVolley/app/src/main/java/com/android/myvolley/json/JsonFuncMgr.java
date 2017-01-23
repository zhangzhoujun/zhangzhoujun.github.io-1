package com.android.myvolley.json;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.android.myvolley.model.MResponseBase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonFuncMgr {

    private static JsonFuncMgr _instance;

    public static JsonFuncMgr getInstance() {
        if (_instance == null)
            _instance = new JsonFuncMgr();
        return _instance;
    }

    /**
     * 验证是否成功 response.biz_action=0
     * 
     * @param response
     * @return
     */
    public Boolean ValidateResult(MResponseBase response) {
        Boolean bReturn = true;
        if (response.getBiz_action().equals("1")) {
            bReturn = false;
        }
        return bReturn;
    }

    /**
     * 对象转换成json字符串
     * 
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     * 
     * @paramstr
     * @param type
     * @return
     */
    public <T> T fromJson(MResponseBase response, Type type) throws Exception {

        GsonBuilder gsonb = new GsonBuilder();
        DateDeserializer dds = new DateDeserializer();
        gsonb.registerTypeAdapter(Date.class, dds);
        Gson gson = gsonb.create();

        if (ValidateResult(response)) {
            return gson.fromJson(response.getData(), type);
        } else {
            return null;
        }
    }

    /**
     * json字符串转成对象
     * 
     * @paramstr
     * @param type
     * @return
     */
    public <T> T fromJson(MResponseBase response, Class<T> type) throws Exception {
        Gson gson = new Gson();
        if (ValidateResult(response)) {
            return gson.fromJson(response.getData(), type);
        } else {
            return null;
        }
    }

    /**
     * json字符串转成对象
     * 
     * @paramstr
     * @param type
     * @return
     */
    public <T> T fromJsonDate(String jsonString, Class<T> type) throws Exception {
        GsonBuilder gsonb = new GsonBuilder();
        DateDeserializer dds = new DateDeserializer();
        gsonb.registerTypeAdapter(Date.class, dds);
        Gson gson = gsonb.create();
        return gson.fromJson(jsonString, type);
    }

    /**
     * json字符串转成对象
     * 
     * @paramstr
     * @param type
     * @return
     */
    public <T> List<T> fromJsonDateList(String jsonString, Type type) throws Exception {
        GsonBuilder gsonb = new GsonBuilder();
        DateDeserializer dds = new DateDeserializer();
        gsonb.registerTypeAdapter(Date.class, dds);
        Gson gson = gsonb.create();
        return gson.fromJson(jsonString, type);
    }

    /**
     * 根据本项目特定格式 返回数据
     * 如{"biz_action":0,"biz_msg":null,"return_status":0,"data":{"auth_code"
     * :"332360"}}
     * 
     * @param strReturn
     * @return
     */
    public MResponseBase GetMResponse(String strReturn) {
        try {
            MResponseBase response = new MResponseBase();
            JSONObject dataJson = new JSONObject(strReturn);
            if (dataJson.has("biz_action")) {
                response.setBiz_action(dataJson.getString("biz_action"));
            }
            if (dataJson.has("biz_msg")) {
                response.setBiz_msg(dataJson.getString("biz_msg"));
            }
            if (dataJson.has("return_status")) {
                response.setReturn_status(dataJson.getString("return_status"));
            }
            if (dataJson.has("data")) {
                response.setData(dataJson.getString("data"));
            }
            return response;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
}
