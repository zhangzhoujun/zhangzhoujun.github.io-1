package com.qm.lib.widget.toolbar;

/**
 * @author dim
 * @create at 2019/3/12 13:51
 * @description:
 */
public class JYToolbarOptions {

    /**
     * toolbar的title
     */
    private String titleString;
    /**
     * toolbar的返回按钮资源id
     */
    private int backId;
    /**
     * toolbar的返回按钮
     */
    private boolean isNeedNavigate;
    /**
     * toolbar的右边按钮资源id
     */
    private int rightId;
    private String rightIconUrl;
    /**
     * toolbar的右边第二按钮资源id
     */
    private int rightLeftId;

    /**
     * toolbar的背景色
     *
     * @return
     */

    private int bgColor = 0;
    /**
     * toolbar的右边的文字
     */
    private String rightString;
    private int rightStringColor = 0;

    /**
     * 标题的颜色
     */
    private int titleColor = 0;

    /**
     * 是否显示关闭按钮
     */
    private boolean doesShowClose;

    public int getRightStringColor() {
        return rightStringColor;
    }

    public void setRightStringColor(int rightStringColor) {
        this.rightStringColor = rightStringColor;
    }

    public String getRightIconUrl() {
        return rightIconUrl;
    }

    public void setRightIconUrl(String rightIconUrl) {
        this.rightIconUrl = rightIconUrl;
    }

    public boolean isDoesShowClose() {
        return doesShowClose;
    }

    public void setDoesShowClose(boolean doesShowClose) {
        this.doesShowClose = doesShowClose;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public String getRightString() {
        return rightString;
    }

    public void setRightString(String rightString) {
        this.rightString = rightString;
    }

    public int getBackId() {
        return backId;
    }

    public void setBackId(int backId) {
        this.backId = backId;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getRightId() {
        return rightId;
    }

    public void setRightId(int rightId) {
        this.rightId = rightId;
    }

    public int getRightLeftId() {
        return rightLeftId;
    }

    public void setRightLeftId(int rightLeftId) {
        this.rightLeftId = rightLeftId;
    }

    public String getTitleString() {
        return titleString;
    }

    public void setTitleString(String titleString) {
        this.titleString = titleString;
    }

    public boolean isNeedNavigate() {
        return isNeedNavigate;
    }

    public void setNeedNavigate(boolean needNavigate) {
        isNeedNavigate = needNavigate;
    }
}
