package com.yeyu.dearinformaton.respository;

import com.yeyu.dearinformaton.Constant.ApiConstant;
import com.yeyu.dearinformaton.Constant.Constant;
import com.yeyu.dearinformaton.GreenDao.NewsChannelTable;
import com.yeyu.dearinformaton.GreenDao.NewsChannelTableDao;
import com.yeyu.dearinformaton.MyApplication;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.utils.MyUtils;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.dao.query.Query;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class NewsChannelTableManager {

    /**
     * 首次打开程序初始化db
     */
    public static void initDB() {
        if (!MyUtils.getSharedPreferences().getBoolean(Constant.INIT_DB, false)) {
            NewsChannelTableDao dao = MyApplication.getNewsChannelTableDao();
            List<String> channelName = Arrays.asList(MyApplication.getAppContext().getResources()
                    .getStringArray(R.array.news_channel_name));
            List<String> channelId = Arrays.asList(MyApplication.getAppContext().getResources()
                    .getStringArray(R.array.news_channel_id));
            for (int i = 0; i < channelName.size(); i++) {
                NewsChannelTable entity = new NewsChannelTable(channelName.get(i), channelId.get(i)
                        , ApiConstant.getType(channelId.get(i)), i <= 5, i, i == 0);
                dao.insert(entity);
            }
            MyUtils.getSharedPreferences().edit().putBoolean(Constant.INIT_DB, true).apply();
        }
    }

    public static List<NewsChannelTable> loadNewsChannelsMine() {
        Query<NewsChannelTable> newsChannelTableQuery = MyApplication.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(true))
                .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsMore() {
        Query<NewsChannelTable> newsChannelTableQuery = MyApplication.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(false))
                .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex).build();
        return newsChannelTableQuery.list();
    }

    public static NewsChannelTable loadNewsChannel(int position) {
        return MyApplication.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex.eq(position)).build().unique();
    }

    public static void update(NewsChannelTable newsChannelTable) {
        MyApplication.getNewsChannelTableDao().update(newsChannelTable);
    }

    public static List<NewsChannelTable> loadNewsChannelsWithin(int from, int to) {
        Query<NewsChannelTable> newsChannelTableQuery = MyApplication.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex
                        .between(from, to)).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsIndexGt(int channelIndex) {
        Query<NewsChannelTable> newsChannelTableQuery = MyApplication.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex
                        .gt(channelIndex)).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsIndexLtAndIsUnselect(int channelIndex) {
        Query<NewsChannelTable> newsChannelTableQuery = MyApplication.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex.lt(channelIndex),
                        NewsChannelTableDao.Properties.NewsChannelSelect.eq(false)).build();
        return newsChannelTableQuery.list();
    }

    public static int getAllSize() {
        return MyApplication.getNewsChannelTableDao().loadAll().size();
    }

    public static int getNewsChannelSelectSize() {
        return (int) MyApplication.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(true))
                .buildCount().count();
    }
}