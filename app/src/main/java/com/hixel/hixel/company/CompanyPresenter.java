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

    public int setLeverageColor() {
        int color = Color.parseColor("#FFB75D");
        double value = company.getLeverage();

        if (value > 1.5) {
            color = Color.parseColor("#C23934");
        } else if (value < 1.0) {
            color = Color.parseColor("#4BCA81");
        }
        return color;
    }

    public int setLiquidityColor() {
        int color = Color.parseColor("#FFB75D");
        double value = company.getLiquidity();

        if (value > 1.5) {
            color = Color.parseColor("#C23934");
        } else if (value < 1.0) {
            color = Color.parseColor("#4BCA81");
        }
        return color;
    }

    public int setHealthColor() {
        int color = Color.parseColor("#FFB75D");
        double value = company.getHealth();

        if (value < 0.5) {
            color = Color.parseColor("#C23934");
        } else if (value > 0.6) {
            color = Color.parseColor("#4BCA81");
        }
        return color;
    }
}
