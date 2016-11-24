package com.yeyu.dearinformaton.presenter.impl;

import com.yeyu.dearinformaton.View.NewsDetailView;
import com.yeyu.dearinformaton.entity.NewsDetail;
import com.yeyu.dearinformaton.interactor.NewsDetailInteractor;
import com.yeyu.dearinformaton.interactor.impl.NewsDetailInteractorImpl;
import com.yeyu.dearinformaton.presenter.BasePresenter.BasePresenterImpl;
import com.yeyu.dearinformaton.presenter.NewsDetailPresenter;

import javax.inject.Inject;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class NewsDetailPresenterImpl extends BasePresenterImpl<NewsDetailView, NewsDetail>
        implements NewsDetailPresenter {
    private NewsDetailInteractor<NewsDetail> mNewsDetailInteractor;
    private String mPostId;

    @Inject
    public NewsDetailPresenterImpl(NewsDetailInteractorImpl newsDetailInteractor) {
        mNewsDetailInteractor = newsDetailInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSubscription = mNewsDetailInteractor.loadNewsDetail(this, mPostId);

    }

    @Override
    public void success(NewsDetail data) {
        super.success(data);
        mView.setNewsDetail(data);
    }

    @Override
    public void setPosId(String postId) {
        mPostId = postId;
    }
}