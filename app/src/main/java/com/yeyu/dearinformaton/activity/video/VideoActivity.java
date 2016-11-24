package com.yeyu.dearinformaton.activity.video;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.View.CustomTextView;
import com.yeyu.dearinformaton.View.VideoView;
import com.yeyu.dearinformaton.activity.BaseActivity.BaseActivity;
import com.yeyu.dearinformaton.fragment.videoFragment.DailyFragment;
import com.yeyu.dearinformaton.fragment.videoFragment.FindFragment;
import com.yeyu.dearinformaton.fragment.videoFragment.HotFragment;
import com.yeyu.dearinformaton.utils.NetUtil;
import com.yeyu.dearinformaton.utils.video.ToastUtil;

import java.util.prefs.BackingStoreException;

import butterknife.ButterKnife;
import butterknife.BindView;

/**
 * Created by gaoyehua on 2016/9/9.
 */
public class VideoActivity extends BaseActivity implements VideoView {
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
//    @BindView(R.id.fab)
//    FloatingActionButton mFab;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.main_rgp_menu)
    RadioGroup mainRgpMenu;
    @BindView(R.id.main_toolbar_tv_time)
    CustomTextView mainToolbarTvTime;
    @BindView(R.id.main_toolbar_iv_right)
    ImageButton mainToolbarIvRight;
    private FragmentTransaction transaction;
    private FindFragment findFragment;
    private HotFragment hotFragment;
    private DailyFragment dailyFragment;
    private FragmentManager fragmentManager;
    private long mExitTime = 0;
    private String Menutitle;//首页toolbar的标题

    @Override
    public int getLayoutId() {
        return R.layout.activity_video;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {

        mIsHasNavigationView = true;
        mBaseNavView = mNavView;

        fragmentManager = getSupportFragmentManager();
        initView();
        setListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取控件
        //ButterKnife.bind(this);
        //获取Fragment的管理器
    }

    //初始化界面
    private void initView() {

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设置不显示标题
        mainToolbar.setNavigationIcon(R.drawable.ic_action_menu);
        //设置默认第一个菜单按钮为选中状态
        RadioButton rb = (RadioButton) mainRgpMenu.getChildAt(0);
        rb.setChecked(true);
        setChocie(1);
    }


    //设置事件监听
    private void setListener() {
        //底部菜单栏的事件监听
        mainRgpMenu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setChocie(checkedId);
            }
        });

        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VideoActivity.this, FunctionActivity.class);
//                startActivity(intent);
            }
        });


    }

    /**
     * 底部菜单栏的点击切换
     *
     * @param currenItem
     */
    private void setChocie(int currenItem) {

        transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (currenItem) {
            case 1://每日精选
                mainToolbarTvTime.setVisibility(View.VISIBLE);
                mainToolbarIvRight.setImageResource(R.drawable.main_toolbar_eye_selector);
                if (dailyFragment == null) {
                    dailyFragment = new DailyFragment();
                    transaction.add(R.id.main_ll_fragment, dailyFragment);
                } else {
                    transaction.show(dailyFragment);
                }
                break;
            case 2://发现更多
                mainToolbarIvRight.setImageResource(R.drawable.ic_action_search);
                mainToolbarTvTime.setVisibility(View.GONE);
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.main_ll_fragment, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case 3://热门排行
                mainToolbarIvRight.setImageResource(R.drawable.main_toolbar_eye_selector);
                mainToolbarTvTime.setVisibility(View.GONE);
                if (hotFragment == null) {
                    hotFragment = new HotFragment();
                    transaction.add(R.id.main_ll_fragment, hotFragment);
                } else {
                    transaction.show(hotFragment);
                }
                break;
        }
        //提交事务
        transaction.commit();
    }

    /**
     * 隐藏所有的Fragment，避免fragment混乱
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (dailyFragment != null) {
            transaction.hide(dailyFragment);
        }
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
        if (hotFragment != null) {
            transaction.hide(hotFragment);
        }

    }

    // 按两次退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if ((System.currentTimeMillis() - mExitTime) > 2000) {
//                ToastUtil.showToast(VideoActivity.this, "再按一次退出程序");
//                mExitTime = System.currentTimeMillis();
//            } else {
                finish();
//            }
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showProgress() {
       // mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {

        //mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String message) {

        //mProgressBar.setVisibility(View.GONE);
//        if (NetUtil.isNetworkAvailable()) {
//            Snackbar.make(mFab, message, Snackbar.LENGTH_SHORT).show();
//        }
    }
}
