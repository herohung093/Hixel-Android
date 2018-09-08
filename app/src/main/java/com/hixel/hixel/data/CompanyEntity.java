package com.hixel.hixel.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import java.util.Objects;

/**
 * Immutable Company Model.
 */

@Entity(tableName = "companies")
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

    @NonNull
    public String getCik() { return cik; }

    public String getTicker() { return ticker; }

    public String getName() { return name; }

    // NOTE: TEMPORARY METHOD FOR DISPLAY.
    public double getRatio() {
        return 10.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyEntity company = (CompanyEntity) o;
        return Objects.equals(ticker, company.ticker) &&
                Objects.equals(name, company.name) &&
                Objects.equals(cik, company.cik);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, name, cik);
    }
}
