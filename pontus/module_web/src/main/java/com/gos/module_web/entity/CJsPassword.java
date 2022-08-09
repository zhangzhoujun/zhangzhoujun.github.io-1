package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-25
 * 更改时间：2019-07-25
 * 版本号：1
 */
public class CJsPassword extends CAppSuccess {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CJsPassword(String password) {
        this.password = password;
    }
}
