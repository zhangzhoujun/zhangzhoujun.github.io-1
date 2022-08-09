package com.qm.lib.utils;

/**
 * @author dim
 * @create at 2019/3/21 11:36
 * @description:
 */
public class RuntimeData {
    private static final RuntimeData ourInstance = new RuntimeData();

    public static RuntimeData getInstance() {
        return ourInstance;
    }

    private RuntimeData() {
    }

    private String httpUrl;
    private String webHost;
    private String ApplicationId;
    private String wxAppId;
    private String tPushToken;
    // 黑名单不给看
    private Boolean isHideBlock = false;
    private Boolean isMustLogin = false;

    public Boolean getMustLogin() {
        return isMustLogin;
    }

    public void setMustLogin(Boolean mustLogin) {
        isMustLogin = mustLogin;
    }

    public String gettPushToken() {
        return tPushToken;
    }

    public void settPushToken(String tPushToken) {
        this.tPushToken = tPushToken;
    }

    public Boolean getHideBlock() {
        return isHideBlock;
    }

    public void setHideBlock(Boolean hideBlock) {
        isHideBlock = hideBlock;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getWebHost() {
        return webHost;
    }

    public void setWebHost(String webHost) {
        this.webHost = webHost;
    }

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }
}
