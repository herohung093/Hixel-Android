package com.hixel.hixel.service.models.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class CompanyEntity {

    @PrimaryKey
    @NonNull
    @SerializedName("cik")
    @Expose
    private String cik;

    @SerializedName("ticker")
    @Expose
    private String ticker;

    @SerializedName("name")
    @Expose
    private String name;

    // Constructors
    public CompanyEntity() { }

    public CompanyEntity(@NonNull String cik, String ticker, String name) {
        this.cik = cik;
        this.ticker = ticker;
        this.name = name;
    }

    // Getters
    public String getCik() { return cik; }

    public String getTicker() { return ticker; }

    public String getName() { return name; }

    // Setters
    public void setCik(String cik) { this.cik = cik; }

    public void setTicker(String ticker) { this.ticker = ticker; }

    public void setName(String name) { this.name = name; }
}
