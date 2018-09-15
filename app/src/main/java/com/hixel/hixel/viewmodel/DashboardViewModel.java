package com.hixel.hixel.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.hixel.hixel.data.source.CompanyRepository;
import com.hixel.hixel.data.CompanyEntity;
import java.util.List;
import javax.inject.Inject;


public class DashboardViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private static final String TAG = DashboardViewModel.class.getSimpleName();

    private static final String[] tickers = { "AAPL" };


    private LiveData<List<CompanyEntity>> companies;
    private CompanyRepository companyRepository;


    // Tells Dagger to provide the CompanyRepository parameter
    @Inject
    public DashboardViewModel(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void init() {
        if (this.companies != null) {
            return;
        }

        Log.d(TAG, "inside init()");

        companies = companyRepository.getCompanies(tickers);
    }


    public LiveData<List<CompanyEntity>> getCompanies() {
        return this.companies;
    }
}
