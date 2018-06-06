package com.hixel.hixel.company;

import static com.hixel.hixel.network.Client.getClient;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.network.Client;
import com.hixel.hixel.network.ServerInterface;

import com.hixel.hixel.search.SearchEntry;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyPresenter implements CompanyContract.Presenter {

    private Company company;
    private final CompanyContract.View companyView;
    private static ArrayList<String> ratios1;

    private CompositeDisposable disposable;
    private PublishSubject<String> publishSubject;

    CompanyPresenter(CompanyContract.View companyView) {
        this.companyView = companyView;
        companyView.setPresenter(this);

        ratios1 = new ArrayList<>();
        this.disposable = new CompositeDisposable();
        this.publishSubject = PublishSubject.create();
    }


    public void start() {
        doMeta();

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

    public String getRatio(String ratio, int year) {
        Double value = company.getRatio(ratio, year);

        return (value == null) ? "N/A" : Double.toString(value);
    }

    public Company getCompany() {
        return company;
    }

    public int getColorIndicator(String ratio, double value) {
        //Default thresholds.
        double green = 1.5;
        double yellow = 1.0;

        //Specified thresholds for different ratios.
        switch (ratio) {
            case "Health":
                green = 0.7;
                yellow = 0.5;
                break;

            //TODO: Add other ratio thresholds.
        }

        return Color.parseColor((value > green) ? "#C23934":
                (value > yellow)? "#FFB75D":
                        "#4BCA81");
    }

    @Override
    public void doMeta() {
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull  Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                ArrayList<String>stringArrayList = response.body();
                ratios1=stringArrayList;

                assert stringArrayList != null;

                companyView.updateRatios(ratios1);

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                Log.d("Search Suggestion",
                        "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
    }

    @Override
    public ArrayList<String> getRatios1() {
        return ratios1;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getCompanyName() {
        return company.getIdentifiers().getName();
    }

    /*
    public void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion) {
        this.tickerFromSearchSuggestion = tickerFromSearchSuggestion;
        //loadDataForAParticularCompany(tickerFromSearchSuggestion);
    }*/

    @Override
    public void loadSearchResults(String query) {
        publishSubject.onNext(query);
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

                company =  Objects.requireNonNull(response.body()).get(0);
                companyView.goToCompanyView();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                //TODO: Add failure handling...
            }
        });
    }

    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> result) {
                companyView.showSearchResults(result);
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

}
