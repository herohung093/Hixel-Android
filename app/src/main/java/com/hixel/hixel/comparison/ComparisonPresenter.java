package com.hixel.hixel.comparison;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.models.FinancialData;
import com.hixel.hixel.network.Client;
import com.hixel.hixel.network.ServerInterface;
import com.hixel.hixel.search.SearchEntry;
import com.hixel.hixel.search.SearchSuggestion;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComparisonPresenter implements ComparisonContract.Presenter {
    private List<Company> listCompareCompanies = new ArrayList<>();
    private final ComparisonContract.View mComparisonView;
    private SearchSuggestion searchSuggestion;
    private static List<String> names;

    ComparisonPresenter(ComparisonContract.View mComparisonView) {
        this.mComparisonView = mComparisonView;
        listCompareCompanies.clear();
        this.searchSuggestion = new SearchSuggestion();
        names = new ArrayList<>();

    }

    public void setListCompareCompanies(List<Company> listCompareCompanies) {
        this.listCompareCompanies = listCompareCompanies;
    }

    @Override
    public void start() {
        names.add("");
    }

    public void removeCompareFromList(int position){
        listCompareCompanies.remove(position);

    }
    public void compare(){

    }
    public List<Company> getListCompareCompanies(){
        return listCompareCompanies;
    }

    public int addToCompare(String ticker) {
        final int[] successFlag = {0};
        if (listCompareCompanies.size() <= 1) {
            ServerInterface client = Client
                    .getRetrofit()
                    .create(ServerInterface.class);

            Call<ArrayList<Company>> call = client
                    .doGetCompanies(StringUtils.join(ticker, ','), 5);

            call.enqueue(new Callback<ArrayList<Company>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                       @NonNull Response<ArrayList<Company>> response) {
                    Log.d("RECEIVED COMPANY**", response.body().toString());
                    if(response.body().size()!= 0) {
                        successFlag[0] = 1;
                        Log.d("RECEIVED COMPANY", response.body().get(0).getIdentifiers().getTicker());
                        listCompareCompanies.add(response.body().get(0));

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                    Log.d("ADD COMPANY TO COMPARE",
                            "Failed to load company data from the server: " + t.getMessage());
                }
            });
        } else {
            return successFlag[0] = 2;
        }
        return successFlag[0];
    }

public void checkUpFinancialEntry(Company company){
    List<FinancialData> financialData = company.getFinancialDataEntries();

    Set<String> keys = new HashSet<>();
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

                }
            }
        }

    }
}
    @Override
    public void removeLastItemFromList() {
        if (listCompareCompanies.size()!= 0) {
            listCompareCompanies.remove(listCompareCompanies.size() - 1);
        }
    }

    @Override
    public void loadSearchSuggestion(String s) {
        ServerInterface client = Client.getRetrofit().create(ServerInterface.class);
        Call<ArrayList<SearchEntry>> call = client.doSearchQuery(s);
        call.enqueue(new Callback<ArrayList<SearchEntry>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchEntry>> call, Response<ArrayList<SearchEntry>> response) {
                searchSuggestion.setSearchEntries(response.body());
                names = searchSuggestion.getNames();
                if (names.size() != 0) {
                    Log.d("Search Suggestion=====", "" + names.get(0));
                }

            }

            @Override
            public void onFailure(Call<ArrayList<SearchEntry>> call, Throwable t) {
                Log.d("loadPortfolio",
                        "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
    }

    @Override
    public List<String> getNames() {
        return names;
    }

}
