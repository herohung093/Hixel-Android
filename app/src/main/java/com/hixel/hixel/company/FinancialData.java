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
}
