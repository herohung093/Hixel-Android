package com.hixel.hixel.comparison;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.network.Client;
import com.hixel.hixel.network.ServerInterface;
import com.hixel.hixel.search.SearchSuggestion;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void removeLastItemFromList() {
        if (listCompareCompanies.size()!= 0) {
            listCompareCompanies.remove(listCompareCompanies.size() - 1);
        }
    }

    @Override
    public void loadSearchSuggestion(String query) {
        //searchSuggestion.query(query);
    }

    @Override
    public List<String> getNames() {
        return names;
    }

}
