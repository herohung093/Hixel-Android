package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * Immutable Company Entity
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

    @SerializedName("Current Ratio")
    private double currentRatio;

    @SerializedName("Debt-to-Equity Ratio")
    private double debtToEquity;

    @SerializedName("Return-on-Equity Ratio")
    private double returnOnEquity;

    @SerializedName("Return-on-Assets Ratio")
    private double returnOnAssets;

    @SerializedName("Profit-Margin Ratio")
    private double profitMargin;

    public Company(@NonNull String cik, String name, String ticker, double currentRatio, double debtToEquity, double returnOnEquity, double returnOnAssets, double profitMargin) {
        this.cik = cik;
        this.name = name;
        this.ticker = ticker;
        this.currentRatio = currentRatio;
        this.debtToEquity = debtToEquity;
        this.returnOnEquity = returnOnEquity;
        this.returnOnAssets = returnOnAssets;
        this.profitMargin = profitMargin;
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

    public double getDebtToEquity() {
        return debtToEquity;
    }

    public double getReturnOnAssets() {
        return returnOnAssets;
    }

    public double getProfitMargin() {
        return profitMargin;
    }

    public double getReturnOnEquity() {
        return returnOnEquity;
    }

    public double getCurrentRatio() {
        return currentRatio;
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
        // TODO: Make this dividend yield.
        return generateScore(this.returnOnAssets);
    }

    public int getPerformanceScore() {
        return generateScore(this.returnOnEquity);
    }

    public int getStrengthScore() {
        // TODO: Make this interest coverage
        return generateScore(this.profitMargin);
    }

    public int getHealthScore() {
        return generateScore(this.currentRatio);
    }

    public int getSafetyScore() {
        return generateScore(this.debtToEquity);
    }

    private int generateScore(double ratio) {
        int score = 1;

        if (ratio >= 0.5 && ratio < 1.0) {
            score = 2;
        } else if (ratio >= 1.0 && ratio < 1.5) {
            score = 3;
        } else if (ratio >= 1.5 && ratio < 2.0) {
            score = 4;
        } else if (ratio >= 2.0) {
            score = 5;
        }

        return score;
    }
}
