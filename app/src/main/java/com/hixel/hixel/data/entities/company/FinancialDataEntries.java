package com.hixel.hixel.data.entities.company;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Db Entity that holds the year of the entry, a list of Ratios for that year
 * (to be re-implemented), and the cik of the Company that is responsible for
 * the entries.
 */
@Entity(foreignKeys = {
                @ForeignKey(
                    entity = Identifiers.class,
                    parentColumns = "id",
                    childColumns = "identifier_id",
                    onDelete = CASCADE
        )}, indices = @Index("identifier_id")
)
public class FinancialDataEntries {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("year")
    @ColumnInfo(name="year")
    private int year;

    @ColumnInfo(name="identifier_id")
    private String identifierId;

    @Embedded
    @SerializedName("ratios")
    private Ratios ratios;


    public FinancialDataEntries(int id, int year, String identifierId, Ratios ratios) {
        this.id = id;
        this.year = year;
        this.identifierId = identifierId;
        this.ratios = ratios;
    }

    public Ratios getRatios() {
        return ratios;
    }

    public String getIdentifierId() {
        return identifierId;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setIdentifierId(String identifierId) {
        this.identifierId = identifierId;
    }

    public double overallScore() {
        return (ratios.returnOnEquityRatio
                + ratios.interestCoverageRatio
                + ratios.currentDebtToEquityRatio
                + ratios.returnOnAssetsRatio
                + ratios.debtToEquityRatio)
                / 5;
    }

    public int getReturns() {
        return generateScore(ratios.returnOnAssetsRatio);
    }

    public int getPerformance() {
        return generateScore(ratios.returnOnEquityRatio);
    }

    public int getStrength() {
        return generateScore(ratios.interestCoverageRatio);
    }

    public int getHealth() {
        return generateScore(ratios.returnOnAssetsRatio);
    }

    public int getSafety() {
        return generateScore(ratios.currentDebtToEquityRatio);
    }

    private int generateScore(double ratio) {
        int score;

        if (ratio < .5) {
            score = 1;
        } else if (ratio < 1) {
            score = 2;
        } else if (ratio < 1.5) {
            score = 3;
        } else if (ratio < 2.0) {
            score = 4;
        } else {
            score = 5;
        }

        return score;
    }
}
