package com.hixel.hixel.di.modules;

import com.hixel.hixel.ui.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.companydetail.CompanyDetailActivity;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.login.LoginActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;
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

    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector
    abstract ProfileActivity profileActivity();
}