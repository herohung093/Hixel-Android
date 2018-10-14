package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import java.util.List;
import javax.inject.Inject;


public class CompanyDetailViewModel extends ViewModel {

    private MutableLiveData<Company> company;

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    private boolean isInPortfolio = false;

    @Inject
    CompanyDetailViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    void init(String ticker) {
        if (this.company != null) {
            return;
        }

        List<String> tickers = userRepository.getUserTickers();

        if (tickers.contains(ticker)) {
            isInPortfolio = true;
        }

        company = companyRepository.getCompany(ticker);
    }


    void loadCompany(String ticker) {
        company = companyRepository.getCompany(ticker);
    }

    public MutableLiveData<Company> getCompany() {
        return this.company;
    }

    void saveCompany() {
        companyRepository.saveCompany(company.getValue());
    }

    boolean companyIsInPortfolio(String ticker) {
        return isInPortfolio;
    }
}
