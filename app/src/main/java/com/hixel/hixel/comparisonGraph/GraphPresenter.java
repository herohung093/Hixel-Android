package com.hixel.hixel.comparisonGraph;

import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphPresenter implements GraphContract.Presenter {
    private ArrayList<Company> companies;
    private final GraphContract.View graphView;
    private ArrayList<String> ratios;


    public GraphPresenter(GraphContract.View graphView, ArrayList<Company> companies) {
        this.graphView = graphView;
        this.companies = companies;
        this.ratios = new ArrayList<>();
        doMeta();

    }

    @Override
    public void start() {
        doMeta();
    }

    @Override
    public void checkUpFinancialEntry(ArrayList<String> toBeCheckRatios) {
        for (Company c : companies) {
            for (int i=0;i<c.getFinancialDataEntries().size();i++) {
                LinkedHashMap<String, Double> ratiosData = c.getFinancialDataEntries().get(i).getRatios();

                for (String k : toBeCheckRatios) {
                    if (ratiosData.get(k) == null) {
                        Log.d(String.valueOf(c.getFinancialDataEntries().get(i).getYear()) + k + ": ", "NULL***");
                        c.getFinancialDataEntries().get(i).getRatios().put(k,0.0);
                    }
                }
            }
        }
    }
    @Override
    public void doMeta() {
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call,
                    @NonNull Response<ArrayList<String>> response) {

                ArrayList<String>stringArrayList=response.body();
                setRatios(stringArrayList);
                checkUpFinancialEntry(stringArrayList);

                assert stringArrayList != null;
                graphView.updateRatios(ratios);

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
            }
        });
    }

    @Override
    public void setRatios(ArrayList<String> ratios) {
        this.ratios=ratios;
    }

    @Override
    public ArrayList<Company> getCompanies() {
        return companies;
    }
}

