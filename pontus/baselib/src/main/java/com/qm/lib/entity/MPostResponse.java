package com.qm.lib.entity;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 
 * @author 张舟俊 阿里云文件传成功后返回的xml数据 2015年10月14日上午11:30:21
 */

@Root(name = "PostResponse", strict = false) //name:要解析的xml数据的头部
public class MPostResponse {
    @Element(name = "Bucket")
    public String Bucket;

    @Element(name = "Location")
    public String Location;

    @Element(name = "Key")
    public String Key;

    @Element(name = "ETag")
    public String ETag;

    public boolean success;


    public String getBucket() {
        return Bucket;
    }

    public void setBucket(String bucket) {
        Bucket = bucket;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getETag() {
        return ETag;
    }

    public void setETag(String ETag) {
        this.ETag = ETag;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
