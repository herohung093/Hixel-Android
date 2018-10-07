package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

/**
 * Company is an Envelope for CompanyIdentifiers and CompanyData classes
 * allows us to format strings, calculate ratios, etc.
 */
@Entity(tableName = "companies")
public class Company {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @Embedded
    @SerializedName("identifiers")
    private CompanyIdentifiers companyIdentifiers;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public CompanyIdentifiers getCompanyIdentifiers() {
        return companyIdentifiers;
    }

    public void setCompanyIdentifiers(CompanyIdentifiers companyIdentifiers) {
        this.companyIdentifiers = companyIdentifiers;
    }

    // TODO: Do this in a nicer way, and test against a bunch of companies.
    // TODO: Get a better way of checking for null object.
    public String getFormattedName() {
        try {
            return (this.companyIdentifiers.getName()
                    .split("[\\s, ]")[0].toLowerCase()
                    .substring(0, 1).toUpperCase())
                    + this.companyIdentifiers.getName().substring(1);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public String getFormattedTicker() {
        try {
            return String.format("NASDAQ: %s", companyIdentifiers.getTicker());
        } catch (NullPointerException e) {
            return "";
        }
    }

    // NOTE: TEMPORARY METHOD FOR DISPLAY.
    public double getRatio() {
        return 10.0;
    }
}
