package com.hixel.hixel.company;

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

    public LinkedHashMap<String, Double> getRatios() {
        return ratios;
    }

    public void setDefaultFinancialData(){ //will be called when FinancialData is null
        ratios.put("Current Ratio",(double)0);
        ratios.put("Quick Ratio",(double)0);
        ratios.put("Cash Ratio",(double)0);
        ratios.put("Dept-to-Equity Ratio",(double)0);
        ratios.put("Health",(double)0);
        ratios.put("Long_Term_Debt_Ratio",(double)0);

    }
    public void setYear(int year) { this.year = year; } //will be called when FinancialData is null

}

