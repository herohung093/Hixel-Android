package com.hixel.hixel.data.entities.company;

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
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("ticker")
    public String ticker;

    public Identifiers(@NonNull String id, String name, String ticker) {
        this.id = id;
        this.name = name;
        this.ticker = ticker;
    }

    /**
     * Method formats the companies name, ensures all companies
     * names look alike
     *
     * @return The formatted name
     */
    public String getFormattedName() {
        try {
            return (this.name
                    .split("[\\s, ]")[0]
                    .toLowerCase()
                    .substring(0, 1).toUpperCase()) + this.name.substring(1);
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
            return String.format("NASDAQ: %s", this.ticker);
        } catch (NullPointerException e) {
            return "";
        }
    }
}
