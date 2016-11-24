package com.yeyu.dearinformaton.presenter.BasePresenter;

import android.support.annotation.NonNull;

import com.yeyu.dearinformaton.View.BaseView.BaseView;

/**
 * Created by gaoyehua on 2016/9/7.
 */
public interface BasePresenter {

//    void onResume();

    void onCreate();

    void attachView(@NonNull BaseView view);

    void onDestroy();

}
