package com.hixel.hixel.comparison;

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

import static com.hixel.hixel.network.Client.getClient;

public class ComparisonPresenter implements ComparisonContract.Presenter {
    private List<Company> listCompareCompanies = new ArrayList<>();
    private final ComparisonContract.View comparisonView;

    private CompositeDisposable disposable;
    private PublishSubject<String> publishSubject;

    ComparisonPresenter(ComparisonContract.View mComparisonView) {
        this.comparisonView = mComparisonView;
        listCompareCompanies.clear();

        disposable = new CompositeDisposable();
        publishSubject = PublishSubject.create();

    }

    /*
    public void setListCompareCompanies(List<Company> listCompareCompanies) {
        this.listCompareCompanies = listCompareCompanies;
    }*/

    @Override
    public void start() {
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

    /*
    public void removeCompareFromList(int position){
        listCompareCompanies.remove(position);

    }

    public void compare(){

    }*/

    public List<Company> getListCompareCompanies(){
        return listCompareCompanies;
    }

    public void addToCompare(String ticker) {

        if (listCompareCompanies.size() <= 1) {
            ServerInterface client = Client
                    .getClient()
                    .create(ServerInterface.class);

            Call<ArrayList<Company>> call = client
                    .doGetCompanies(ticker, 5);

            call.enqueue(new Callback<ArrayList<Company>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                       @NonNull Response<ArrayList<Company>> response) {

                    if (response.body() == null) {
                        comparisonView.userNotification("Company not found!");
                    }
                    else {
                        try {
                            Log.d("addToCompare", Objects.requireNonNull(response.body()).get(0).getIdentifiers().getTicker());

                            listCompareCompanies.add(Objects.requireNonNull(response.body()).get(0));

                            comparisonView.selectedListChanged();
                        }
                        catch (Exception e) {
                            Log.e("loadDataForAParticularCompany",
                                  String.format("Failed to retrieve data for ticker: %s", ticker));

                            comparisonView.userNotification("Company not found!");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                    Log.d("addToCompare",
                            "Failed to load company data from the server: " + t.getMessage());
                }
            });
        }
        else {
            comparisonView.userNotification("Can only compare 2 companies!");
        }

    }

    /*
    @Override
    public void removeLastItemFromList() {
        if (listCompareCompanies.size()!= 0) {
            listCompareCompanies.remove(listCompareCompanies.size() - 1);
        }
    }*/

    @Override
    public void loadSearchResults(String query) {
        publishSubject.onNext(query);
    }

    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> result) {
                comparisonView.showSearchResults(result);
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
