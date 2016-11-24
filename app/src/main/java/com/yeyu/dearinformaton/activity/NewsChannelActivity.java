package com.yeyu.dearinformaton.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.yeyu.dearinformaton.adapter.NewsChannelAdapter;
import com.yeyu.dearinformaton.Event.ChannelItemMoveEvent;
import com.yeyu.dearinformaton.GreenDao.NewsChannelTable;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.View.NewsChannelView;
import com.yeyu.dearinformaton.activity.BaseActivity.BaseActivity;
import com.yeyu.dearinformaton.listener.OnItemClickListener;
import com.yeyu.dearinformaton.presenter.impl.NewsChannelPresenterImpl;
import com.yeyu.dearinformaton.utils.RxBus;
import com.yeyu.dearinformaton.widget.ItemDragHelperCallback;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by gaoyehua on 2016/9/8.
 */
public class NewsChannelActivity extends BaseActivity implements NewsChannelView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_channel_mine_rv)
    RecyclerView mNewsChannelMineRv;
    @BindView(R.id.news_channel_more_rv)
    RecyclerView mNewsChannelMoreRv;

    @Inject
    NewsChannelPresenterImpl mNewsChannelPresenter;

    private NewsChannelAdapter mNewsChannelAdapterMine;
    private NewsChannelAdapter mNewsChannelAdapterMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelItemMoveEvent.class)
                .subscribe(new Action1<ChannelItemMoveEvent>() {
                    @Override
                    public void call(ChannelItemMoveEvent channelItemMoveEvent) {
                        int fromPosition = channelItemMoveEvent.getFromPosition();
                        int toPosition = channelItemMoveEvent.getToPosition();
                        mNewsChannelPresenter.onItemSwap(fromPosition, toPosition);
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_channel;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        mPresenter = mNewsChannelPresenter;
        mPresenter.attachView(this);
    }

    @Override
    public void initRecyclerViews(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore) {
        initRecyclerViewMineAndMore(newsChannelsMine, newsChannelsMore);
    }

    private void initRecyclerViewMineAndMore(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore) {
        initRecyclerView(mNewsChannelMineRv, newsChannelsMine, true);
        initRecyclerView(mNewsChannelMoreRv, newsChannelsMore, false);
    }

    private void initRecyclerView(RecyclerView recyclerView, List<NewsChannelTable> newsChannels
            , final boolean isChannelMine) {
        // !!!加上这句将不能动态增加列表大小。。。
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (isChannelMine) {
            mNewsChannelAdapterMine = new NewsChannelAdapter(newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMine);
            setChannelMineOnItemClick();

            initItemDragHelper();
        } else {
            mNewsChannelAdapterMore = new NewsChannelAdapter(newsChannels);
            recyclerView.setAdapter(mNewsChannelAdapterMore);
            setChannelMoreOnItemClick();
        }

    }

    private void setChannelMineOnItemClick() {
        mNewsChannelAdapterMine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMine.getData().get(position);
                boolean isNewsChannelFixed = newsChannel.getNewsChannelFixed();
                if (!isNewsChannelFixed) {
                    mNewsChannelAdapterMore.add(mNewsChannelAdapterMore.getItemCount(), newsChannel);
                    mNewsChannelAdapterMine.delete(position);

                    mNewsChannelPresenter.onItemAddOrRemove(newsChannel, true);
                }
            }
        });
    }

    private void setChannelMoreOnItemClick() {
        mNewsChannelAdapterMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMore.getData().get(position);
                mNewsChannelAdapterMine.add(mNewsChannelAdapterMine.getItemCount(), newsChannel);
                mNewsChannelAdapterMore.delete(position);

                mNewsChannelPresenter.onItemAddOrRemove(newsChannel, false);

            }
        });
    }

    private void initItemDragHelper() {
        ItemDragHelperCallback itemDragHelperCallback = new ItemDragHelperCallback(mNewsChannelAdapterMine);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragHelperCallback);
        itemTouchHelper.attachToRecyclerView(mNewsChannelMineRv);

        mNewsChannelAdapterMine.setItemDragHelperCallback(itemDragHelperCallback);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
        Snackbar.make(mNewsChannelMoreRv, message, Snackbar.LENGTH_SHORT).show();
    }
}
