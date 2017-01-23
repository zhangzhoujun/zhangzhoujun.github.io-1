package com.android.myvolley.volleypkg;

import org.json.JSONObject;

import com.android.myvolley.model.MResponseBase;
import com.android.myvolley.volley.VolleyError;

import android.content.Context;

public class ReverseRegisterNetworkHelper extends NetworkHelper<MResponseBase> {

    public ReverseRegisterNetworkHelper(Context context) {
        super(context);
    }

    @Override
    protected void disposeVolleyError(VolleyError error, Object tag) {
        notifyErrorHappened(SystemParams.VOLLEY_ERROR_CODE, error == null ? "NULL" : error.getMessage(), tag, null);
    }

    @Override
    protected void disposeResponse(JSONObject response, Object tag) {
        MResponseBase rrBean = new MResponseBase();

        if (response != null) {
            try {
                if (response.has("biz_action")) {
                    rrBean.setBiz_action(response.getString("biz_action"));
                }
                if (response.has("biz_msg")) {
                    rrBean.setBiz_msg(response.getString("biz_msg"));
                }
                if (response.has("return_status")) {
                    rrBean.setReturn_status(response.getString("return_status"));
                }
                if (response.has("date")) {
                    rrBean.setData(response.getString("date"));
                }
                if (response.has("data")) {
                    rrBean.setData(response.getString("data"));
                }
                if (response.has("server_time")) {
                    rrBean.setServer_time(response.getString("server_time"));
                }
                rrBean.setAll_data(response.toString());

                // 2016年5月3日增加，add by 张舟俊
                // 如果服务端返回99，是成功处理，是在用户信息修改的时候才会出现，需要修改本地缓存的用户信息
                // if (rrBean.getBiz_action().equals("99")) {
                // // 为了安全处理，防止有页面会处理成功但是又判断了action是否为0，所以修改本地的action为0
                // rrBean.setBiz_action("0");
                // // 修改本地缓存的信息
                // refreshUserInfo(rrBean.getData(), rrBean, tag);
                // } else
                if (rrBean.getBiz_action().equals("") || "0".equals(rrBean.getBiz_action())) {
                    notifyDataChanged(rrBean, tag);
                } else {
                    notifyErrorHappened(rrBean.getBiz_action(), rrBean.getBiz_msg(), tag, rrBean.getData());
                }
            } catch (Exception e) {
                notifyErrorHappened(SystemParams.RESPONSE_FORMAT_ERROR, "Response format error", tag, rrBean.getData());
            }
        } else {
            notifyErrorHappened(SystemParams.RESPONSE_IS_NULL, "Response is null!", tag, rrBean.getData());
        }

    }

    /**
     * 刷新用户信息
     * 
     * @param json
     */
    // private void refreshUserInfo(String json, MResponseBase rrBean, Object tag) {
    // JSONObject dataJson;
    // try {
    // dataJson = new JSONObject(json);
    // if (dataJson.has("user")) {
    // MUser mRefreshUser = new MUser();
    // mRefreshUser = NetWorkFun.getInstance().fromJsonDate(dataJson.toString(),
    // mRefreshUser.getClass());
    // CommonUser user = CommonUser.getInstance();
    // user.Login(mRefreshUser);
    // }
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } finally {
    // // 通知当前页面
    // notifyDataChanged(rrBean, tag);
    // }
    // }

}