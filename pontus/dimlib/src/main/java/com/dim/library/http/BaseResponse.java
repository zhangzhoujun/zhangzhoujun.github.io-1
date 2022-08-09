package com.dim.library.http;

/**
 * @author dim
 * @create at 2019/3/15 14:26
 * @description:
 */
public class BaseResponse<T> {

    private int code;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isOk(){
        return code == 0;
    }
}
