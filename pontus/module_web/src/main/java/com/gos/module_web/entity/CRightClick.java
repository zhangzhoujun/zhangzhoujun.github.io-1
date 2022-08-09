package com.gos.module_web.entity;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2020/3/3
 * 更改时间：2020/3/3
 * 版本号：1
 */
public class CRightClick extends CAppSuccess {

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public CRightClick(String key) {
        this.key = key;
    }
}
