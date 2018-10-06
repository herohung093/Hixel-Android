package com.hixel.hixel.data.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import android.support.annotation.NonNull;

/**
 * Company entity acts as an entry point to other models
 */

@Entity(tableName = "companies")
public class Company {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @Embedded
    private FinancialIdentifiers financialIdentifiers;

    public Company() { }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public FinancialIdentifiers getFinancialIdentifiers() {
        return financialIdentifiers;
    }

    public void setFinancialIdentifiers(FinancialIdentifiers financialIdentifiers) {
        this.financialIdentifiers = financialIdentifiers;
    }

    // NOTE: TEMPORARY METHOD FOR DISPLAY.
    public double getRatio() {
        return 10.0;
    }
}
