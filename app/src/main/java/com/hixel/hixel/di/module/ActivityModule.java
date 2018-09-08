package com.hixel.hixel.di.module;

import com.hixel.hixel.view.ui.DashboardActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract DashboardActivity contributeDashboardActivity();
}
