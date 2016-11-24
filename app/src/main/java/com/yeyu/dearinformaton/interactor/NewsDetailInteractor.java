package com.yeyu.dearinformaton.interactor;

import com.yeyu.dearinformaton.listener.RequestCallBack;

import rx.Subscription;

/**
 *
 * Created by gaoyehua on 2016/9/8.
 */
public interface NewsDetailInteractor<T> {
    Subscription loadNewsDetail(RequestCallBack<T> callBack, String postId);
}
