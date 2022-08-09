package com.qm.lib.http;

import com.dim.library.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.qm.lib.utils.JYMMKVManager;
import com.qm.lib.utils.JYUtils;
import com.qm.lib.utils.MD5Utils;
import com.qm.lib.utils.SystemUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by  on 2017/5/10.
 */
public class RequestInterceptor implements Interceptor {
    Gson gson;

    public RequestInterceptor() {
        // 这里的Gson设置是为了解决jsonString转map时会出现Int被转成Double的问题
        this.gson = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<HashMap<String, Object>>() {
                        }.getType(),
                        new JsonDeserializer<HashMap<String, Object>>() {
                            @Override
                            public HashMap<String, Object> deserialize(
                                    JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {

                                HashMap<String, Object> hashMap = new HashMap<>();
                                JsonObject jsonObject = json.getAsJsonObject();
                                Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                                for (Map.Entry<String, JsonElement> entry : entrySet) {
                                    hashMap.put(entry.getKey(), entry.getValue());
                                }
                                return hashMap;
                            }
                        }).create();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //请求定制：添加请求头
        Request.Builder requestBuilder = request.newBuilder();

        //if (request.method().equals("POST")) {
        //    RequestBody requestBody = request.body();
        //    if (requestBody instanceof PostJsonBody) {
        //        String content = ((PostJsonBody) requestBody).getContent();
        //        HashMap<String, Object> hashMap = gson.fromJson(content, new TypeToken<HashMap<String, Object>>() {
        //        }.getType());
        //        //添加统一参数
        //        String deviceId = JYMMKVManager.Companion.getInstance().getDeviceId();
        //        long timeStamp = System.currentTimeMillis();
        //        String appId = JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "QY_APP_ID");
        //        String appSecret = JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "QY_APP_SECRET");
        //        hashMap.put("appId", appId);
        //        hashMap.put("phoneModel", SystemUtil.getSystemModel());
        //        hashMap.put("phoneBrand", SystemUtil.getDeviceBrand());
        //        hashMap.put("timeZone", "GMT+8");
        //        hashMap.put("language", "zh-CN");
        //        hashMap.put("version", SystemUtil.getVersionName(Utils.getContext()));
        //        hashMap.put("appType", "Android");
        //        hashMap.put("appName", JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "QY_APP_NAME"));
        //        hashMap.put("timestamp", String.valueOf(timeStamp));
        //        hashMap.put("sign", MD5Utils.digest(appId + timeStamp + appSecret));
        //        hashMap.put("deviceId", deviceId);
        //        hashMap.put("requestId", MD5Utils.digest(deviceId + timeStamp + (int) ((Math.random() * 9 + 1) * 100000)));
        //        hashMap.put("userToken", JYMMKVManager.Companion.getInstance().getToken());
        //        // get请求 url参数转到post body中data
        //        Set<String> paramsList = request.url().queryParameterNames();
        //        if (paramsList != null && paramsList.size() > 0) {
        //            if (hashMap.containsKey("data")) {
        //                JsonObject obj = (JsonObject) hashMap.get("data");
        //                for (int i = 0; i < paramsList.size(); i++) {
        //                    obj.addProperty(request.url().queryParameterName(i), request.url().queryParameterValue(i));
        //                }
        //            }
        //        }
        //        //end
        //        requestBuilder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(hashMap)));
        //    }
        //
        //}
        return chain.proceed(requestBuilder.build());
    }
}