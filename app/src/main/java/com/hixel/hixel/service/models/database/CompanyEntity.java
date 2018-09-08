package com.hixel.hixel.service.models.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class CompanyEntity {

    @PrimaryKey
    @NonNull
    private String cik;
    private String ticker;
    private String name;

    public CompanyEntity(@NonNull String cik, String ticker, String name) {
        this.cik = cik;
        this.ticker = ticker;
        this.name = name;
    }

    // Getters
    @NonNull
    public String getCik() { return cik; }

    public String getTicker() { return ticker; }

    public String getName() { return name; }

    // TEMPORARY METHODS
    public double getRatio() {
        return 10.0;
    }

    // Note: No setters as the data is read-only
}
