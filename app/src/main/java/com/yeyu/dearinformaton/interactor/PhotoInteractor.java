package com.yeyu.dearinformaton.interactor;

import com.yeyu.dearinformaton.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by gaoyehua on 2016/9/11.
 */
public interface PhotoInteractor<T> {
    Subscription loadPhotos(RequestCallBack<T> listener, int size, int page);
}
