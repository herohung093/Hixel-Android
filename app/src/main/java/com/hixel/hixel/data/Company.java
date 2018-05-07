package com.hixel.hixel.data;


import java.io.Serializable;

// Implementing serializable while we can to prevent coupling with the android SDK
public class Company implements Serializable {

    private String name;
    private String ticker;
    private double health;
    private double liquidity;
    private double leverage;

    public Company(String name, String ticker, double health) {
        this.name = name;
        this.ticker = ticker;
        this.health = health;
        this.liquidity = 1.5;
        this.leverage = 1.5;
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

    public double getLiquidity() {
        return this.liquidity;
    }

    public double getLeverage() {
        return this.leverage;
    }
}
