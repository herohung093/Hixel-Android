package com.hixel.hixel.di.modules;

import com.hixel.hixel.view.ui.CompanyActivity;
import com.hixel.hixel.view.ui.DashboardActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Lets Dagger know our Views at compile time
 */

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract DashboardActivity dashboardActivity();

    @ContributesAndroidInjector
    abstract CompanyActivity companyActivity();

    @ContributesAndroidInjector
    abstract CompanyActivity comparisonActivity();
}