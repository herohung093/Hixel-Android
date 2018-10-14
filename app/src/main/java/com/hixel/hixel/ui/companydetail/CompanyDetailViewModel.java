package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.User;
import java.util.List;
import javax.inject.Inject;


public class CompanyDetailViewModel extends ViewModel {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    private MutableLiveData<Company> company;
    private LiveData<User> user;

    @Inject
    CompanyDetailViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    void init() {
        if (this.user != null) {
            return;
        }

        user = userRepository.getUser();
    }

    void loadCompany(String ticker) {
        if (this.company != null) {
            return;
        }

        company = companyRepository.getCompany(ticker);
    }

    public LiveData<User> getUser() { return user; }

    public MutableLiveData<Company> getCompany() {
        return this.company;
    }

    void saveCompany(Company savedCompany) {
        companyRepository.saveCompany(savedCompany);
    }

}
