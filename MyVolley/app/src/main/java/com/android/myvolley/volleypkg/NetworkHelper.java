package com.android.myvolley.volleypkg;

import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;

import com.android.myvolley.utils.BaseComFunc;
import com.android.myvolley.utils.BaseLogUtils;
import com.android.myvolley.volley.Request;
import com.android.myvolley.volley.Request.Method;
import com.android.myvolley.volley.RequestQueue;
import com.android.myvolley.volley.Response;
import com.android.myvolley.volley.Response.ErrorListener;
import com.android.myvolley.volley.VolleyError;
import com.android.myvolley.volley.toolbox.Volley;

public abstract class NetworkHelper<T> implements Response.Listener<JSONObject>, ErrorListener {
    private Context context;

    public NetworkHelper(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    protected NetworkRequest getRequestForGet(String url, List<NameValuePair> params) {
        if (params == null) {
            return new NetworkRequest(url, this, this);
        } else {
            return new NetworkRequest(url, params, this, this);
        }
    }

    protected NetworkRequest getRequestForPost(String url, Map<String, String> params) {
        return new NetworkRequest(Method.POST, url, params, this, this);
    }

    protected NetworkRequest getRequestForPatch(String url, Map<String, String> params) {
        return new NetworkRequest(Method.PATCH, url, params, this, this);
    }

    protected NetworkRequest getRequestForPut(String url, Map<String, String> params) {
        return new NetworkRequest(Method.PUT, url, params, this, this);
    }

    protected NetworkRequest getRequestForDelete(String url, Map<String, String> params) {
        return new NetworkRequest(Method.DELETE, url, params, this, this);
    }

    public void sendGETRequest(String url, List<NameValuePair> params, Object tag) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        NetworkRequest request = getRequestForGet(url, params);
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void sendPostRequest(String url, Map<String, String> params, Object tag) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        NetworkRequest request = getRequestForPost(url, params);
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void sendPostRequest(String url, Map<String, String> params, Object tag, int headType) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        NetworkRequest request = getRequestForPost(url, params);
        request.setHeadType(1);
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void sendPatchRequest(String url, Map<String, String> params, Object tag) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        NetworkRequest request = getRequestForPatch(url, params);
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void sendDeleteRequest(String url, Map<String, String> params, Object tag) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        NetworkRequest request = getRequestForDelete(url, params);
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void sendPutRequest(String url, Map<String, String> params, Object tag) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        NetworkRequest request = getRequestForPut(url, params);
        request.setTag(tag);
        requestQueue.add(request);
    }

    @Override
    public void onErrorResponse(VolleyError error, Object tag) {
        BaseLogUtils.logOnResponseError(error, tag);
        Request tagEnd = (Request) tag;
        if (error.getMessage() != null)
            BaseLogUtils.d("cccc", error.getMessage());
        // token失效，需要再次登录
        // if (error != null && error.networkResponse != null &&
        // error.networkResponse.statusCode == 401) {
        // Intent intent = new Intent();
        //
        // BaseToast.showShort(context, "登录状态失效，请重新登录");
        // context.startActivity(intent);
        // return;
        // }
        disposeVolleyError(error, tagEnd.getTag());
    }

    protected abstract void disposeVolleyError(VolleyError error, Object tag);

    @Override
    public void onResponse(JSONObject response, Object tag) {
        BaseLogUtils.logOnResponse(response, tag);
        Request tagEnd = (Request) tag;
        disposeResponse(response, tagEnd.getTag());
    }

    protected abstract void disposeResponse(JSONObject response, Object tag);

    private UIDataListener<T> uiDataListener;

    public void setUiDataListener(UIDataListener<T> uiDataListener) {
        this.uiDataListener = uiDataListener;
    }

    protected void notifyDataChanged(T data, Object tag) {
        if (uiDataListener != null) {
            uiDataListener.onDataChanged(data, tag);
        }
    }

    protected void notifyErrorHappened(String errorCode, String errorMessage, Object tag, String data) {
        if (errorCode.equals("VOLLEY_ERROR_CODE")) {
            // com.kalemao.thalassa.com.kalemao.thalassa.utils.T.showShort(context,
            // "小喵说网络不给力哦，稍后再试");
            if (uiDataListener != null) {
                if (BaseComFunc.isNetworkAvailable(context)) {
                    uiDataListener.onErrorHappened(errorCode, "小喵说网络不给力哦，稍后再试", tag, data);
                } else {
                    uiDataListener.onErrorHappened(errorCode, "网络连接异常，请检测网络", tag, data);
                }
            }
            return;
        }
        if (uiDataListener != null) {
            uiDataListener.onErrorHappened(errorCode, errorMessage, tag, data);
        }
    }

}
