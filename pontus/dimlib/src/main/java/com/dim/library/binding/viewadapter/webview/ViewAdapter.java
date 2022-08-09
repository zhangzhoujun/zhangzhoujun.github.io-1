package com.dim.library.binding.viewadapter.webview;

import android.text.TextUtils;
import com.tencent.smtt.sdk.WebView;

import androidx.databinding.BindingAdapter;

/**
 * Created by  on 2017/6/18.
 */
public class ViewAdapter {
    @BindingAdapter({"render"})
    public static void loadHtml(android.webkit.WebView webView, final String html) {
        if (!TextUtils.isEmpty(html)) {
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }

    @BindingAdapter({"webUrl"})
    public static void loadUrl(WebView webView, final String webUrl) {
        if (!TextUtils.isEmpty(webUrl)) {
            webView.loadUrl(webUrl);
        }
    }
}
