package com.hixel.hixel.data.entities;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;


/**
 *  * Returns:         Dividend Yield = Annual Dividends per share/Price per share ***
 *  * Performance:     Return on equity = NetIncome(Loss) / Equity
 *  * Strength:        Interest coverage = EBIT / net interest expense
 *  * Health:          Current ratio = AssetsCurrent / LiabilitiesCurrent
 *  * Security/Safety: Current Debt/Equity (D/E) Ratio = LiabilitiesCurrent/ Equity
 */
@Entity(tableName = "company_data",
        foreignKeys = @ForeignKey(entity = Company.class,
                parentColumns = "cik",
                childColumns = "cik",
                onDelete = CASCADE
))
public class CompanyData {

    @PrimaryKey(autoGenerate = true)
    private final int id;

    @SerializedName("year")
    private int year;

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

    private final String cik;

    /**
     *
     * @param currentRatio the companies current ratio
     * @param debtToEquity the companies debt to equity ratio
     *  @param currentDebtToEquity the companies current debt to equity ratio
     *  *      * @param returnOnEquity the companies return on equity ratio
     *  *      * @param returnOnAssets the companies return on assets
     *  *      * @param interestCoverage the companies interest coverage
     */
    public CompanyData(final int id, int year, double currentRatio, double returnOnEquity,
            double debtToEquity, double currentDebtToEquity,
            double returnOnAssets, double interestCoverage, final String cik) {
        this.id = id;
        this.year = year;
        this.currentRatio = currentRatio;
        this.debtToEquity = debtToEquity;
        this.currentDebtToEquity = currentDebtToEquity;
        this.returnOnEquity = returnOnEquity;
        this.returnOnAssets = returnOnAssets;
        this.interestCoverage = interestCoverage;
        this.cik = cik;
    }

    public int getId() {
        return id;
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

    public int getYear() {
        return year;
    }

    public String getCik() {
        return cik;
    }

    /**
     * Method returns the returns score of the company out of 5
     * @return The returns score out of 5
     */
    public int getReturnsScore() {
        return generateScore(0);
    }

    public int getPerformanceScore() {
        return generateScore(0);
    }

    public int getStrengthScore() {
        return generateScore(0);
    }

    public int getHealthScore() {
        return generateScore(0);
    }

    public int getSafetyScore() {
        return generateScore(0);
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
