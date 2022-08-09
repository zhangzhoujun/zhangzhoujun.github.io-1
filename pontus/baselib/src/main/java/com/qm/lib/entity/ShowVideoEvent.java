package com.qm.lib.entity;

/**
 * @author 作者 luohl
 * @time 创建时间：2019/9/14
 * @explain 类说明:
 */
public class ShowVideoEvent {

    private String id = "";
    private String type = "";
    private String CSJ_VIDEO_ID = "";
    private String GDT_VIDEO_ID = "";
    private String KS_VIDEO_ID = "";
    private boolean showDialog = false;

    public ShowVideoEvent(String CSJ_VIDEO_ID, String GDT_VIDEO_ID, String KS_VIDEO_ID, boolean showDialog) {
        this.CSJ_VIDEO_ID = CSJ_VIDEO_ID;
        this.GDT_VIDEO_ID = GDT_VIDEO_ID;
        this.KS_VIDEO_ID = KS_VIDEO_ID;
        this.showDialog = showDialog;
    }

    public String getCSJ_VIDEO_ID() {
        return CSJ_VIDEO_ID;
    }

    public void setCSJ_VIDEO_ID(String CSJ_VIDEO_ID) {
        this.CSJ_VIDEO_ID = CSJ_VIDEO_ID;
    }

    public String getGDT_VIDEO_ID() {
        return GDT_VIDEO_ID;
    }

    public void setGDT_VIDEO_ID(String GDT_VIDEO_ID) {
        this.GDT_VIDEO_ID = GDT_VIDEO_ID;
    }

    public String getKS_VIDEO_ID() {
        return KS_VIDEO_ID;
    }

    public void setKS_VIDEO_ID(String KS_VIDEO_ID) {
        this.KS_VIDEO_ID = KS_VIDEO_ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShowVideoEvent(String id, String type, boolean showDialog) {
        this.id = id;
        this.type = type;
        this.showDialog = showDialog;
    }
}
