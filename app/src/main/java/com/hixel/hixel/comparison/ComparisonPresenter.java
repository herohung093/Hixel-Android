package com.hixel.hixel.comparison;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.api.Client;
import com.hixel.hixel.api.ServerInterface;
import com.hixel.hixel.models.Company;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComparisonPresenter implements ComparisonContract.Presenter {
    private ArrayList<Company> listCompareCompanies= new ArrayList<Company>();
    private final ComparisonContract.View mComparisonView;

    public ComparisonPresenter(ComparisonContract.View mComparisonView) {
        this.mComparisonView=mComparisonView;
        listCompareCompanies.clear();

    }

    public void setListCompareCompanies(ArrayList<Company> listCompareCompanies) {
        this.listCompareCompanies = listCompareCompanies;
    }

    @Override
    public void start() {

    }

    public void removeCompareFromList(int position){
        listCompareCompanies.remove(position);

    }
    public void compare(){

    }
    public ArrayList<Company> getListCompareCompanies(){
        return listCompareCompanies;
    }

    public int addToCompare(String ticker) {
        final int[] successFlag = {0};
        if (listCompareCompanies.size()<=1)
        {
            ServerInterface client = Client
                    .getRetrofit()
                    .create(ServerInterface.class);

            Call<ArrayList<Company>> call = client
                    .doGetCompanies(StringUtils.join(ticker, ','), 5);

            call.enqueue(new Callback<ArrayList<Company>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                       @NonNull Response<ArrayList<Company>> response) {
                    if(response.body().size()!=0 && listCompareCompanies.size()<=2) {
                        successFlag[0] = 1;

                        listCompareCompanies.add(response.body().get(0));

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                    Log.d("ADD COMPANY TO COMPARE",
                            "Failed to load company data from the server: " + t.getMessage());
                }
            });
    } else return successFlag[0]=2;
        return successFlag[0];
}
/*
private void checkUpFinancialEntry(Company company){
    ArrayList<FinancialData> financialData=company.getFinancialDataEntries();

    for (FinancialData f : company.getFinancialDataEntries()) {
        if(f!=null) {
            LinkedHashMap<String, Double> ratios = f.getRatios();
            Set<String> keys = ratios.keySet();
            for (String k : keys) {
                if (ratios.get(k) == null) {
                    Log.d(String.valueOf(f.getYear()) + k + ": ", "NULL***");
                    ratios.replace(k, (double) -1);
                }
            }
        }

    }
}*/
    @Override
    public void removeLastItemFromList() {
        if (listCompareCompanies.size()!=0) {
            listCompareCompanies.remove(listCompareCompanies.size() - 1);
        }
    }
}
