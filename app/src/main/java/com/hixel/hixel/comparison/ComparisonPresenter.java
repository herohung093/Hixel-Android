package com.hixel.hixel.comparison;

import static com.hixel.hixel.network.Client.getRetrofit;

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
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComparisonPresenter implements ComparisonContract.Presenter {
    private List<Company> listCompareCompanies = new ArrayList<>();
    private final ComparisonContract.View mComparisonView;

    private static List<String> names;
    private CompositeDisposable disposable;
    private PublishSubject<String> publishSubject;
    ComparisonPresenter(ComparisonContract.View mComparisonView) {
        this.mComparisonView = mComparisonView;
        listCompareCompanies.clear();

        names = new ArrayList<>();
        disposable = new CompositeDisposable();
        publishSubject = PublishSubject.create();

    }

    public void setListCompareCompanies(List<Company> listCompareCompanies) {
        this.listCompareCompanies = listCompareCompanies;
    }

    @Override
    public void start() {
        names.add("");

        disposable.add(publishSubject
            .debounce(50, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter(text -> !text.isEmpty())
            .switchMapSingle((Function<String, Single<ArrayList<SearchEntry>>>) searchTerm -> getRetrofit()
                .create(ServerInterface.class)
                .doSearchQuery(searchTerm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
            .subscribeWith(getSearchObserver()));
    }

    public void removeCompareFromList(int position){
        listCompareCompanies.remove(position);

    }
    public void compare(){

    }

    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> result) {
                mComparisonView.searchResultReceived(result);
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
    public List<Company> getListCompareCompanies(){
        return listCompareCompanies;
    }

    public void addToCompare(String ticker) {

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

                    if(response.body()==null)
                    {
                        mComparisonView.userNotification("Company not found");

                    }else {
                        Log.d("RECEIVED COMPANY",
                            response.body().get(0).getIdentifiers().getTicker());
                        listCompareCompanies.add(response.body().get(0));
                        mComparisonView.selectedListChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                    Log.d("ADD COMPANY TO COMPARE",
                            "Failed to load company data from the server: " + t.getMessage());
                }
            });
        }else mComparisonView.userNotification("reach limit !");

    }

    @Override
    public void removeLastItemFromList() {
        if (listCompareCompanies.size()!= 0) {
            listCompareCompanies.remove(listCompareCompanies.size() - 1);
        }
    }

    @Override
    public void loadSearchResult(String query) {
        publishSubject.onNext(query);
    }


    @Override
    public List<String> getNames() {
        return names;
    }

}
