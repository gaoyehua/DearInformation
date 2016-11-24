package com.yeyu.dearinformaton.interactor.impl;

import com.yeyu.dearinformaton.GreenDao.NewsChannelTable;
import com.yeyu.dearinformaton.MyApplication;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.interactor.NewsInteractor;
import com.yeyu.dearinformaton.listener.RequestCallBack;
import com.yeyu.dearinformaton.respository.NewsChannelTableManager;
import com.yeyu.dearinformaton.utils.TransformUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class NewsInteractorImpl implements NewsInteractor<List<NewsChannelTable>> {

    @Inject
    public NewsInteractorImpl() {
    }

    @Override
    public Subscription lodeNewsChannels(final RequestCallBack<List<NewsChannelTable>> callback) {
        return Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                NewsChannelTableManager.initDB();
                subscriber.onNext(NewsChannelTableManager.loadNewsChannelsMine());
                subscriber.onCompleted();
            }
        })
                .compose(TransformUtils.<List<NewsChannelTable>>defaultSchedulers())
                .subscribe(new Subscriber<List<NewsChannelTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(MyApplication.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannelTable> newsChannelTables) {
                        callback.success(newsChannelTables);
                    }
                });
    }
}

