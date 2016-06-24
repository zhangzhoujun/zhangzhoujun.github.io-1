package com.android.myvolley.volleypkg;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.myvolley.volley.AuthFailureError;
import com.android.myvolley.volley.DefaultRetryPolicy;
import com.android.myvolley.volley.NetworkResponse;
import com.android.myvolley.volley.ParseError;
import com.android.myvolley.volley.Response;
import com.android.myvolley.volley.Response.ErrorListener;
import com.android.myvolley.volley.Response.Listener;
import com.android.myvolley.volley.toolbox.HttpHeaderParser;
import com.android.myvolley.volley.toolbox.JsonRequest;
import com.android.myvolley.volley.toolbox.VolleyConfig;

public class NetworkRequest extends JsonRequest<JSONObject> {
    private Priority mPriority = Priority.HIGH;

    public NetworkRequest(int method, String url, Map<String, String> postParams, Listener<JSONObject> listener, ErrorListener errorListener) {
        super(method, url, paramstoString(postParams), listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(15000, 0, VolleyConfig.DEFAULT_BACKOFF_MULT));
    }

    public NetworkRequest(String url, List<NameValuePair> params, Listener<JSONObject> listener, ErrorListener errorListener) {
        this(Method.GET, urlBuilder(url, params), null, listener, errorListener);
    }

    public NetworkRequest(String url, Listener<JSONObject> listener, ErrorListener errorListener) {
        this(Method.GET, url, null, listener, errorListener);
    }

    private static String paramstoString(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        JSONObject param = new JSONObject();
        Iterator iter = params.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (entry == null || entry.getKey() == null || entry.getValue() == null)
                continue;
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            try {
                param.put(key, value);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return param.toString();
    }

    @Override
    public Map<String, String> getHeaders(int headType) throws AuthFailureError {
        HashMap<String, String> head = new HashMap<>();
        // 设置请求头
        return head;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObject = new JSONObject(new String(response.data, "UTF-8"));
            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    private static String urlBuilder(String url, List<NameValuePair> params) {
        return url + "?" + URLEncodedUtils.format(params, "UTF-8");
    }
}