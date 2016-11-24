package com.yeyu.dearinformaton.presenter;

import com.yeyu.dearinformaton.presenter.BasePresenter.BasePresenter;

/**
 * Created by gaoyehua on 2016/9/11.
 */
public interface PhotoPresenter extends BasePresenter {
    void refreshData();

    void loadMore();
}