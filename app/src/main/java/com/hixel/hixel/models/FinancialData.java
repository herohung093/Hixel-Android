package com.hixel.hixel.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class FinancialData implements Serializable {
    private int year;
    private HashMap<String, BigDecimal> xbrlElements; //TODO: Evaluate whether this is needed clientside.
    private LinkedHashMap<String, Double> ratios;

    public FinancialData(int year, HashMap<String, BigDecimal> xbrlElements, LinkedHashMap<String, Double> ratios) {
        this.year = year;
        this.xbrlElements = xbrlElements;
        this.ratios = ratios;
    }

    public int getYear() {
        return year;
    }

    public HashMap<String, BigDecimal> getXbrlElements() {
        return xbrlElements;
    }

    public void setRatios(LinkedHashMap<String, Double> ratios) {
        this.ratios = ratios;
    }

    public LinkedHashMap<String, Double> getRatios() {
        return ratios;
    }

    // will be called when FinancialData is null
    public void setDefaultFinancialData() {
        ratios.put("Current Ratio", 0.0);
        ratios.put("Quick Ratio", 0.0);
        ratios.put("Cash Ratio", 0.0);
        ratios.put("Dept-to-Equity Ratio", 0.0);
        ratios.put("Health", 0.0);
        ratios.put("Long_Term_Debt_Ratio", 0.0);
    }

    // will be called when FinancialData is null
    public void setYear(int year) { this.year = year; }

}

