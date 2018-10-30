package com.hixel.hixel.data.models;

/**
 * Simple model to read in search results from the server.
 */
public class SearchEntry {
    private final String ticker;
    private final String name;
    private final String exchange;

    /**
     * Constructor for a SearchEntry object
     *
     * @param ticker the ticker of the company
     * @param name the name of the company
     * @param exchange the exchange the company is listed on
     */
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