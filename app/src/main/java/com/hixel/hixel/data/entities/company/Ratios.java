package com.hixel.hixel.data.entities.company;

import com.google.gson.annotations.SerializedName;

public class Ratios {
    @SerializedName("Debt-to-Equity Ratio")
    private double debtToEquityRatio;
    @SerializedName("Current Debt-to-Equity Ratio")
    private double currentDebtToEquityRatio;
    @SerializedName("Return-on-Equity Ratio")
    private double returnOnEquityRatio;
    @SerializedName("Return-on-Assets Ratio")
    private double returnOnAssetsRatio;
    @SerializedName("Interest Coverage")
    private double interestCoverageRatio;

    public Ratios(double debtToEquityRatio, double currentDebtToEquityRatio,
            double returnOnEquityRatio, double returnOnAssetsRatio, double interestCoverageRatio) {
        this.debtToEquityRatio = debtToEquityRatio;
        this.currentDebtToEquityRatio = currentDebtToEquityRatio;
        this.returnOnAssetsRatio = returnOnAssetsRatio;
        this.returnOnEquityRatio = returnOnEquityRatio;
        this.interestCoverageRatio = interestCoverageRatio;
    }

    public double getReturnOnAssetsRatio() {
        return returnOnAssetsRatio;
    }

    public double getDebtToEquityRatio() {
        return debtToEquityRatio;
    }

    public double getReturnOnEquityRatio() {
        return returnOnEquityRatio;
    }

    public double getCurrentDebtToEquityRatio() {
        return currentDebtToEquityRatio;
    }

    public double getInterestCoverageRatio() {
        return interestCoverageRatio;
    }
}

