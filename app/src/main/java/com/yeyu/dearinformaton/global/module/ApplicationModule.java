package com.yeyu.dearinformaton.global.module;

import android.content.Context;

import com.yeyu.dearinformaton.global.prepare.ContextLife;
import com.yeyu.dearinformaton.global.prepare.PreApp;
import com.yeyu.dearinformaton.MyApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gaoyehua on 2016/9/7.
 */

@Module
public class ApplicationModule {
    private MyApplication mApplication;

    public ApplicationModule(MyApplication application) {
        mApplication = application;
    }

    @Provides
    @PreApp
    @ContextLife("Application")
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }
}
