package com.kalemao.library.base;

import java.util.HashMap;

import android.content.Context;

/**
 * Created by dim on 2017/5/22 16:00 邮箱：271756926@qq.com
 */

public class RunTimeData {
    protected static RunTimeData    runTimeData;

    private HashMap<String, String> httpHead;
    private Context                 mContext;
    private String                  baseErrorMessage = "小喵说网络不给力哦，稍后再试";

    public static RunTimeData getInstance() {
        if (runTimeData == null) {
            runTimeData = new RunTimeData();
        }
        return runTimeData;
    }

    public HashMap<String, String> getHttpHead() {
        return httpHead == null ? new HashMap<>() : httpHead;
    }

    public void setHttpHead(HashMap<String, String> httpHead) {
        this.httpHead = httpHead;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getBaseErrorMessage() {
        return baseErrorMessage;
    }

    public void setBaseErrorMessage(String baseErrorMessage) {
        this.baseErrorMessage = baseErrorMessage;
    }
}
