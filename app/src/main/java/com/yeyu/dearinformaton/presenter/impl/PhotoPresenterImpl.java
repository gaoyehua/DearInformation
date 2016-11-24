package com.yeyu.dearinformaton.presenter.impl;

import com.yeyu.dearinformaton.Constant.LoadNewsType;
import com.yeyu.dearinformaton.View.PhotoView;
import com.yeyu.dearinformaton.entity.PhotoGirl;
import com.yeyu.dearinformaton.interactor.impl.PhotoInteractorImpl;
import com.yeyu.dearinformaton.listener.RequestCallBack;
import com.yeyu.dearinformaton.presenter.BasePresenter.BasePresenterImpl;
import com.yeyu.dearinformaton.presenter.PhotoPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by gaoyehua on 2016/9/11.
 */
public class PhotoPresenterImpl extends BasePresenterImpl<PhotoView, List<PhotoGirl>>
        implements PhotoPresenter, RequestCallBack<List<PhotoGirl>> {
    private PhotoInteractorImpl mPhotoInteractor;
    private static int SIZE = 20;
    private int mStartPage = 1;
    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public PhotoPresenterImpl(PhotoInteractorImpl photoInteractor) {
        mPhotoInteractor = photoInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadPhotoData();
    }

    @Override
    public void beforeRequest() {
        if (!misFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {
            int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_ERROR : LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setPhotoList(null, loadType);
        }
    }

    @Override
    public void success(List<PhotoGirl> items) {
        super.success(items);
        misFirstLoad = true;
        if (items != null) {
            mStartPage += 1;
        }

        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setPhotoList(items, loadType);
            mView.hideProgress();
        }
    }

    @Override
    public void refreshData() {
        mStartPage = 1;
        mIsRefresh = true;
        loadPhotoData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadPhotoData();
    }

    private void loadPhotoData() {
        mPhotoInteractor.loadPhotos(this, SIZE, mStartPage);
    }
}

