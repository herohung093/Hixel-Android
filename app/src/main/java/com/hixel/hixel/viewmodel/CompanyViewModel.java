package com.hixel.hixel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.hixel.hixel.service.models.Company;



public class CompanyViewModel extends AndroidViewModel {

    private MutableLiveData<Company> company= new MutableLiveData<>();

    public CompanyViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<Company> getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company.setValue(company);
    }

    // TODO: Logic for adding a company to the portfolio

}
