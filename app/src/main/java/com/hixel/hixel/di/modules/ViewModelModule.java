package com.hixel.hixel.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.hixel.hixel.di.ViewModelKey;
import com.hixel.hixel.ui.companycomparison.GraphViewModel;
import com.hixel.hixel.ui.companydetail.CompanyDetailViewModel;
import com.hixel.hixel.ui.companycomparison.CompanyComparisonViewModel;
import com.hixel.hixel.ui.dashboard.DashboardViewModel;
import com.hixel.hixel.ViewModelFactory;
import com.hixel.hixel.ui.login.LoginViewModel;
import com.hixel.hixel.ui.profile.ProfileViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Binds and Maps each of our ViewModels to a (k,v), and provides a ViewModel.
 * <p>
 * key: ViewModel Class constant.
 * value: ViewModel Instance.
 * </p>
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
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GraphViewModel.class)
    abstract ViewModel bindGraphViewModel(GraphViewModel graphViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
