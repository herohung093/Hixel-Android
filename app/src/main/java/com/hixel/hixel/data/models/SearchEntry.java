package com.hixel.hixel.data.models;

/**
 * Simple model to read in search results from the server.
 */
public class SearchEntry {
    private String ticker;
    private String name;
    private String exchange;

    public SearchEntry(String ticker, String name, String exchange) {
        this.ticker = ticker;
        this.name = name;
        this.exchange = exchange;
    }

    public String getTicker() {
        return ticker;
    }

    public String getName() {
        return name;
    }

    public String getExchange() {
        return exchange;
    }

    @Override
    public String toString() {
        return name + "    " + exchange + ":" + ticker;
    }
}