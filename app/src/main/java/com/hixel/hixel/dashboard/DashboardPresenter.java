package com.hixel.hixel.dashboard;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.network.ServerInterface;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.models.Portfolio;
import com.hixel.hixel.search.SearchEntry;

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

import static com.hixel.hixel.network.Client.getClient;

public class DashboardPresenter implements DashboardContract.Presenter {

    private static final String TAG = DashboardPresenter.class.getSimpleName();

    private final DashboardContract.View dashboardView;

    private Portfolio portfolio;
    private Company company;

    private ServerInterface serverInterface;
    private CompositeDisposable disposable = new CompositeDisposable();

    DashboardPresenter(DashboardContract.View dashboardView) {
        this.dashboardView = dashboardView;
        this.dashboardView.setPresenter(this);
        this.portfolio = new Portfolio();
    }

    @Override
    public void start() {
        loadPortfolio();
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
    public void search(PublishSubject<String> publishSubject) {
        disposable.add(publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .filter(s -> !s.isEmpty())
                .distinctUntilChanged()
                .switchMapSingle((Function<String, Single<List<SearchEntry>>>) s ->
                        serverInterface.doSearchQuery(s)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()))
                .subscribeWith(new DisposableObserver<List<SearchEntry>>() {

                    @Override
                    public void onNext(List<SearchEntry> searchEntries) {
                        dashboardView.showSuggestions(searchEntries);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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
        // loadDataForAParticularCompany(tickerFromSearchSuggestion);
    }

    // TODO: Implement this in a way in which the Presenter does NOT rely on a Company object
    // NOTE: This is not currently being implemented anywhere due to breaking changes
    // it will be re-implemented later in this sprint.
    // **** Could we just pass a ticker (String) to the?

    public void loadDataForAParticularCompany(String ticker) {

        ServerInterface client = getClient().create(ServerInterface.class);

        Call<ArrayList<Company>> call = client.doGetCompanies(ticker, 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                   @NonNull Response<ArrayList<Company>> response) {

                company =  Objects.requireNonNull(response.body()).get(0);
                dashboardView.goToCompanyView();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                //TODO: Add failure handling...
            }
        });
    }

    public Company getCompany()
    {
        return company;
    }

}
