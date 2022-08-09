package com.qm.lib.http;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

/**
 * @ClassName PostJsonBody
 * @Description 自定义RequestBody，解决POST请求时拦截器添加统一参数问题
 * @Author zhangzhoujun
 * @Date 2020/10/31 1:25 PM
 * @Version 1.0
 */
public class PostJsonBody extends RequestBody {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final Charset charset = Util.UTF_8;

    private String content;

    public PostJsonBody(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return JSON;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        byte[] bytes = content.getBytes(charset);
        if (bytes == null) throw new NullPointerException("content == null");
        Util.checkOffsetAndCount(bytes.length, 0, bytes.length);
        sink.write(bytes, 0, bytes.length);
    }

    public static RequestBody create(String content) {
        return new PostJsonBody(content);
    }

}
