package com.gos.module_web.entity;

import com.dim.library.utils.Utils;
import com.qm.lib.utils.JYUtils;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-20
 * 更改时间：2019-07-20
 * 版本号：1
 */
public class CWebUsers extends CAppSuccess {

    private String authToken;
    private String userId;
    private String mobile;
    private String appName;
    private String appKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public CWebUsers(String authToken, String userId, String mobile, String appKey) {
        this.authToken = authToken;
        this.userId = userId;
        this.mobile = mobile;
        this.appKey = appKey;
        this.appName = JYUtils.Companion.getInstance().getMetaDatByName(Utils.getContext(), "QY_APP_NAME");
    }
}
