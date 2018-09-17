package com.hixel.hixel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.CompanyEntity;
import com.hixel.hixel.data.source.CompanyRepository;
import javax.inject.Inject;


public class CompanyViewModel extends ViewModel {

    private MutableLiveData<CompanyEntity> company;
    private CompanyRepository companyRepository;

    @Inject
    CompanyViewModel(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void loadCompany(String ticker) {
        company = companyRepository.getCompany(ticker);
    }

    public MutableLiveData<CompanyEntity> getCompany() {
        return this.company;
    }

    public void saveCompany() {
        companyRepository.saveCompany(company.getValue());
    }

    // TODO: Actually check if it is in the portfolio.
    public boolean companyIsInPortfolio() {
        return true;
    }

}
