package com.yeyu.dearinformaton.View;

import com.yeyu.dearinformaton.Constant.LoadNewsType;
import com.yeyu.dearinformaton.View.BaseView.BaseView;
import com.yeyu.dearinformaton.entity.NewsSummary;

import java.util.List;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public interface NewsListView extends BaseView {

    void setNewsList(List<NewsSummary> newsSummary, @LoadNewsType.checker int loadType);
}
