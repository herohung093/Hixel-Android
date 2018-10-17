package com.hixel.hixel.data.entities;

import com.google.gson.annotations.SerializedName;


/**
 * Immutable CompanyData Entity to interact with company information
 * and store it within the RoomDB
 */
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
}

