package com.qm.lib.entity;

/**
 * @ClassName IBXInitMessage
 * @Description TODO
 * @Author zhangzhoujun
 * @Date 2020/9/21 2:19 PM
 * @Version 1.0
 */
public class DownloadAppMessage {

    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public DownloadAppMessage(String packageName) {
        this.packageName = packageName;
    }
}
