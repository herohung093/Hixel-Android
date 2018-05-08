package com.hixel.hixel.company;

import android.graphics.Color;
import com.hixel.hixel.data.Company;

public class CompanyPresenter implements CompanyContract.Presenter {
    private Company company;
    private final CompanyContract.View companyView;

    CompanyPresenter(CompanyContract.View companyView) {
        this.companyView = companyView;
        companyView.setPresenter(this);
    }

    public void start() {

    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return company.getName();
    }

    public String getLeverage() {
        return Double.toString(company.getLeverage());
    }

    public String getLiquidity() {
        return Double.toString(company.getLiquidity());
    }

    public String getHealth() {
        return Double.toString(company.getHealth());
    }

}
