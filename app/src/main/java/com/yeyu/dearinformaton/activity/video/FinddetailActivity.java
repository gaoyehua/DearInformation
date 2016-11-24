package com.yeyu.dearinformaton.activity.video;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.activity.BaseActivity.BaseActivity;
import com.yeyu.dearinformaton.fragment.videoFragment.CommonfindFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaoyehua on 2016/9/19.
 */
public class FinddetailActivity extends BaseActivity {
    //标题
    private static final String[] TITLE = new String[]{"按时间排序", "分享排行"};
    private static  final String[] RANK=new String[]{"date","shareCount"};
    @BindView(R.id.find_back)
    ImageButton findBack;
    @BindView(R.id.find_detail_title)
    TextView findDetailTitle;
    @BindView(R.id.find_right)
    ImageButton findRight;
    @BindView(R.id.find_toolbar)
    Toolbar findToolbar;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.find_detail_viewpager)
    ViewPager findDetailViewpager;
    private List<Fragment> fragments = new ArrayList<>();
    private String name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_detail;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_detail);
        ButterKnife.bind(this);
        initView();
        initData();
        setAdapter();
    }
    //初始化控件
    private void initView() {
        name = getIntent().getStringExtra("name");
        findDetailTitle.setText(name);
    }

    //初始化数据
    private void initData() {
        //创建两个通用fragment
        for (int i = 0; i < TITLE.length; i++) {
            CommonfindFragment commonFindFragment = new CommonfindFragment();
            fragments.add(commonFindFragment);
        }
    }

    //设置适配器
    private void setAdapter() {
        final Bundle bundle=new Bundle();
        bundle.putString("name", name);
        viewpagertab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                bundle.putInt("position",position);

            }
        });
        //实例化适配器
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(FragmentPagerItem.of(TITLE[0], CommonfindFragment.class, bundle))
                .add(FragmentPagerItem.of(TITLE[1],CommonfindFragment.class,bundle))
                .create());
        //设置适配器
        findDetailViewpager.setAdapter(adapter);
        viewpagertab.setViewPager(findDetailViewpager);
    }

    @OnClick({R.id.find_back, R.id.find_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find_back://返回上一级
                finish();
                break;
            case R.id.find_right:
                break;
        }
    }
}
