package com.hixel.hixel.dashboard;

// TODO: Get rid of the Android imports
import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.network.Client;
import com.hixel.hixel.network.ServerInterface;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.models.Portfolio;
import com.hixel.hixel.search.SearchEntry;
import com.hixel.hixel.search.SearchSuggestion;

import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardPresenter implements DashboardContract.Presenter {

    // Associated View
    private final DashboardContract.View dashboardView;

    // Associated Model(s)
    private Portfolio portfolio;
    private SearchSuggestion searchSuggestion;
    // TODO: Assess if this is needed
    private static List<String> names;

    DashboardPresenter(DashboardContract.View dashboardView) {
        this.dashboardView = dashboardView;
        this.dashboardView.setPresenter(this);
        this.portfolio = new Portfolio();
        this.searchSuggestion = new SearchSuggestion();

        names = new ArrayList<>();
    }

    @Override
    public void start() {
        loadPortfolio();
        populateGraph();

        // TODO: Figure out what this is doing
        names.add("");

    }

    @Override
    public void loadPortfolio() {
        dashboardView.setLoadingIndicator(true);
        // Dummy data before the DB is hooked up. Passing a list of tickers to the server.
        List<String> companies = new ArrayList<>();
        companies.add("AAPL");
        companies.add("TSLA");
        companies.add("TWTR");
        companies.add("SNAP");
        companies.add("FB");
        companies.add("WFC");

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

                // Setup the views with portfolio data then hide the loading indicator
                dashboardView.populateChart();
                dashboardView.setupDashboardAdapter();
                dashboardView.setLoadingIndicator(false);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                dashboardView.showLoadingError();
                Log.d("loadPortfolio",
                        "Failed to load company data from the server: " + t.getMessage());
            }
        });
    }


    @Override
    public void populateGraph() {
        //dashboardView.showMainGraph(portfolio.getCompanies());
    }

    public void loadSearchSuggestion(String query) {
        ServerInterface client = Client.getRetrofit().create(ServerInterface.class);
        Call<ArrayList<SearchEntry>> call = client.doSearchQuery(query);
        call.enqueue(new Callback<ArrayList<SearchEntry>>() {
            @Override
            public void onResponse(@NonNull  Call<ArrayList<SearchEntry>> call,
                    @NonNull Response<ArrayList<SearchEntry>> response) {
                searchSuggestion.setSearchEntries(response.body());
                names = searchSuggestion.getNames();

                if (names.size() != 0) {
                    Log.d("Search Suggestion=====", "" + names.get(0));
                }

            }

            @Override
            public void onFailure(@NonNull  Call<ArrayList<SearchEntry>> call, @NonNull Throwable t) {
                Log.d("loadSearchSuggestion",
                        "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
    }

    @Override
    public List<String> getNames() {
        return names;
    }


    @Override
    public List<Company> getCompanies() {
        return portfolio.getCompanies();
    }

    @Override
    public void sortCompaniesBy(String name) {
        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;

        portfolio.getCompanies().sort(Comparator.comparingDouble(c -> c.getRatio(name, last_year)));

        Collections.reverse(portfolio.getCompanies());
    }

    @Override
    public double getAverageRatios(String ratioName) {
        // Note - using code from the dashboard adapter. All of this will need a change at some
        // point.
        double ratio = 0;
        int last_year = Calendar.getInstance().get(Calendar.YEAR) - 1;

        for (Company c : portfolio.getCompanies()) {
            ratio += c.getRatio(ratioName, last_year);
        }

        ratio /= portfolio.getCompanies().size();

        return ratio;
    }

    // TODO: Figure out if this is needed
    @Override
    public void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion) {
        // loadDataForAParticularCompany(tickerFromSearchSuggestion);
    }

    // TODO: Implement this in a way in which the Presenter does NOT rely on a Company object
    // NOTE: This is not currently being implemented anywhere due to breaking changes
    // it will be re-implemented later in this sprint.
    /*
    public void loadDataForAParticularCompany(String ticker) {

        ServerInterface client = Client
                .getRetrofit()
                .create(ServerInterface.class);

        Call<ArrayList<Company>> call = client
                .doGetCompanies(StringUtils.join(ticker, ','), 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                   @NonNull Response<ArrayList<Company>> response) {

                // dashboardView.goToCompanyView();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {

            }
        });
    }*/
}
