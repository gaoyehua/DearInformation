package com.yeyu.dearinformaton.global.component;

import android.content.Context;

import com.yeyu.dearinformaton.global.module.ApplicationModule;
import com.yeyu.dearinformaton.global.prepare.ContextLife;
import com.yeyu.dearinformaton.global.prepare.PreApp;

import dagger.Component;

/**
 * Created by gaoyehua on 2016/9/7.
 */
@PreApp
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    @ContextLife("Application")
    Context getApplication();

}
