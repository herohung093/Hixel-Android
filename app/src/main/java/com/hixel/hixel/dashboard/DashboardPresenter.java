package com.hixel.hixel.dashboard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.service.network.ServerInterface;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.Portfolio;
import com.hixel.hixel.service.models.SearchEntry;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

import static com.hixel.hixel.service.network.Client.getClient;

public class DashboardPresenter implements DashboardContract.Presenter {

    private static final String TAG = DashboardPresenter.class.getSimpleName();

    private final DashboardContract.View dashboardView;

    private Portfolio portfolio;
    private Company company;

    private CompositeDisposable disposable;
    private PublishSubject<String> publishSubject;

    public DashboardPresenter(DashboardContract.View dashboardView) {
        this.dashboardView = dashboardView;
        this.dashboardView.setPresenter(this);
        this.portfolio = new Portfolio();

        this.disposable = new CompositeDisposable();
        this.publishSubject = PublishSubject.create();
    }

    @Override
    public void start() {
        loadPortfolio();

        disposable.add(publishSubject
                .debounce(150, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(text -> !text.isEmpty())
                .switchMapSingle((Function<String, Single<List<SearchEntry>>>) searchTerm -> getClient()
                        .create(ServerInterface.class)
                        .doSearchQuery(searchTerm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribeWith(getSearchObserver()));
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

        Call<ArrayList<Company>> call = getClient()
                .create(ServerInterface.class)
                .doGetCompanies(StringUtils.join(companies, ','), 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                   @NonNull Response<ArrayList<Company>> response) {

                portfolio.setCompanies(response.body());
                dashboardView.getAddedCompany();

                // Setup the views with portfolio data then hide the loading indicator.
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
    public void loadSearchResults(String query) {
        publishSubject.onNext(query);
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
    public void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion) {
        //loadDataForAParticularCompany(tickerFromSearchSuggestion);
    }

    // TODO: Implement this in a way in which the Presenter does NOT rely on a Company object
    public void loadDataForAParticularCompany(String ticker) {

        ServerInterface client = getClient().create(ServerInterface.class);

        Call<ArrayList<Company>> call = client.doGetCompanies(ticker, 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                   @NonNull Response<ArrayList<Company>> response) {

                try {
                    company = Objects.requireNonNull(response.body()).get(0);
                    dashboardView.goToCompanyView();
                }
                catch (Exception e) { //TODO: Provide user-facing message when this occurs.
                    Log.e("loadDataForAParticularCompany",
                          String.format("Failed to retrieve data for ticker: %s", ticker));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                // TODO: Add failure handling...
            }
        });
    }

    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> result) {
                dashboardView.showSearchResults(result);
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

    public Company getCompany()
    {
        return company;
    }

}
