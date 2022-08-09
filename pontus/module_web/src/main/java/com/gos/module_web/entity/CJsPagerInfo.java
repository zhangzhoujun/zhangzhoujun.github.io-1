package com.gos.module_web.entity;

import android.text.TextUtils;
import java.util.List;

/**
 * 文件描述：
 * 作者：dim
 * 创建时间：2019-07-21
 * 更改时间：2019-07-21
 * 版本号：1
 */
public class CJsPagerInfo extends CJsCallBack {

    // 页面标题
    private String title;
    // 是否下拉刷新
    private boolean isPullRefresh;
    // header背景颜色
    private String bgColor;
    // header文字颜色
    private String color;
    // 是否显示header
    private boolean hasHead = true;

    /**
     * menus : [{"key":"deleteAddress","text":"删除地址","color":"#666666"}]
     * callbackKey : js_callbackKey1583138703064_48972762
     * callback :
     */
    private String callback;
    private List<MenusBean> menus;

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public List<MenusBean> getMenus() {
        return menus;
    }

    public void setMenus(List<MenusBean> menus) {
        this.menus = menus;
    }

    public static class MenusBean {
        /**
         * key : deleteAddress
         * text : 删除地址
         * color : #666666
         */

        private String key;
        private String text;
        private String color;
        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    public String getBg_color() {
        if (TextUtils.isEmpty(bgColor)) {
            return "";
        }
        if (bgColor.startsWith("#")) {
            return bgColor;
        } else {
            return "#" + bgColor;
        }
    }

    public String getColor() {
        if (TextUtils.isEmpty(color)) {
            return "";
        }
        if (color.startsWith("#")) {
            return color;
        } else {
            return "#" + color;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPullRefresh() {
        return isPullRefresh;
    }

    public void setPullRefresh(boolean pullRefresh) {
        isPullRefresh = pullRefresh;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isHasHead() {
        return hasHead;
    }

    public void setHasHead(boolean hasHead) {
        this.hasHead = hasHead;
    }
}
