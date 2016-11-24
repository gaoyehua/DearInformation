package com.yeyu.dearinformaton.interactor;

import com.yeyu.dearinformaton.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by gaoyehua on 2016/9/11.
 */
public interface PhotoDetailInteractor<T> {
    Subscription saveImageAndGetImageUri(RequestCallBack<T> listener, String url);
}
