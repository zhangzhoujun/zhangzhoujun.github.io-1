package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-10-28
 * 更改时间：2019-10-28
 * 版本号：1
 */
public class CJsDownLoad extends CJsCallBack {

    private String downloadUrl;
    private String downloadName;
    private String fileSuffix;
    private double totalSize;

    public String getFileSuffix() {
        return fileSuffix.startsWith(".") ? fileSuffix : "." + fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public double getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(double totalSize) {
        this.totalSize = totalSize;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public void setDownloadName(String downloadName) {
        this.downloadName = downloadName;
    }
}
