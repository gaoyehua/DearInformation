package com.yeyu.dearinformaton.fragment.zhihuFragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yeyu.dearinformaton.Constant.zhihu.Before;
import com.yeyu.dearinformaton.Constant.zhihu.Latest;
import com.yeyu.dearinformaton.Constant.zhihu.StoriesEntity;
import com.yeyu.dearinformaton.Constant.zhihu.zhihuConstant;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.View.zhihu.Kanner;
import com.yeyu.dearinformaton.activity.zhihu.LatestContentActivity;
import com.yeyu.dearinformaton.activity.zhihu.ZhihuActivity;
import com.yeyu.dearinformaton.adapter.zhihu.MainNewsItemAdapter;
import com.yeyu.dearinformaton.utils.HttpUtils;
import com.yeyu.dearinformaton.utils.PreUtils;

import java.util.List;

/**
 * Created by gaoyehua on 2016/9/18.
 */
public class ZhihuFragment extends BaseZhihuFragment {
    public ListView lv_news;
    private MainNewsItemAdapter mAdapter;
    private Latest latest;
    private Before before;
    private Kanner kanner;
    private String date;
    private boolean isLoading = false;
    private Handler handler = new Handler();

    private FloatingActionButton mfab;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((ZhihuActivity) mActivity).setToolbarTitle("今日热闻");
        View view = inflater.inflate(R.layout.zhihu_news_layout, container, false);

//        mfab =(FloatingActionButton) view.findViewById(R.id.fab);
//        mfab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lv_news.setSelection(0);
//            }
//        });

        lv_news = (ListView) view.findViewById(R.id.lv_news);
        View header = inflater.inflate(R.layout.kanner, lv_news, false);
        kanner = (Kanner) header.findViewById(R.id.kanner);
        kanner.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void click(View v, Latest.TopStoriesEntity entity) {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                StoriesEntity storiesEntity = new StoriesEntity();
                storiesEntity.setId(entity.getId());
                storiesEntity.setTitle(entity.getTitle());
                Intent intent = new Intent(mActivity, LatestContentActivity.class);
                intent.putExtra(zhihuConstant.START_LOCATION, startingLocation);
                intent.putExtra("entity", storiesEntity);
                intent.putExtra("isLight", ((ZhihuActivity) mActivity).isLight());
                startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });
        lv_news.addHeaderView(header);
        mAdapter = new MainNewsItemAdapter(mActivity);
        lv_news.setAdapter(mAdapter);
        lv_news.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lv_news != null && lv_news.getChildCount() > 0) {
                    boolean enable = (firstVisibleItem == 0) && (view.getChildAt(firstVisibleItem).getTop() == 0);
                    ((ZhihuActivity) mActivity).setSwipeRefreshEnable(enable);

                    if (firstVisibleItem + visibleItemCount == totalItemCount && !isLoading) {
                        loadMore(zhihuConstant.BEFORE + date);
                    }
                }

            }
        });
        lv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth() / 2;
                StoriesEntity entity = (StoriesEntity) parent.getAdapter().getItem(position);
                Intent intent = new Intent(mActivity, LatestContentActivity.class);
                intent.putExtra(zhihuConstant.START_LOCATION, startingLocation);
                intent.putExtra("entity", entity);
                intent.putExtra("isLight", ((ZhihuActivity) mActivity).isLight());
                String readSequence = PreUtils.getStringFromDefault(mActivity, "read", "");
                String[] splits = readSequence.split(",");
                StringBuffer sb = new StringBuffer();
                if (splits.length >= 200) {
                    for (int i = 100; i < splits.length; i++) {
                        sb.append(splits[i] + ",");
                    }
                    readSequence = sb.toString();
                }

                if (!readSequence.contains(entity.getId() + "")) {
                    readSequence = readSequence + entity.getId() + ",";
                }
                PreUtils.putStringToDefault(mActivity, "read", readSequence);
                TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                tv_title.setTextColor(getResources().getColor(R.color.clicked_tv_textcolor));
                startActivity(intent);
                mActivity.overridePendingTransition(0, 0);
            }
        });
        return view;
    }

    private void loadFirst() {
        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)) {
            HttpUtils.get(zhihuConstant.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase db = ((ZhihuActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + zhihuConstant.LATEST_COLUMN + ",' " + responseString + "')");
                    db.close();
                    parseLatestJson(responseString);
                }

            });
        } else {
            SQLiteDatabase db = ((ZhihuActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + zhihuConstant.LATEST_COLUMN, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestJson(json);
            } else {
                isLoading = false;
            }
            cursor.close();
            db.close();
        }

    }

    private void parseLatestJson(String responseString) {
        Gson gson = new Gson();
        latest = gson.fromJson(responseString, Latest.class);
        date = latest.getDate();
        kanner.setTopEntities(latest.getTop_stories());
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = latest.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(zhihuConstant.TOPIC);
                topic.setTitle("今日热闻");
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }

    private void loadMore(final String url) {
        isLoading = true;
        if (HttpUtils.isNetworkConnected(mActivity)) {
            HttpUtils.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                    PreUtils.putStringTo(Constant.CACHE, mActivity, url, responseString);
                    SQLiteDatabase db = ((ZhihuActivity) mActivity).getCacheDbHelper().getWritableDatabase();
                    db.execSQL("replace into CacheList(date,json) values(" + date + ",' " + responseString + "')");
                    db.close();
                    parseBeforeJson(responseString);

                }

            });
        } else {
            SQLiteDatabase db = ((ZhihuActivity) mActivity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from CacheList where date = " + date, null);
            if (cursor.moveToFirst()) {
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            } else {
                db.delete("CacheList", "date < " + date, null);
                isLoading = false;
                Snackbar sb = Snackbar.make(lv_news, "没有更多的离线内容了~", Snackbar.LENGTH_SHORT);
                sb.getView().setBackgroundColor(getResources().getColor(((ZhihuActivity) mActivity).isLight() ? android.R.color.holo_blue_dark : android.R.color.black));
                sb.show();
            }
            cursor.close();
            db.close();
        }
    }

    private void parseBeforeJson(String responseString) {
        Gson gson = new Gson();
        before = gson.fromJson(responseString, Before.class);
        if (before == null) {
            isLoading = false;
            return;
        }
        date = before.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> storiesEntities = before.getStories();
                StoriesEntity topic = new StoriesEntity();
                topic.setType(zhihuConstant.TOPIC);
                topic.setTitle(convertDate(date));
                storiesEntities.add(0, topic);
                mAdapter.addList(storiesEntities);
                isLoading = false;
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        loadFirst();
    }

    private String convertDate(String date) {
        String result = date.substring(0, 4);
        result += "年";
        result += date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }

    public void updateTheme() {
        mAdapter.updateTheme();
    }

    public ListView listView(){
        return lv_news;
    }
}
