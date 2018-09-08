package com.hixel.hixel.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.source.CompanyRepository;
import com.hixel.hixel.data.CompanyEntity;
import java.util.List;
import javax.inject.Inject;


public class DashboardViewModel extends ViewModel {

    private static final String[] tickers = { "APPL", "APPL", "APPL", "APPL" };


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

        companies = companyRepository.getCompanies(tickers);
    }


    public LiveData<List<CompanyEntity>> getCompanies() {
        return this.companies;
    }
}
