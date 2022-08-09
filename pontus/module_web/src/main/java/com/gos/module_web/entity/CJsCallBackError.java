package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-20
 * 更改时间：2019-07-20
 * 版本号：1
 */
public class CJsCallBackError {

    /**
     * 10003:app找不到数据
     * 10005：js传参有问题，不支持解析或者解析失败
     */
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CJsCallBackError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
