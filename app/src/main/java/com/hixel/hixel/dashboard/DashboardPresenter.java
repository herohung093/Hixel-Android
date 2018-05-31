package com.hixel.hixel.dashboard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.network.ServerInterface;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.models.Portfolio;
import com.hixel.hixel.search.SearchEntry;

import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hixel.hixel.network.Client.getClient;

public class DashboardPresenter implements DashboardContract.Presenter {

    private static final String TAG = DashboardPresenter.class.getSimpleName();

    private final DashboardContract.View dashboardView;
    private Portfolio portfolio;

    private ServerInterface serverInterface;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();


    DashboardPresenter(DashboardContract.View dashboardView) {
        this.dashboardView = dashboardView;
        this.dashboardView.setPresenter(this);
        this.portfolio = new Portfolio();
    }

    @Override
    public void start() {
        loadPortfolio();
        populateGraph();
    }

    @Override
    public void loadPortfolio() {

        dashboardView.showLoadingIndicator(true);
        // Dummy data before the DB is hooked up. Passing a list of tickers to the server.
        List<String> companies = new ArrayList<>();
        companies.add("AAPL");
        companies.add("TSLA");
        companies.add("TWTR");
        companies.add("SNAP");
        companies.add("FB");
        companies.add("AMZN");


        serverInterface = getClient().create(ServerInterface.class);

        Call<ArrayList<Company>> call = serverInterface
                .doGetCompanies(StringUtils.join(companies, ','), 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                   @NonNull Response<ArrayList<Company>> response) {

                portfolio.setCompanies(response.body());

                // Setup the views with portfolio data then hide the loading indicator
                dashboardView.populateChart();
                dashboardView.setupDashboardAdapter();
                dashboardView.showLoadingIndicator(false);
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

    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> result) {
                dashboardView.searchResultReceived(result);
            }

            @Override
            public void onError(Throwable e) {
                Log.e("SearchObserver", e.getMessage());
            }

            @Override
            public void onComplete() {
                //What a gorgeous little stub.
            }
        };
    }

    @Override
    public void loadSearchResult(String searchTerm) {
        publishSubject.onNext(searchTerm);
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


    // TODO: Figure out if this is needed
    @Override
    public void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion) {
        // loadDataForAParticularCompany(tickerFromSearchSuggestion);
    }

}
