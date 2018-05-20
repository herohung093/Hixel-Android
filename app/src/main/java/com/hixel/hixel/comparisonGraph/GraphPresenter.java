package com.hixel.hixel.comparisonGraph;

import android.util.Log;

import com.hixel.hixel.company.FinancialData;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class GraphPresenter implements GraphContract.Presenter {
    private ArrayList<Company> companies;
    private final GraphContract.View graphView;
    private ArrayList<String> ratios;

    public GraphPresenter(GraphContract.View graphView, ArrayList<Company> companies) {
        this.graphView = graphView;
        this.companies=companies;
        this.ratios=new ArrayList<String>();
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
        ArrayList<FinancialData> financialData=company.getFinancialDataEntries();

        Set<String> keys = new HashSet<String>();
        keys.add("Current Ratio");
        keys.add("Quick Ratio");
        keys.add("Cash Ratio");
        keys.add("Debt-to-Equity Ratio");
        //Ratios.add("Health");
        keys.add("Long Term Debt-to-Equity Ratio");

        for (FinancialData f : company.getFinancialDataEntries()) {
            if(f!=null) {
                LinkedHashMap<String, Double> ratios = f.getRatios();

                for (String k : keys) {
                    if (ratios.get(k) == null) {
                        Log.d(String.valueOf(f.getYear()) + k + ": ", "NULL***");
                        ratios.put(k, (double) 0);
                        Log.d(String.valueOf(f.getYear()) + k + ": ", ratios.get(k).toString());
                    }
                }
            }

        }
    }
    @Override
    public ArrayList<Company> getCompanies() {
        return companies;
    }
}

