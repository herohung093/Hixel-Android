package com.hixel.hixel.comparisonGraph;

import android.util.Log;

import com.hixel.hixel.models.FinancialData;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class GraphPresenter implements GraphContract.Presenter {
    private List<Company> companies;
    private final GraphContract.View graphView;
    private List<String> ratios;

    GraphPresenter(GraphContract.View graphView, List<Company> companies) {
        this.graphView = graphView;
        this.companies = companies;
        this.ratios = new ArrayList<>();
    }

    @Override
    public void start() {
        ratios.add("Current Ratio");
        ratios.add("Quick Ratio");
        ratios.add("Cash Ratio");
        ratios.add("Dept-to-Equity Ratio");
        ratios.add("Health");
        ratios.add("Long_Term_Debt_Ratio");
    }

    @Override
    public void checkUpFinancialEntry(Company company){
        List<FinancialData> financialData = company.getFinancialDataEntries();

        Set<String> keys = new HashSet<>();
        keys.add("Current Ratio");
        keys.add("Quick Ratio");
        keys.add("Cash Ratio");
        keys.add("Debt-to-Equity Ratio");
        //ratios.add("Health");
        keys.add("Long Term Debt-to-Equity Ratio");

        for (FinancialData f : company.getFinancialDataEntries()) {
            if(f != null) {

                LinkedHashMap<String, Double> ratios = f.getRatios();

                for (String k : keys) {
                    if (ratios.get(k) == null) {
                        Log.d(String.valueOf(f.getYear()) + k + ": ", "NULL***");
                        ratios.put(k, 0.0);
                        Log.d(String.valueOf(f.getYear()) + k + ": ", ratios.get(k).toString());
                    }
                }
            }

        }
    }

    @Override
    public List<Company> getCompanies() {
        return companies;
    }
}

