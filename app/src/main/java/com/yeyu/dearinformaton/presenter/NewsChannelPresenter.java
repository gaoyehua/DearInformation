package com.yeyu.dearinformaton.presenter;

import com.yeyu.dearinformaton.GreenDao.NewsChannelTable;
import com.yeyu.dearinformaton.presenter.BasePresenter.BasePresenter;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public interface NewsChannelPresenter extends BasePresenter {
    void onItemSwap(int fromPosition, int toPosition);

    void onItemAddOrRemove(NewsChannelTable newsChannel, boolean isChannelMine);
}