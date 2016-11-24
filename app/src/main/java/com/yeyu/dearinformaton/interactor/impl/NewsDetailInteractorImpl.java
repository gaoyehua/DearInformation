package com.yeyu.dearinformaton.interactor.impl;

import com.socks.library.KLog;
import com.yeyu.dearinformaton.Constant.HostType;
import com.yeyu.dearinformaton.MyApplication;
import com.yeyu.dearinformaton.entity.NewsDetail;
import com.yeyu.dearinformaton.interactor.NewsDetailInteractor;
import com.yeyu.dearinformaton.listener.RequestCallBack;
import com.yeyu.dearinformaton.respository.network.RetrofitManager;
import com.yeyu.dearinformaton.utils.MyUtils;
import com.yeyu.dearinformaton.utils.TransformUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class NewsDetailInteractorImpl implements NewsDetailInteractor<NewsDetail> {

    @Inject
    public NewsDetailInteractorImpl() {
    }

    @Override
    public Subscription loadNewsDetail(final RequestCallBack<NewsDetail> callBack, final String postId) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getNewsDetailObservable(postId)
                .map(new Func1<Map<String, NewsDetail>, NewsDetail>() {
                    @Override
                    public NewsDetail call(Map<String, NewsDetail> map) {
                        KLog.d(Thread.currentThread().getName());

                        NewsDetail newsDetail = map.get(postId);
                        changeNewsDetail(newsDetail);
                        return newsDetail;
                    }
                })
                .compose(TransformUtils.<NewsDetail>defaultSchedulers())
                .subscribe(new Observer<NewsDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e(e.toString());
                        callBack.onError(MyUtils.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(NewsDetail newsDetail) {
                        callBack.success(newsDetail);
                    }
                });
    }

    private void changeNewsDetail(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        if (isChange(imgSrcs)) {
            String newsBody = newsDetail.getBody();
            newsBody = changeNewsBody(imgSrcs, newsBody);
            newsDetail.setBody(newsBody);
        }
    }

    private boolean isChange(List<NewsDetail.ImgBean> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 2 && MyApplication.isHavePhoto();
    }

    private String changeNewsBody(List<NewsDetail.ImgBean> imgSrcs, String newsBody) {
        for (int i = 0; i < imgSrcs.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);

        }
        return newsBody;
    }
}
