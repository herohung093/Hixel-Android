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
}

