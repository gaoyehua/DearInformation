package com.yeyu.dearinformaton.interactor;

import com.yeyu.dearinformaton.GreenDao.NewsChannelTable;
import com.yeyu.dearinformaton.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public interface NewsChannelInteractor<T> {
    Subscription lodeNewsChannels(RequestCallBack<T> callback);

    void swapDb(int fromPosition,int toPosition);

    void updateDb(NewsChannelTable newsChannel, boolean isChannelMine);
}
