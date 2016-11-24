package com.yeyu.dearinformaton.activity.zhihu;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yeyu.dearinformaton.Event.ScrollToTopEvent;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.View.ZhihuView;
import com.yeyu.dearinformaton.activity.AboutActivity;
import com.yeyu.dearinformaton.activity.BaseActivity.BaseActivity;
import com.yeyu.dearinformaton.adapter.zhihu.MainNewsItemAdapter;
import com.yeyu.dearinformaton.db.CacheDbHelper;
import com.yeyu.dearinformaton.fragment.zhihuFragment.ZhihuFragment;
import com.yeyu.dearinformaton.utils.NetUtil;
import com.yeyu.dearinformaton.utils.RxBus;

import butterknife.BindView;

/**
 * Created by gaoyehua on 2016/9/18.
 */
public class ZhihuActivity extends BaseActivity implements ZhihuView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.fl_content)
    FrameLayout fl_content;

    private long firstTime;
    private String curId;
    private boolean isLight;
    private CacheDbHelper dbHelper;
    private SharedPreferences sp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zhihu;
    }

    @Override
    public void initInjector() {

        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {

        mIsHasNavigationView = true;
        mBaseNavView = mNavView;

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        dbHelper = new CacheDbHelper(this, 1);
        isLight = sp.getBoolean("isLight", true);
        initView();
        loadLatest();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhihuFragment zhihuFragment =new ZhihuFragment();
                if(zhihuFragment.listView() !=null){
                    zhihuFragment.listView().setSelection(0);
                }

            }
        });
    }

    public void loadLatest() {
        getSupportFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
                replace(R.id.fl_content, new ZhihuFragment(), "latest").
                commit();
        curId = "latest";
    }

    public void setCurId(String id) {
        curId = id;
    }

    private void initView() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        mNavView =(NavigationView) findViewById(R.id.nav_view);
        // toolbar.setBackgroundColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));
//        setSupportActionBar(toolbar);
       // setStatusBarColor(getResources().getColor(isLight ? R.color.light_toolbar : R.color.dark_toolbar));

        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                replaceFragment();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    public void replaceFragment() {
        if (curId.equals("latest")) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                    .replace(R.id.fl_content,
                            new ZhihuFragment(), "latest").commit();
        } else {

        }

    }

    public void closeMenu() {
        mDrawerLayout.closeDrawers();
    }

    public void setSwipeRefreshEnable(boolean enable) {
        mSwipeRefreshLayout.setEnabled(enable);
    }

//    public void setToolbarTitle(String text) {
//        toolbar.setTitle(text);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_zhihu, menu);
        //menu.getItem(0).setTitle(sp.getBoolean("isLight", true) ? "夜间模式" : "日间模式");
        menu.getItem(0).setTitle("关于");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_mode) {
            Intent intent =new Intent(ZhihuActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isLight() {
        return isLight;
    }

    public CacheDbHelper getCacheDbHelper() {
        return dbHelper;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            closeMenu();
        } else {
//            long secondTime = System.currentTimeMillis();
//            if (secondTime - firstTime > 2000) {
//                Snackbar sb = Snackbar.make(fl_content, "再按一次退出", Snackbar.LENGTH_SHORT);
//                sb.getView().setBackgroundColor(getResources().getColor(isLight ? android.R.color.holo_blue_dark : android.R.color.black));
//                sb.show();
//                firstTime = secondTime;
//            } else {
                finish();
           // }
        }

    }


    @TargetApi(21)
    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

        if (NetUtil.isNetworkAvailable()) {
            Snackbar.make(mFab, message, Snackbar.LENGTH_SHORT).show();
        }

    }
}
