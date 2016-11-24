package com.yeyu.dearinformaton.global.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.yeyu.dearinformaton.global.prepare.ContextLife;
import com.yeyu.dearinformaton.global.prepare.PreFragment;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by gaoyehua on 2016/9/8.
 */
@Module
public class FragmentModule {
    private Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    @PreFragment
    @ContextLife("Activity")
    public Context provideActivityContext() {
        return mFragment.getActivity();
    }

    @Provides
    @PreFragment
    public Activity provideActivity() {
        return mFragment.getActivity();
    }

    @Provides
    @PreFragment
    public Fragment provideFragment() {
        return mFragment;
    }
}
