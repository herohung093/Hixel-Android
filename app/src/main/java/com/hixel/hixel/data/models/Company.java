package com.hixel.hixel.data.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Company entity acts as an entry point to other models
 */
@Entity(tableName = "companies")
public class Company {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded
    private FinancialIdentifiers financialIdentifiers;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public FinancialIdentifiers getFinancialIdentifiers() {
        return financialIdentifiers;
    }

    public void setFinancialIdentifiers(FinancialIdentifiers financialIdentifiers) {
        this.financialIdentifiers = financialIdentifiers;
    }

    // TODO: Do this in a nicer way, and test against a bunch of companies.
    // TODO: Get a better way of checking for null object.
    public String getFormattedName() {
        try {
            return (this.financialIdentifiers.getName()
                    .split("[\\s, ]")[0].toLowerCase()
                    .substring(0, 1).toUpperCase())
                    + this.financialIdentifiers.getName().substring(1);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFormattedTicker() {
        try {
            return String.format("NASDAQ: %s", financialIdentifiers.getTicker());
        } catch (NullPointerException e) {
            return "";
        }
    }

    // NOTE: TEMPORARY METHOD FOR DISPLAY.
    public double getRatio() {
        return 10.0;
    }
}
