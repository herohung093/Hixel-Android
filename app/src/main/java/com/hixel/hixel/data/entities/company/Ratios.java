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

    public void setInterestCoverageRatio(double interestCoverageRatio) {
        this.interestCoverageRatio = interestCoverageRatio;
    }

    public void setReturnOnEquityRatio(double returnOnEquityRatio) {
        this.returnOnEquityRatio = returnOnEquityRatio;
    }

    public void setReturnOnAssetsRatio(double returnOnAssetsRatio) {
        this.returnOnAssetsRatio = returnOnAssetsRatio;
    }

    public void setCurrentDebtToEquityRatio(double currentDebtToEquityRatio) {
        this.currentDebtToEquityRatio = currentDebtToEquityRatio;
    }

    public void setDebtToEquityRatio(double debtToEquityRatio) {
        this.debtToEquityRatio = debtToEquityRatio;
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

