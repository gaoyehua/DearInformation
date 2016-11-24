package com.yeyu.dearinformaton.presenter.impl;

import com.yeyu.dearinformaton.GreenDao.NewsChannelTable;
import com.yeyu.dearinformaton.View.NewsView;
import com.yeyu.dearinformaton.interactor.NewsInteractor;
import com.yeyu.dearinformaton.interactor.impl.NewsInteractorImpl;
import com.yeyu.dearinformaton.presenter.BasePresenter.BasePresenterImpl;
import com.yeyu.dearinformaton.presenter.NewsPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class NewPresenterImpl extends BasePresenterImpl<NewsView, List<NewsChannelTable>>
        implements NewsPresenter {

    private NewsInteractor<List<NewsChannelTable>> mNewsInteractor;

    @Inject
    public NewPresenterImpl(NewsInteractorImpl newsInteractor) {
        mNewsInteractor = newsInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadNewsChannels();
    }

    private void loadNewsChannels() {
        mSubscription = mNewsInteractor.lodeNewsChannels(this);
    }

    @Override
    public void success(List<NewsChannelTable> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    @Override
    public void onChannelDbChanged() {
        loadNewsChannels();
    }
}
