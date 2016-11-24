package com.yeyu.dearinformaton.global.module;

import android.app.Activity;
import android.content.Context;

import com.yeyu.dearinformaton.global.prepare.ContextLife;
import com.yeyu.dearinformaton.global.prepare.PreActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gaoyehua on 2016/9/7.
 */
@Module
public class ActivityModule {
    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PreActivity
    @ContextLife("Activity")
    public Context ProvideActivityContext() {
        return mActivity;
    }

    @Provides
    @PreActivity
    public Activity ProvideActivity() {
        return mActivity;
    }
}

