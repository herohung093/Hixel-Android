package com.hixel.hixel.viewmodel;

import static com.hixel.hixel.service.network.Client.getClient;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.SearchEntry;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
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

public class ComparisonViewModel extends ViewModel {

    private final String TAG = getClass().getSimpleName();


    private MutableLiveData<ArrayList<Company>> companies=new MutableLiveData<ArrayList<Company>>();
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private List<SearchEntry> searchResults = new ArrayList<>();
    private ArrayList<Company> tempCompanies = new ArrayList<Company>() ;
    public void setupSearch() {
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
    private DisposableObserver<List<SearchEntry>> getSearchObserver() {
        return new DisposableObserver<List<SearchEntry>>() {
            @Override
            public void onNext(List<SearchEntry> searchEntries) {
                searchResults = searchEntries;
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }
    public void loadSearchResults(String query) {

        publishSubject.onNext(query);

    }
    public List<SearchEntry> getSearchResults() {
        return searchResults;
    }
    public void addToCompare(String ticker) {
         //if (listCompareCompanies.size() <= 1) {
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
                        Log.d(TAG,"Respond body is NULL");
                    }
                    else {
                        try {



                                if(companies.getValue().size()==1 && tempCompanies.size()==0){
                                    tempCompanies.add(companies.getValue().get(0));
                            }
                                Log.d(TAG,"Body respond: " +Objects.requireNonNull(response.body()).size());
                                tempCompanies.add(Objects.requireNonNull(response.body()).get(0));
                                companies.setValue(tempCompanies);
                        }
                        catch (Exception e) {
                            Log.e("loadDataForAParticularCompany",
                                    String.format("Failed to retrieve data for ticker: %s", ticker));
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
    public MutableLiveData<ArrayList<Company>> getCompanies(){
        if(companies.getValue()==null){
            companies.setValue(tempCompanies);
        }
        return companies;
    }

}

