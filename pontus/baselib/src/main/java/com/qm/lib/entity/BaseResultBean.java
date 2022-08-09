package com.qm.lib.entity;

/**
 * Created by dim on 2019/3/6
 */
public class BaseResultBean<T> {

    private boolean success;
    private T data;
    private String time;
    private int error_code;
    private Object error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public String getErrMsg() {
        if (error instanceof String) {
            return error.toString();
        }
        return "";
    }

    public boolean doesSuccess() {
        return success;
    }
}
