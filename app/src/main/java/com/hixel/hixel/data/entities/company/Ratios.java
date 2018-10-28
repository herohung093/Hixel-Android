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

    public void setDebtToEquityRatio(double debtToEquityRatio) {
        this.debtToEquityRatio = debtToEquityRatio;
    }

    public void setCurrentDebtToEquityRatio(double currentDebtToEquityRatio) {
        this.currentDebtToEquityRatio = currentDebtToEquityRatio;
    }

    public void setReturnOnEquityRatio(double returnOnEquityRatio) {
        this.returnOnEquityRatio = returnOnEquityRatio;
    }

    public void setReturnOnAssetsRatio(double returnOnAssetsRatio) {
        this.returnOnAssetsRatio = returnOnAssetsRatio;
    }

    public void setInterestCoverageRatio(double interestCoverageRatio) {
        this.interestCoverageRatio = interestCoverageRatio;
    }
}

