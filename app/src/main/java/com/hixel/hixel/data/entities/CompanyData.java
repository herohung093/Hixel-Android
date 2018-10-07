package com.hixel.hixel.data.entities;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CompanyData {
    private int year;
    private LinkedHashMap<String, Double> ratios;
    private HashMap<String, Integer> indicators = new HashMap<>();

    public CompanyData(int year, LinkedHashMap<String, Double> ratios) {
        this.year = year;
        this.ratios = ratios;
        createIndicators();
    }

    public int getYear() {
        return year;
    }

    public LinkedHashMap<String, Double> getRatios() {
        return ratios;
    }

    private void createIndicators() {
        indicators.put("health", setScore(1, ratios.get("Current Ratio")));
        indicators.put("performance", setScore(1, ratios.get("Quick Ratio")));
        indicators.put("risk", setScore(1, ratios.get("Cash Ratio")));
        indicators.put("strength", setScore(1, ratios.get("Debt-to-Equity Ratio")));
        indicators.put("return", setScore(1, ratios.get("Long_Term_Debt_Ratio")));
    }

    // Flag = 1 for positively skewed ratio
    // TODO: Make this not suck.
    private Integer setScore(int flag, Double value) {
        if (value == null)
            return -1;

        if (flag == 1) {
            if (value < 0.5) {
                return 1;
            } else if (value < 0.8) {
                return 2;
            } else if (value < 1.0) {
                return 3;
            } else if (value < 1.3) {
                return 4;
            } else {
                return 5;
            }
        } else {
            if (value < 0.5) {
                return 5;
            } else if (value < 0.8) {
                return 4;
            } else if (value < 1.0) {
                return 3;
            } else if (value < 1.3) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    // will be called when CompanyData is null
    public void setDefaultFinancialData() {
        ratios.put("Current Ratio", 0.0);
        ratios.put("Quick Ratio", 0.0);
        ratios.put("Cash Ratio", 0.0);
        ratios.put("Dept-to-Equity Ratio", 0.0);
        ratios.put("Health", 0.0);
        ratios.put("Long_Term_Debt_Ratio", 0.0);
    }

    // will be called when CompanyData is null
    public void setYear(int year) { this.year = year; }

}

