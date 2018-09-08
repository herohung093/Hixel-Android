package com.hixel.hixel.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * Immutable Company Model.
 */

@Entity(tableName = "companies")
public class CompanyEntity {

    @PrimaryKey
    @NonNull
    @SerializedName("ticker")
    @Expose
    private String ticker;

    @SerializedName("name")
    @Expose
    private String name;

    public CompanyEntity(String ticker, String name) {
        this.ticker = ticker;
        this.name = name;
    }

    @NonNull
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
                Objects.equals(name, company.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, name);
    }
}
