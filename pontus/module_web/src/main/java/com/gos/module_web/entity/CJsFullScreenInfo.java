package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-21
 * 更改时间：2019-07-21
 * 版本号：1
 */
public class CJsFullScreenInfo extends CJsCallBack {

    // 是否是全屏
    private boolean isFullScreen;
    // 是否全屏遮挡住toolbar
    private boolean isCoverToolbar;

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    public boolean isCoverToolbar() {
        return isCoverToolbar;
    }

    public void setCoverToolbar(boolean coverToolbar) {
        isCoverToolbar = coverToolbar;
    }
}
