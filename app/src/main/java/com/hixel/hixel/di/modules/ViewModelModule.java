package com.hixel.hixel.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.hixel.hixel.di.ViewModelKey;
import com.hixel.hixel.companydetail.CompanyDetailViewModel;
import com.hixel.hixel.companycomparison.CompanyComparisonViewModel;
import com.hixel.hixel.dashboard.DashboardViewModel;
import com.hixel.hixel.ViewModelFactory;
import com.hixel.hixel.login.LoginViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Module information for each ViewModel
 */

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel.class)
    abstract ViewModel bindDashboardViewModel(DashboardViewModel dashboardViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CompanyDetailViewModel.class)
    abstract ViewModel bindCompanyDetailViewModel(CompanyDetailViewModel companyDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CompanyComparisonViewModel.class)
    abstract ViewModel bindCompanyComparisonViewModel(CompanyComparisonViewModel companyComparisonViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
