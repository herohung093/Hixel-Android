package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Db Entity that holds the year of the entry, a list of Ratios for that year
 * (to be re-implemented), and the cik of the Company that is responsible for
 * the entries.
 */
@Entity
public class FinancialDataEntries {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("year")
    private int year;

    private String identifiersCik;

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getIdentifiersCik() {
        return identifiersCik;
    }

    public void setIdentifiersCik(String identifier_cik) {
        this.identifiersCik = identifier_cik;
    }
}
