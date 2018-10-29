package com.hixel.hixel.data.entities.user;

import com.google.gson.annotations.SerializedName;

public class Ticker {
    @SerializedName("ticker")
    private String ticker;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
