package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * Immutable Company Entity
 *
 * Returns:         Dividend Yield = Annual Dividends per share/Price per share ***
 * Performance:     Return on equity = NetIncome(Loss) / Equity
 * Strength:        Interest coverage = EBIT / net interest expense
 * Health:          Current ratio = AssetsCurrent / LiabilitiesCurrent
 * Security/Safety: Current Debt/Equity (D/E) Ratio = LiabilitiesCurrent/ Equity
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

    @SerializedName("Current Debt-to-Equity Ratio")
    private double currentDebtToEquity;

    @SerializedName("Return-on-Equity Ratio")
    private double returnOnEquity;

    @SerializedName("Return-on-Assets Ratio")
    private double returnOnAssets;

    @SerializedName("Interest Coverage")
    private double interestCoverage;

    public Company(@NonNull String cik, String name, String ticker,
            double currentRatio, double debtToEquity, double currentDebtToEquity,
            double returnOnEquity, double returnOnAssets, double interestCoverage) {
        this.cik = cik;
        this.name = name;
        this.ticker = ticker;
        this.currentRatio = currentRatio;
        this.debtToEquity = debtToEquity;
        this.currentDebtToEquity = currentDebtToEquity;
        this.returnOnEquity = returnOnEquity;
        this.returnOnAssets = returnOnAssets;
        this.interestCoverage = interestCoverage;
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

    public double getReturnOnEquity() {
        return returnOnEquity;
    }

    public double getReturnOnAssets() {
        return returnOnAssets;
    }

    public double getCurrentDebtToEquity() {
        return currentDebtToEquity;
    }

    public double getCurrentRatio() {
        return currentRatio;
    }

    public double getInterestCoverage() {
        return interestCoverage;
    }

    // TODO: Do this in a nicer way, and test against a bunch of companies.
    // TODO: Get a better way of checking for null object.

    /**
     * Method formats the companies name, ensures all companies
     * names look alike
     *
     * @return The formatted name
     */
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

    /**
     * Method formats the companies ticker, ensures all tickers
     * look alike.
     *
     * @return The formatted ticker
     */
    public String getFormattedTicker() {
        try {
            return String.format("NASDAQ: %s", getTicker());
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * Method returns the returns score of the company out of 5
     * @return The returns score out of 5
     */
    public int getReturnsScore() {
        // TODO: Need to use dividend yield.
        return generateScore(this.returnOnAssets);
    }

    public int getPerformanceScore() {
        return generateScore(this.returnOnEquity);
    }

    public int getStrengthScore() {
        return generateScore(this.interestCoverage);
    }

    public int getHealthScore() {
        return generateScore(this.currentRatio);
    }

    public int getSafetyScore() {
        return generateScore(this.currentDebtToEquity);
    }

    /**
     * Method generates a score based upon the input ratio.
     *
     * @param ratio The ratio for generating a score.
     * @return The score for the corresponding ratio.
     */
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
