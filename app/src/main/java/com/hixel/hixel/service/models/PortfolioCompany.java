package com.hixel.hixel.service.models;

public class PortfolioCompany {
    public long id;
    @SuppressWarnings("WeakerAccess")
    public String ticker;

    public PortfolioCompany(long id, String ticker)
    {
        this.id = id;
        this.ticker = ticker;
    }
}
