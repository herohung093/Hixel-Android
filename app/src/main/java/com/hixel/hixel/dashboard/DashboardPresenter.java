package com.hixel.hixel.dashboard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.api.Client;
import com.hixel.hixel.api.ServerInterface;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.models.Portfolio;
import com.hixel.hixel.search.SearchEntry;
import com.hixel.hixel.search.SearchSuggestion;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardPresenter implements DashboardContract.Presenter {

    private Portfolio portfolio;
    private final DashboardContract.View dashboardView;
    protected SearchSuggestion searchSuggestion;
    protected static ArrayList<String> names;


    DashboardPresenter(DashboardContract.View dashboardView) {
        this.dashboardView = dashboardView;
        this.dashboardView.setPresenter(this);

        this.portfolio = new Portfolio();
        this.searchSuggestion = new SearchSuggestion();

        names = new ArrayList<>();

    }

    @Override
    public void start() {
        ArrayList<String> companies = new ArrayList<>();
        companies.add("AAPL");
        companies.add("TSLA");
        companies.add("TWTR");
        companies.add("SNAP");
        companies.add("FB");
        companies.add("WFC");

        loadPortfolio(companies);
        populateGraph();
        names.add("");

    }

    @Override
    public void populateGraph() {
        //dashboardView.showMainGraph(portfolio.getCompanies());
    }

    private void loadPortfolio(ArrayList<String> companies) {
        ServerInterface client = Client
                .getRetrofit()
                .create(ServerInterface.class);

        Call<ArrayList<Company>> call = client
                .doGetCompanies(StringUtils.join(companies, ','), 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                   @NonNull Response<ArrayList<Company>> response) {

                portfolio.setCompanies(response.body());
                populateGraph();

                dashboardView.portfolioChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                Log.d("loadPortfolio",
                        "Failed to load company data from the server: " + t.getMessage());
            }
        });
    }

    public void loadSearchSuggestion(String query) {
        ServerInterface client = Client.getRetrofit().create(ServerInterface.class);
        Call<ArrayList<SearchEntry>> call = client.doSearchQuery(query);
        call.enqueue(new Callback<ArrayList<SearchEntry>>() {
            @Override
            public void onResponse(Call<ArrayList<SearchEntry>> call, Response<ArrayList<SearchEntry>> response) {
                searchSuggestion.setSearchEntries(response.body());
                names = searchSuggestion.getNames();
                if (names.size() != 0) {
                    Log.d("Search SUggstion=====", "" + names.get(0));
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
    public ArrayList<String> getnames() {
        return names;

    }


    @Override
    public ArrayList<Company> getCompanies() {
        return portfolio.getCompanies();
    }

    @Override
    public void sortCompaniesBy(String name) {
        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;

        Collections.sort(portfolio.getCompanies(),
                (c1, c2) -> Double.compare(c1.getRatio(name, last_year),
                        c2.getRatio(name, last_year)));

        Collections.reverse(portfolio.getCompanies());
    }
}
