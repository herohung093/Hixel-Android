package com.hixel.hixel.company;

import java.io.Serializable;

public class CompanyIdentifiers implements Serializable {
    private  String ticker;
    private String name;
    private String cik;


    public CompanyIdentifiers(String ticker, String name, String cik) {
        this.ticker = ticker;
        this.name = name;
        this.cik = cik;
    }

    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }

    public String getCik() {
        return cik;
    }

}
