package com.hixel.hixel.company;

import com.hixel.hixel.data.Company;

public class CompanyPresenter implements CompanyContract.Presenter {
    private Company company;
    private final CompanyContract.View companyView;

    CompanyPresenter(CompanyContract.View companyView) {
        this.companyView = companyView;
        companyView.setPresenter(this);
    }

    public void start() {}


    public String getCompanyName() {
        return company.getName();
    }
}
