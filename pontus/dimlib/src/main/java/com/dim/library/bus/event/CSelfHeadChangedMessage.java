package com.dim.library.bus.event;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-11-08
 * 更改时间：2019-11-08
 * 版本号：1
 */
public class CSelfHeadChangedMessage {

    private String headUrl;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public CSelfHeadChangedMessage(String headUrl) {
        this.headUrl = headUrl;
    }
}
