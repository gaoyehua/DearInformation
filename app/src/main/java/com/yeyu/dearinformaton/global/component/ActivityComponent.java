package com.yeyu.dearinformaton.global.component;

import android.app.Activity;
import android.content.Context;

import com.yeyu.dearinformaton.activity.PhotoActivity;
import com.yeyu.dearinformaton.activity.PhotoDetailActivity;
import com.yeyu.dearinformaton.activity.video.VideoActivity;
import com.yeyu.dearinformaton.activity.video.VideoDetailActivity;
import com.yeyu.dearinformaton.activity.zhihu.ZhihuActivity;
import com.yeyu.dearinformaton.global.module.ActivityModule;
import com.yeyu.dearinformaton.global.prepare.ContextLife;
import com.yeyu.dearinformaton.global.prepare.PreActivity;
import com.yeyu.dearinformaton.activity.NewsActivity;
import com.yeyu.dearinformaton.activity.NewsChannelActivity;
import com.yeyu.dearinformaton.activity.NewsDetailActivity;
import com.yeyu.dearinformaton.activity.NewsPhotoDetailActivity;

import dagger.Component;

/**
 * Created by gaoyehua on 2016/9/7.
 */
@PreActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(NewsActivity newsActivity);
//
    void inject(NewsDetailActivity newsDetailActivity);
//
    void inject(NewsChannelActivity newsChannelActivity);
//
    void inject(NewsPhotoDetailActivity newsPhotoDetailActivity);
//
    void inject(PhotoActivity photoActivity);
//
    void inject(PhotoDetailActivity photoDetailActivity);

    void inject(ZhihuActivity zhihuActivity);

    void inject(VideoActivity videoActivity);

    void inject(VideoDetailActivity videoDetailActivity);
}