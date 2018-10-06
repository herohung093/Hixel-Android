package com.hixel.hixel.di.modules;

import com.hixel.hixel.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.companydetail.CompanyDetailActivity;
import com.hixel.hixel.dashboard.DashboardActivity;
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
    abstract CompanyDetailActivity companyDetailActivity();

    @ContributesAndroidInjector
    abstract CompanyComparisonActivity comparisonComparisonActivity();
}