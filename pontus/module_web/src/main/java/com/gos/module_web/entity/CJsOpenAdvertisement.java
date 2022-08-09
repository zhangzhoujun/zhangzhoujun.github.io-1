package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2020/4/7
 * 更改时间：2020/4/7
 * 版本号：1
 */
public class CJsOpenAdvertisement extends CJsCallBack {

    private String type;
    private CJSVideo data;
    private String id;

    public CJSVideo getData() {
        return data;
    }

    public void setData(CJSVideo data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    class CJSVideo{
        private String CSJ_VIDEO_ID;
        private String GDT_VIDEO_ID;
        private String KS_VIDEO_ID;
    }
}
