package com.yeyu.dearinformaton.global.component;

import android.app.Activity;
import android.content.Context;

import com.yeyu.dearinformaton.fragment.NewsListFragment;
import com.yeyu.dearinformaton.global.module.FragmentModule;
import com.yeyu.dearinformaton.global.prepare.ContextLife;
import com.yeyu.dearinformaton.global.prepare.PreFragment;

import dagger.Component;

/**
 * Created by gaoyehua on 2016/9/8.
 */
@PreFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    @ContextLife("Activity")
    Context getActivityContext();

    @ContextLife("Application")
    Context getApplicationContext();

    Activity getActivity();

    void inject(NewsListFragment newsListFragment);
}

