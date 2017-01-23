package com.android.myvolley.model;

import java.io.Serializable;

public class MResponseBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private String biz_action;
    private String biz_msg;
    private String return_status;
    private String data;
    private String all_data;
    private String server_time;

    public MResponseBase() {
        super();
        this.biz_action = "";
        this.biz_msg = "";
        this.return_status = "";
        this.data = "";
        this.server_time="";
    }

    public String getBiz_action() {
        return biz_action;
    }

    public void setBiz_action(String biz_action) {
        this.biz_action = biz_action;
    }

    public String getBiz_msg() {
        return biz_msg==null?"":biz_msg;
    }

    public void setBiz_msg(String biz_msg) {
        this.biz_msg = biz_msg;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

	public String getAll_data() {
		return all_data;
	}

	public void setAll_data(String all_data) {
		this.all_data = all_data;
	}

	public String getServer_time() {
		return server_time;
	}

	public void setServer_time(String server_time) {
		this.server_time = server_time;
	}
    
    

}
