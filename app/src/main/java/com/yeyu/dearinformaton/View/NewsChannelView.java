package com.yeyu.dearinformaton.View;

import com.yeyu.dearinformaton.GreenDao.NewsChannelTable;
import com.yeyu.dearinformaton.View.BaseView.BaseView;

import java.util.List;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public interface NewsChannelView extends BaseView {
    void initRecyclerViews(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore);
}

