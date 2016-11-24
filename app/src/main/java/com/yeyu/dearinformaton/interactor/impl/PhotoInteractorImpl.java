package com.yeyu.dearinformaton.interactor.impl;

import com.yeyu.dearinformaton.Constant.HostType;
import com.yeyu.dearinformaton.MyApplication;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.entity.GirlData;
import com.yeyu.dearinformaton.entity.PhotoGirl;
import com.yeyu.dearinformaton.interactor.PhotoInteractor;
import com.yeyu.dearinformaton.listener.RequestCallBack;
import com.yeyu.dearinformaton.respository.network.RetrofitManager;
import com.yeyu.dearinformaton.utils.TransformUtils;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by gaoyehua on 2016/9/11.
 */
public class PhotoInteractorImpl implements PhotoInteractor<List<PhotoGirl>> {

    @Inject
    public PhotoInteractorImpl() {
    }

    @Override
    public Subscription loadPhotos(final RequestCallBack<List<PhotoGirl>> listener, int size, int page) {
        return RetrofitManager.getInstance(HostType.GANK_GIRL_PHOTO)
                .getPhotoListObservable(size, page)
                .map(new Func1<GirlData, List<PhotoGirl>>() {
                    @Override
                    public List<PhotoGirl> call(GirlData girlData) {
                        return girlData.getResults();
                    }
                })
                .compose(TransformUtils.<List<PhotoGirl>>defaultSchedulers())
                .subscribe(new Subscriber<List<PhotoGirl>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(MyApplication.getAppContext().getString(R.string.load_error));
                    }

                    @Override
                    public void onNext(List<PhotoGirl> photoGirls) {
                        listener.success(photoGirls);
                    }
                })
                ;
    }
}

