package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * Company is an Envelope for CompanyIdentifiers and CompanyData classes
 * allows us to format strings, calculate ratios, etc.
 */
@Entity(tableName = "companies")
public class Company {

    @PrimaryKey
    @NonNull
    private String cik;

    @SerializedName("name")
    private String name;

    @SerializedName("ticker")
    private String ticker;

    private String resp;

    public Company(@NonNull String cik, String name, String ticker, String resp) {
        this.cik = cik;
        this.name = name;
        this.ticker = ticker;
        this.resp = resp;
    }

    public String getResp() {
        return resp;
    }

    @NonNull
    public String getCik() {
        return cik;
    }

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    // TODO: Do this in a nicer way, and test against a bunch of companies.
    // TODO: Get a better way of checking for null object.
    public String getFormattedName() {
        try {
            return (this.getName()
                    .split("[\\s, ]")[0]
                    .toLowerCase()
                    .substring(0, 1).toUpperCase()) + this.getName().substring(1);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFormattedTicker() {
        try {
            return String.format("NASDAQ: %s", getTicker());
        } catch (NullPointerException e) {
            return "";
        }
    }

    public int getReturnsScore() {
        return 0;
    }

    public int getPerformanceScore() {
        return 0;
    }

    public int getStrengthScore() {
        return 0;
    }

    public int getHealthScore() {
        return 0;
    }

    public int getSafetyScore() {
        return 0;
    }
}
