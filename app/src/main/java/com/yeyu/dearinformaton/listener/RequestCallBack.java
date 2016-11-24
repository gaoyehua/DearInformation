package com.yeyu.dearinformaton.listener;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public interface RequestCallBack<T> {

    void beforeRequest();

    void success(T data);

    void onError(String errorMsg);
}
