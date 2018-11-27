package com.hixel.hixel.data.entities.company;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Ratios implements Serializable {
    @SerializedName("Current Ratio")
    private double currentRatio;

    @SerializedName("Debt-to-Equity Ratio")
    private double debtToEquityRatio;

    @SerializedName("Current Debt-to-Equity Ratio")
    private double currentDebtToEquityRatio;

    @SerializedName("Return-on-Equity Ratio")
    private double returnOnEquityRatio;

    @SerializedName("Return-on-Assets Ratio")
    private double returnOnAssetsRatio;

    @SerializedName("Profit-Margin Ratio")
    private double profitMarginRatio;

    @SerializedName("Interest Coverage")
    private double interestCoverageRatio;

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

    public double getProfitMarginRatio() {
        return profitMarginRatio;
    }

    public void setCurrentRatio(double currentRatio) {
        this.currentRatio = currentRatio;
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

    public void setProfitMarginRatio(double profitMarginRatio) {
        this.profitMarginRatio = profitMarginRatio;
    }

    public void setInterestCoverageRatio(double interestCoverageRatio) {
        this.interestCoverageRatio = interestCoverageRatio;
    }
}

