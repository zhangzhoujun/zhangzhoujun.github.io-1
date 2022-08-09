package com.qm.module_juggle.widget.pool;


import com.qm.module_juggle.entity.MHomeDataBean;

public class EquitiesPollItem {

    private String name;
    private String nt_amount;   // 接口数据
    private String stage;       // 接口数据
    private boolean isDefault;
    // isDefault = true的时候使用的类型
    private int type;           // 0: 默认球,邀请那个, 1: 右上球1，2：1: 右上球2, 3：1: 右上球3 ，4 ：邀请下面的球
    private String url;         // 跳转url

    private MHomeDataBean.MHomeDataItemData item;

    public EquitiesPollItem() {
    }

    public EquitiesPollItem(String name, String nt_amount, String stage, boolean isDefault) {
        this.name = name;
        this.nt_amount = nt_amount;
        this.stage = stage;
        this.isDefault = isDefault;
    }

    public MHomeDataBean.MHomeDataItemData getItem() {
        return item;
    }

    public void setItem(MHomeDataBean.MHomeDataItemData item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNt_amount() {
        return nt_amount;
    }

    public void setNt_amount(String nt_amount) {
        this.nt_amount = nt_amount;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
