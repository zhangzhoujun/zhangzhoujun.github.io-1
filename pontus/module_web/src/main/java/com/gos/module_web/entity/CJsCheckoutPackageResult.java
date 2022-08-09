package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2020/4/9
 * 更改时间：2020/4/9
 * 版本号：1
 */
public class CJsCheckoutPackageResult {

    private boolean hasApk;

    public boolean isHasApk() {
        return hasApk;
    }

    public void setHasApk(boolean hasApk) {
        this.hasApk = hasApk;
    }

    public CJsCheckoutPackageResult(boolean hasApk) {
        this.hasApk = hasApk;
    }
}
