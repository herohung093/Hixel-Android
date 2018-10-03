package com.hixel.hixel.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import com.hixel.hixel.di.ViewModelKey;
import com.hixel.hixel.viewmodel.CompanyViewModel;
import com.hixel.hixel.viewmodel.ComparisonViewModel;
import com.hixel.hixel.viewmodel.DashboardViewModel;
import com.hixel.hixel.ViewModelFactory;
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
    @ViewModelKey(CompanyViewModel.class)
    abstract ViewModel bindCompanyViewModel(CompanyViewModel companyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ComparisonViewModel.class)
    abstract ViewModel bindComparisonViewModel(ComparisonViewModel comparisonViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
