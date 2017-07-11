package com.kalemao.library.logutils.okhttplog;

import com.kalemao.library.logutils.LogUtil;

/**
 * Created by dim on 2017/5/22 15:07
 * 邮箱：271756926@qq.com
 */

public class LogInterceptor implements KLMHttpLoggingInterceptor.Logger{
    public static String INTERCEPTOR_TAG_STR = "OkHttp";

    public LogInterceptor() {
    }

    public LogInterceptor(String tag) {
        INTERCEPTOR_TAG_STR = tag;
    }

    @Override public void log(String message, @LogUtil.LogType int type) {
        LogUtil.printLog(false, type, INTERCEPTOR_TAG_STR, message);
    }
}
