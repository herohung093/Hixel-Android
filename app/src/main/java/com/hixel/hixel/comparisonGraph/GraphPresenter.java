package com.hixel.hixel.comparisonGraph;

import android.util.Log;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.models.FinancialData;
import com.hixel.hixel.network.Client;
import com.hixel.hixel.network.ServerInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphPresenter implements GraphContract.Presenter {
    private List<Company> companies;
    private final GraphContract.View graphView;
    private ArrayList<String> ratios;


    GraphPresenter(GraphContract.View graphView, List<Company> companies) {
        this.graphView = graphView;
        this.companies = companies;
        this.ratios = new ArrayList<>();
    }

    @Override
    public void start() {
        doMeta();
    }

    @Override
    public void checkUpFinancialEntry(Company company){
        List<FinancialData> financialData = company.getFinancialDataEntries();
/*
        Set<String> keys = new HashSet<>();
        keys.add("Current Ratio");
        keys.add("Quick Ratio");
        keys.add("Cash Ratio");
        keys.add("Debt-to-Equity Ratio");
        //ratios.add("Health");
        keys.add("Long Term Debt-to-Equity Ratio");
*/
        for (FinancialData f : company.getFinancialDataEntries()) {
            LinkedHashMap<String, Double> ratios = f.getRatios();

            for (String k : this.ratios) {
                if (ratios.get(k) == null) {
                    Log.d(String.valueOf(f.getYear()) + k + ": ", "NULL***");
                    ratios.put(k, 0.0);
                    Log.d(String.valueOf(f.getYear()) + k + ": ", ratios.get(k).toString());
                }
            }
        }
    }

    @Override
    public void doMeta() {
        ServerInterface client = Client.getRetrofit().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                //searchSuggestion.setSearchEntries(response.body());
                //names = searchSuggestion.getNames();
                //if (names.size() != 0) {
                //  Log.d("Search SUggstion=====", "" + names.get(0));
                //}
                ArrayList<String>stringArrayList=response.body();
                ratios=stringArrayList;
                Log.d("ratios------------>",""+stringArrayList.size());
                graphView.updateRatios(ratios);



            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("loadPortfolio",
                        "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
    }

    @Override
    public ArrayList<String> getRatios() {
        return ratios;
    }

    @Override
    public List<Company> getCompanies() {
        return companies;
    }
}

