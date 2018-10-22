package com.hixel.hixel.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;

/**
 * Holds basic information about the Company, the cik, name, and ticker.
 * The cik is used as a Foreign Key for other Entities so that a relationship exists between
 * them.
 */
@Entity
public class Identifiers {
    @PrimaryKey
    @NonNull
    @SerializedName("cik")
    private String cik;
    @SerializedName("name")
    private String name;
    @SerializedName("ticker")
    private String ticker;

    public void setName(String name) {
        this.name = name;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setCik(@NonNull String cik) {
        this.cik = cik;
    }

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    @NonNull
    public String getCik() {
        return cik;
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
