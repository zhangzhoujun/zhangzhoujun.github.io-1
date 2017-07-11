package com.kalemao.library.base;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

/**
 * Created by dim on 2017/5/22 13:49 邮箱：271756926@qq.com
 */

public class MResponse {

    private int    biz_action;
    private String biz_msg;
    private int    return_status;
    private String server_time;
    private String data;

       public static class JsonAdapter implements JsonDeserializer<MResponse> {
        @Override
        public MResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String jsonRoot = json.getAsJsonObject().toString();
                MResponse response = new MResponse();
                JSONObject jsobRespData = new JSONObject(jsonRoot);
                response.biz_action = jsobRespData.getInt("biz_action");
                response.biz_msg = jsobRespData.getString("biz_msg");
                response.return_status = jsobRespData.getInt("return_status");
                response.server_time = jsobRespData.getString("server_time");
                response.data = jsobRespData.getString("data");
                return response;
            } catch (JSONException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public int getBiz_action() {
        return biz_action;
    }

    public void setBiz_action(int biz_action) {
        this.biz_action = biz_action;
    }

    public String getBiz_msg() {
        return biz_msg;
    }

    public void setBiz_msg(String biz_msg) {
        this.biz_msg = biz_msg;
    }

    public int getReturn_status() {
        return return_status;
    }

    public void setReturn_status(int return_status) {
        this.return_status = return_status;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getData() {
        return data.toString();
    }

    public void setData(String data) {
        this.data = data;
    }
}
