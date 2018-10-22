package com.hixel.hixel.data.entities.user;

import android.arch.persistence.room.TypeConverters;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Simple Portfolio model with a TypeConverter so that Room can directly read in the List
 * of company tickers without needing to manually deserialize.
 */
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
