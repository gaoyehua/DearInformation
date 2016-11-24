package com.yeyu.dearinformaton.interactor;

import com.yeyu.dearinformaton.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public interface NewsListInteractor<T> {

    Subscription loadNews(RequestCallBack<T> listener, String type, String id, int startPage);
}
