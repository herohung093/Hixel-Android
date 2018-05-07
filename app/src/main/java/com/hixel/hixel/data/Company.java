package com.hixel.hixel.data;


import java.io.Serializable;

// Implementing serializable while we can to prevent coupling with the android SDK
public class Company implements Serializable {

    private String name;
    private String ticker;
    private double health;

    public Company(String name, String ticker, double health) {
        this.name = name;
        this.ticker = ticker;
        this.health = health;
    }

    public String getName() {
        return this.name;
    }

    public String getTicker() {
        return this.ticker;
    }

    public double getHealth() {
        return this.health;
    }
}
