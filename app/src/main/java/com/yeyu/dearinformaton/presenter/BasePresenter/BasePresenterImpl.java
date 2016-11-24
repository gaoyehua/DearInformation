package com.yeyu.dearinformaton.presenter.BasePresenter;

import android.support.annotation.NonNull;

import com.yeyu.dearinformaton.View.BaseView.BaseView;
import com.yeyu.dearinformaton.listener.RequestCallBack;
import com.yeyu.dearinformaton.utils.MyUtils;

import rx.Subscription;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class BasePresenterImpl<T extends BaseView, E> implements BasePresenter, RequestCallBack<E> {
    protected T mView;
    protected Subscription mSubscription;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        MyUtils.cancelSubscription(mSubscription);
    }

    @Override
    public void attachView(@NonNull BaseView view) {
        // TODO?
        mView = (T) view;
    }

    @Override
    public void beforeRequest() {
        mView.showProgress();
    }

    @Override
    public void success(E data) {
        mView.hideProgress();
    }

    @Override
    public void onError(String errorMsg) {
        mView.hideProgress();
        mView.showMsg(errorMsg);
    }

}
