package com.dim.library.bus.event;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2020/3/19
 * 更改时间：2020/3/19
 * 版本号：1
 */
public class CRequestProtocalList {

    private boolean checkVersion;

    public boolean isCheckVersion() {
        return checkVersion;
    }

    public void setCheckVersion(boolean checkVersion) {
        this.checkVersion = checkVersion;
    }

    public CRequestProtocalList(boolean checkVersion) {
        this.checkVersion = checkVersion;
    }
}
