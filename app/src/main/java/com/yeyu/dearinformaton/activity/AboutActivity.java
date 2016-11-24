package com.yeyu.dearinformaton.activity;

import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.yeyu.dearinformaton.activity.BaseActivity.BaseActivity;

import com.yeyu.dearinformaton.R;

import butterknife.BindView;

/**
 * Created by gaoyehua on 2016/9/7.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.msg_tv)
    TextView mMsgTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initInjector() {

    }

    @Override
    public void initViews() {

        mMsgTv.setAutoLinkMask(Linkify.ALL);
        mMsgTv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
