package com.android.myvolley.volleypkg;

public interface UIDataListener<T> {
    public void onDataChanged(T data, Object tag);

    public void onErrorHappened(String errorCode, String errorMessage, Object tag, String data);

}
