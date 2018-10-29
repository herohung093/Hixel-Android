package com.hixel.hixel.data.entities.company;

import com.google.gson.annotations.SerializedName;

public class Ratios {
    @SerializedName("Debt-to-Equity Ratio")
    public double debtToEquityRatio;
    @SerializedName("Current Debt-to-Equity Ratio")
    public double currentDebtToEquityRatio;
    @SerializedName("Return-on-Equity Ratio")
    public double returnOnEquityRatio;
    @SerializedName("Return-on-Assets Ratio")
    public double returnOnAssetsRatio;
    @SerializedName("Interest Coverage")
    public double interestCoverageRatio;
    @SerializedName("Current Ratio")
    public double currentRatio;
    @SerializedName("Dividend Yield")
    public double dividendYield;
    @SerializedName("Profit-Margin Ratio")
    public double profitMarginRatio;

    public Ratios(double debtToEquityRatio, double currentDebtToEquityRatio,
            double returnOnAssetsRatio, double interestCoverageRatio) {
        this.debtToEquityRatio = debtToEquityRatio;
        this.currentDebtToEquityRatio = currentDebtToEquityRatio;
        this.returnOnEquityRatio = returnOnAssetsRatio;
        this.interestCoverageRatio = interestCoverageRatio;
    }

    public double getDebtToEquityRatio() {
        return debtToEquityRatio;
    }

    public double getReturnOnEquityRatio() {
        return returnOnEquityRatio;
    }

    public double getReturnOnAssetsRatio() {
        return returnOnAssetsRatio;
    }

    public double getInterestCoverageRatio() {
        return interestCoverageRatio;
    }

    public double getCurrentDebtToEquityRatio() {
        return currentDebtToEquityRatio;
    }

    public double getCurrentRatio() {
        return currentRatio;
    }

    public double getDividendYield() {
        return dividendYield;
    }

    public double getProfitMarginRatio() {
        return profitMarginRatio;
    }
}

