package com.hixel.hixel.company;

import android.graphics.Color;

import com.hixel.hixel.models.Company;

public class CompanyPresenter implements CompanyContract.Presenter {
    private Company company;
    private final CompanyContract.View companyView;

    CompanyPresenter(CompanyContract.View companyView) {
        this.companyView = companyView;
        companyView.setPresenter(this);
    }

    public void start() {

    }

    public void loadFromServer() {

    }

    public String getRatio(String ratio, int year) {
        Double value = company.getRatio(ratio, year);

        return (value == null) ? "N/A"
                : Double.toString(value);
    }

    public int getColorIndicator(String ratio, double value)
    {
        //Default thresholds.
        double green = 1.5;
        double yellow = 1.0;

        //Specified thresholds for different ratios.
        switch (ratio) {
            case "Health":
                green = 0.7;
                yellow = 0.5;
                break;

            //TODO: Add other ratio thresholds.
        }

        return Color.parseColor((value > green) ? "#C23934":
                (value > yellow)? "#FFB75D":
                        "#4BCA81");
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return company.getIdentifiers().getName();
    }
}
