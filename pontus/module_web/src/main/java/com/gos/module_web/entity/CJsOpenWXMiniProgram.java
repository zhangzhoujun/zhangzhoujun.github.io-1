package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2020/4/7
 * 更改时间：2020/4/7
 * 版本号：1
 */
public class CJsOpenWXMiniProgram extends CJsCallBack {

    private String userName;
    private String path;
    // WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE
    private int miniprogramType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getMiniprogramType() {
        return miniprogramType;
    }

    public void setMiniprogramType(int miniprogramType) {
        this.miniprogramType = miniprogramType;
    }
}
