package com.android.myvolley.volley.toolbox;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.android.myvolley.volley.AuthFailureError;
import com.android.myvolley.volley.NetworkResponse;
import com.android.myvolley.volley.ParseError;
import com.android.myvolley.volley.Request;
import com.android.myvolley.volley.Response;
import com.android.myvolley.volley.Response.ErrorListener;
import com.android.myvolley.volley.Response.Listener;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by
 * Gson.
 */
public class GsonRequest<T> extends Request<T> {
    private final Gson                gson = new Gson();
    private final Class<T>            clazz;
    private final Map<String, String> headers;
    private final Listener<T>         listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url
     *            URL of the request to make
     * @param clazz
     *            Relevant class object, for Gson's reflection
     * @param headers
     *            Map of request headers
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers, Listener<T> listener,
            ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders(int headType) throws AuthFailureError {
        return headers != null ? headers : super.getHeaders(headType);
    }

    @Override
    protected void deliverResponse(T response, Object tag) {
        listener.onResponse(response, tag);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}
