package com.yeyu.dearinformaton.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.squareup.picasso.Picasso;
import com.yeyu.dearinformaton.Constant.Constant;
import com.yeyu.dearinformaton.Constant.PhotoRequestType;
import com.yeyu.dearinformaton.R;
import com.yeyu.dearinformaton.View.PhotoDetailView;
import com.yeyu.dearinformaton.activity.BaseActivity.BaseActivity;
import com.yeyu.dearinformaton.global.prepare.ContextLife;
import com.yeyu.dearinformaton.presenter.impl.PhotoDetailPresenterImpl;
import com.yeyu.dearinformaton.utils.MyUtils;
import com.yeyu.dearinformaton.utils.SystemUiVisibilityUtil;
import com.yeyu.dearinformaton.widget.PullBackLayout;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.Lazy;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by gaoyehua on 2016/9/7.
 */
public class PhotoDetailActivity extends BaseActivity implements
        PullBackLayout.Callback, PhotoDetailView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.photo_iv)
    ImageView mPhotoIv;
    @BindView(R.id.pull_back_layout)
    PullBackLayout mPullBackLayout;
    @BindView(R.id.photo_touch_iv)
    PhotoView mPhotoTouchIv;

    @Inject
    Lazy<PhotoDetailPresenterImpl> mPhotoDetailPresenter;
    @Inject
    @ContextLife("Activity")
    Context mContext;

    private ColorDrawable mBackground;
    private boolean mIsToolBarHidden;
    private boolean mIsStatusBarHidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPullBackLayout.setCallback(this);
        initLazyLoadView();
    }

    @Override
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
    }

    private void initLazyLoadView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    showToolBarAndPhotoTouchView();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            showToolBarAndPhotoTouchView();
        }
    }

    private void showToolBarAndPhotoTouchView() {
        toolBarFadeIn();
        loadPhotoTouchIv();
    }

    private void toolBarFadeIn() {
        mIsToolBarHidden = true;
        hideOrShowToolbar();
    }

    private void loadPhotoTouchIv() {
        Picasso.with(this)
                .load(getIntent().getStringExtra(Constant.PHOTO_DETAIL))
                .error(R.drawable.ic_load_fail)
                .into(mPhotoTouchIv);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initViews() {
        initToolbar();
        initImageView();
        initBackground();
        setPhotoViewClickEvent();
    }

    private void initToolbar() {
        mToolbar.setTitle(getString(R.string.girl));
    }

    private void initImageView() {
        loadPhotoIv();
    }

    private void loadPhotoIv() {
        Picasso.with(this)
                .load(getIntent().getStringExtra(Constant.PHOTO_DETAIL))
                .into(mPhotoIv);
    }

    private void setPhotoViewClickEvent() {
        mPhotoTouchIv.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
                KLog.d();
                hideOrShowToolbar();
                hideOrShowStatusBar();
            }

            @Override
            public void onOutsidePhotoTap() {
                KLog.d();
                hideOrShowToolbar();
                hideOrShowStatusBar();
            }
        });
    }

    protected void hideOrShowToolbar() {
        mToolbar.animate()
                .alpha(mIsToolBarHidden ? 1.0f : 0.0f)
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsToolBarHidden = !mIsToolBarHidden;
    }

    private void hideOrShowStatusBar() {
        if (mIsStatusBarHidden) {
            SystemUiVisibilityUtil.enter(PhotoDetailActivity.this);
        } else {
            SystemUiVisibilityUtil.exit(PhotoDetailActivity.this);
        }
        mIsStatusBarHidden = !mIsStatusBarHidden;
    }

    @SuppressWarnings("deprecation")
    private void initBackground() {
        mBackground = new ColorDrawable(Color.BLACK);
        MyUtils.getRootView(this).setBackgroundDrawable(mBackground);
    }

    private void initPresenter() {
        mPresenter = mPhotoDetailPresenter.get(); // 在这时才创建mPhotoDetailPresenter,以后每次调用get会得到同一个mPhotoDetailPresenter对象
        mPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                handlePicture(PhotoRequestType.TYPE_SHARE);
                return true;
            case R.id.action_save:
                handlePicture(PhotoRequestType.TYPE_SAVE);
                return true;
            case R.id.action_set_wallpaper:
                handlePicture(PhotoRequestType.TYPE_SET_WALLPAPER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handlePicture(int type) {
        initPresenter();
        mPhotoDetailPresenter.get().handlePicture(getIntent().getStringExtra(Constant.PHOTO_DETAIL)
                , type);
    }

    @Override
    public void onPullStart() {
        toolBarFadeOut();

        mIsStatusBarHidden = true;
        hideOrShowStatusBar();
    }

    private void toolBarFadeOut() {
        mIsToolBarHidden = false;
        hideOrShowToolbar();
    }

    @Override
    public void onPull(float progress) {
        KLog.d("progress: " + progress);
        progress = Math.min(1f, progress * 3f);
        KLog.d("alpha: " + (int) (0xff * (1f - progress)));
        mBackground.setAlpha((int) (0xff/*255*/ * (1f - progress)));
    }

    @Override
    public void onPullCancel() {
        toolBarFadeIn();
    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}

