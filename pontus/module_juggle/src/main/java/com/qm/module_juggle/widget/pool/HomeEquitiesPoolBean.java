package com.qm.module_juggle.widget.pool;

import java.util.ArrayList;

public class HomeEquitiesPoolBean {

    private String num;
    private ArrayList<EquitiesPollItem> nt_list;

    public HomeEquitiesPoolBean() {
    }

    public HomeEquitiesPoolBean(String num, ArrayList<EquitiesPollItem> nt_list) {
        this.num = num;
        this.nt_list = nt_list;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public ArrayList<EquitiesPollItem> getNt_list() {
        return nt_list;
    }

    public void setNt_list(ArrayList<EquitiesPollItem> nt_list) {
        this.nt_list = nt_list;
    }
}
