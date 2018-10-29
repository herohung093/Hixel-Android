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
        System.out.println("YEAR: "+getYear());
        System.out.println("health: "+getHealth() + " Perform: "+ getPerformance()+" safe: "+ getSafety()+" strength: "+getStrength()+ " return: "+getReturns());
        return ((getHealth() + getPerformance() + getReturns() + getSafety() + getStrength()) *4);

    }

    public int getReturns() {
        double ratio = ratios.dividendYield;
        int score;

        if (ratio < 0.005) {
            score = 1;
        } else if (ratio > 0.005 && ratio < 0.01) {
            score = 2;
        } else if (ratio > 0.01 && ratio < 0.05) {
            score = 3;
        } else if (ratio >0.05 && ratio < 0.1) {
            score = 4;
        } else {
            score = 5;
        }

        return score;
    }

    public int getPerformance() {
        double ratio = ratios.returnOnEquityRatio;
        int score;

        if (ratio < 0.005) {
            score = 1;
        } else if (ratio > 0.005 && ratio < 0.10) {
            score = 2;
        } else if (ratio >0.1 && ratio < 0.20) {
            score = 3;
        } else if (ratio >0.2 && ratio < 0.25) {
            score = 4;
        } else {
            score = 5;
        }

        return score;
    }

    public int getStrength() {
        double ratio = ratios.interestCoverageRatio;
        int score;

        if (ratio < 1.5) {
            score = 1;
        } else if (ratio > 1.5 && ratio < 3.0) {
            score = 2;
        } else if (ratio >3 && ratio < 4.5) {
            score = 3;
        } else if (ratio > 4.5 && ratio < 6.0) {
            score = 4;
        } else {
            score = 5;
        }

        return score;
    }

    public int getHealth() {
        double ratio = ratios.currentRatio;
        int score;

        if (ratio < 0.5) {
            score = 1;
        } else if (ratio >0.5 && ratio < 1.0) {
            score = 2;
        } else if (ratio > 1.0 && ratio < 1.5) {
            score = 3;
        } else if (ratio >1.5 && ratio < 2.0) {
            score = 4;
        } else {
            score = 5;
        }

        return score;
    }

    public int getSafety() {
        double ratio = ratios.currentDebtToEquityRatio;
        int score;

        if (ratio > 10.0) {
            score = 1;
        } else if (ratio > 4.0) {
            score = 2;
        } else if (ratio > 1.5) {
            score = 3;
        } else if (ratio > 0.5) {
            score = 4;
        } else {
            score = 5;
        }

        return score;
    }
}
