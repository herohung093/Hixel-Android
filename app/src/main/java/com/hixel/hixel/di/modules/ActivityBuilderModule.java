package com.hixel.hixel.di.modules;

import com.hixel.hixel.ui.companycomparison.CompanyComparisonActivity;
import com.hixel.hixel.ui.companycomparison.GraphActivity;
import com.hixel.hixel.ui.companydetail.CompanyDetailActivity;
import com.hixel.hixel.ui.dashboard.DashboardActivity;
import com.hixel.hixel.ui.login.ForgotPasswordActivity;
import com.hixel.hixel.ui.login.LoginActivity;
import com.hixel.hixel.ui.login.PinInputActivity;
import com.hixel.hixel.ui.login.SignupActivity;
import com.hixel.hixel.ui.login.UpdatePasswordActivity;
import com.hixel.hixel.ui.profile.ProfileActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Lets Dagger2 know our Views at compile time
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
    abstract GraphActivity graphActivity();

    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector
    abstract ForgotPasswordActivity forgotPasswordActivity();

    @ContributesAndroidInjector
    abstract PinInputActivity pinInputActivity();

    @ContributesAndroidInjector
    abstract SignupActivity signupActivity();

    @ContributesAndroidInjector
    abstract UpdatePasswordActivity updatePasswordActivity();

    @ContributesAndroidInjector
    abstract ProfileActivity profileActivity();
}