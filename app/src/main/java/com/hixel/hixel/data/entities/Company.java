package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Immutable Company Entity containing all the identifying information for a company.
 */
@Entity(tableName = "companies")
public class Company implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("cik")
    private final String cik;
    @SerializedName("name")
    private final String name;
    @SerializedName("ticker")
    private final String ticker;

    public Company(@NonNull String cik, String name, String ticker) {
        this.cik = cik;
        this.name = name;
        this.ticker = ticker;
    }

    @NonNull
    public String getCik() {
        return cik;
    }

    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }

    /**
     * Method formats the companies name, ensures all companies
     * names look alike
     *
     * @return The formatted name
     */
    public String getFormattedName() {
        try {
            return (this.getName()
                    .split("[\\s, ]")[0]
                    .toLowerCase()
                    .substring(0, 1).toUpperCase()) + this.getName().substring(1);
        } catch (NullPointerException e) {
            return "";
        }
    }

    /**
     * Method formats the companies ticker, ensures all tickers
     * look alike.
     *
     * @return The formatted ticker
     */
    public String getFormattedTicker() {
        try {
            return String.format("NASDAQ: %s", getTicker());
        } catch (NullPointerException e) {
            return "";
        }
    }
}
