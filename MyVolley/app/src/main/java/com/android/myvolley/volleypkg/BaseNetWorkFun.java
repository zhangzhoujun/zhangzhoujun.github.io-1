package com.android.myvolley.volleypkg;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.android.myvolley.json.DateDeserializer;
import com.android.myvolley.model.MResponseBase;
import com.android.myvolley.utils.BaseComConst;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseNetWorkFun {
    public static String          TAG_SEND_GROUP_SIGN            = "TAG_SEND_GROUP_SIGN";           // 群签到
    public static String          TAG_DEL_GROUP_SIGN             = "TAG_DEL_GROUP_SIGN";            // 喊出签到
    public static String          TAG_GET_IM_CONFIG              = "TAG_GET_IM_CONFIG";             // 获取配置详情

    private static BaseNetWorkFun _instance;

    public static BaseNetWorkFun getInstance() {
        if (_instance == null)
            _instance = new BaseNetWorkFun();
        return _instance;
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
     * 成员签到
     * 
     * @param networkHelper
     * @param baichuanImId
     *            百川 im_id
     * @param signId
     *            签到 id
     * @param isImid
     *            是否是百川的ID
     */
    public void sendGroupSignIns(NetworkHelper<MResponseBase> networkHelper, String baichuanImId, String signId, boolean isImid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("is_im_id", String.valueOf(isImid));
        networkHelper.sendPostRequest(BaseComConst.SERVICE_PROTOCAL_DOMAIN + "/api/v2/chat_groups/" + baichuanImId + "/sign_ins/" + signId + "/members",
                params, TAG_SEND_GROUP_SIGN);
        return;
    }

    /**
     * 删除签到
     * 
     * @param networkHelper
     * @param baichuanImId
     * @param signId
     * @param isImid
     */
    public void sendDeleteGroupSign(NetworkHelper<MResponseBase> networkHelper, String baichuanImId, String signId, boolean isImid) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("is_im_id", String.valueOf(isImid));
        networkHelper.sendDeleteRequest(BaseComConst.SERVICE_PROTOCAL_DOMAIN + "/api/v2/chat_groups/" + baichuanImId + "/sign_ins/" + signId, params,
                TAG_DEL_GROUP_SIGN);
        return;
    }

    /**
     * 获取IM配置
     * 
     * @param networkHelper
     */
    public void getImConfig(NetworkHelper<MResponseBase> networkHelper) {
        networkHelper.sendGETRequest(BaseComConst.SERVICE_PROTOCAL_DOMAIN + "/api/utils/im_config", null, TAG_GET_IM_CONFIG);
        return;
    }
}
