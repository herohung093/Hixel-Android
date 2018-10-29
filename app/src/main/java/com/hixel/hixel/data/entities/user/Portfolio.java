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
    private List<Ticker> companies;

    public List<Ticker> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Ticker> companies) {
        this.companies = companies;
    }
}
