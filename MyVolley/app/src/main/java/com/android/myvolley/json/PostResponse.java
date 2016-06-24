package com.android.myvolley.json;

/**
 * 
 * @author 张舟俊 阿里云文件传成功后返回的xml数据 2015年10月14日上午11:30:21
 */
public class PostResponse {
    private String Bucket;
    private String Location;
    private String Key;
    private String ETag;

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

    public void setETag(String eTag) {
        ETag = eTag;
    }

}
