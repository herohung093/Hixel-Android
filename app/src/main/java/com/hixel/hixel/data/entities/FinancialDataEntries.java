package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

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
