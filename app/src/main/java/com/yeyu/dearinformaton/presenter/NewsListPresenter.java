package com.yeyu.dearinformaton.presenter;

import com.yeyu.dearinformaton.presenter.BasePresenter.BasePresenter;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public interface NewsListPresenter extends BasePresenter {
    void setNewsTypeAndId(String newsType, String newsId);

    void refreshData();

    void loadMore();
}

