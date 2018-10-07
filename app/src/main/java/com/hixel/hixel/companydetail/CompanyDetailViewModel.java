package com.hixel.hixel.companydetail;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import javax.inject.Inject;


public class CompanyDetailViewModel extends ViewModel {

    private MutableLiveData<Company> company;
    private CompanyRepository companyRepository;

    @Inject
    CompanyDetailViewModel(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void loadCompany(String ticker) {
        company = companyRepository.getCompany(ticker);
    }

    public MutableLiveData<Company> getCompany() {
        return this.company;
    }

    public void saveCompany() {
        companyRepository.saveCompany(company.getValue());
    }

    public boolean companyIsInPortfolio() {
        return true;
    }
}
