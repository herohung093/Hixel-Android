package com.hixel.hixel.models;

import java.io.Serializable;
import java.util.Objects;

public class CompanyIdentifiers implements Serializable {
    private String ticker;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompanyIdentifiers that = (CompanyIdentifiers) o;
        return Objects.equals(ticker, that.ticker) &&
            Objects.equals(name, that.name) &&
            Objects.equals(cik, that.cik);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ticker, name, cik);
    }
}
