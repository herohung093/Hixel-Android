package com.hixel.hixel.data.entities;

import android.arch.persistence.room.TypeConverters;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Portfolio {

    @SerializedName("companies")
    @TypeConverters(PortfolioTypeConverter.class)
    private List<String> companies;

    public List<String> getCompanies() {
        return companies;
    }

    public void setCompanies(List<String> companies) {
        this.companies = companies;
    }

    public void addCompany(String ticker) {
        this.companies.add(ticker);
    }
}
