package com.hixel.hixel.comparisonGraph;

import com.hixel.hixel.models.Company;

import java.util.ArrayList;

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
    public ArrayList<Company> getCompanies() {
        return companies;
    }
}

