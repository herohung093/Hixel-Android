package com.hixel.hixel.data.entities;

import com.google.gson.annotations.SerializedName;

public class CompanyData {
    @SerializedName("Current Ratio")
    private double currentRatio;

    @SerializedName("Debt-to-Equity Ratio")
    private double debtToEquityRatio;

    @SerializedName("Return-on-Equity Ratio")
    private double returnOnEquityRatio;

    @SerializedName("Return-on-Assets Ratio")
    private double returnOnAssetsRatio;

    @SerializedName("Profit-Margin Ratio")
    private double profitMarginRatio;

    public CompanyData(double currentRatio, double debtToEquityRatio,
            double returnOnEquityRatio, double returnOnAssetsRatio, double profitMarginRatio) {
        this.currentRatio = currentRatio;
        this.debtToEquityRatio = debtToEquityRatio;
        this.returnOnEquityRatio = returnOnEquityRatio;
        this.returnOnAssetsRatio = returnOnAssetsRatio;
        this.profitMarginRatio = profitMarginRatio;
    }

    public double getProfitMarginRatio() {
        return profitMarginRatio;
    }

    public double getReturnOnAssetsRatio() {
        return returnOnAssetsRatio;
    }

    public double getDebtToEquityRatio() {
        return debtToEquityRatio;
    }

    // Return on equity = NetIncome(Loss) / Equity
    public double getReturnOnEquityRatio() {
        return returnOnEquityRatio;
    }

    // Current ratio = AssetsCurrent / LiabilitiesCurrent
    public double getCurrentRatio() {
        return currentRatio;
    }

    // Dividend Yield = Annual Dividends per share/Price per share
    public double getDividendYield() {
        return 0;
    }

    // Interest coverage = EBIT / net interest expense
    public double getInterestCoverage() {
        return 0;
    }

    // Current Debt/Equity (D/E) Ratio = LiabilitiesCurrent/ Equity
    public double getCurrentDebtToEquity() {
        return 0;
    }
}

