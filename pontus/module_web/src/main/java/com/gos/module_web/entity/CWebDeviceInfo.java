package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-20
 * 更改时间：2019-07-20
 * 版本号：1
 */
public class CWebDeviceInfo extends CAppSuccess {

    // brand:'华为'、model:'P20'、sysVersion:'9.0'、deviceId:'UWDsfer123'

    private String brand;
    private String model;
    private String sysVersion;
    private String deviceId;
    private String appVersion;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public CWebDeviceInfo(String brand, String model, String sysVersion, String deviceId, String app_version) {
        this.brand = brand;
        this.model = model;
        this.sysVersion = sysVersion;
        this.deviceId = deviceId;
        this.appVersion = app_version;
    }

    @Override
    public String toString() {
        return "CWebDeviceInfo{" +
            "brand='" + brand + '\'' +
            ", model='" + model + '\'' +
            ", sysVersion='" + sysVersion + '\'' +
            ", deviceId='" + deviceId + '\'' +
            ", appVersion='" + appVersion + '\'' +
            '}';
    }
}
