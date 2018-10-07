package com.hixel.hixel.data.entities;

public class FinancialIdentifiers {

    private String ticker;
    private String name;
    private String cik;

    public String getTicker() {
        return this.ticker;
    }

    public String getName() {
        return this.name;
    }

    public String getCik() { return this.cik; }

    public void setTicker(String ticker) { this.ticker = ticker; }

    public void setName(String name) { this.name = name; }

    public void setCik(String cik) { this.cik = cik; }
}