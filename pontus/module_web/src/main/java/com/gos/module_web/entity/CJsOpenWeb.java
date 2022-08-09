package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-21
 * 更改时间：2019-07-21
 * 版本号：1
 */
public class CJsOpenWeb extends CJsCallBack {

    private String url;
    private boolean isNeedReFresh;

    public boolean isNeedReFresh() {
        return isNeedReFresh;
    }

    public void setNeedReFresh(boolean needReFresh) {
        isNeedReFresh = needReFresh;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
