package com.hixel.hixel.viewmodel;

import static com.hixel.hixel.service.network.Client.getClient;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
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


public class CompanyViewModel extends AndroidViewModel {

    private final String TAG = getClass().getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private List<SearchEntry> searchResults = new ArrayList<>();
    private MutableLiveData<Company> company= new MutableLiveData<Company>();
    private LiveData<ArrayList<String>> ratios;

    public CompanyViewModel(@NonNull Application application) {
        super(application);
        ratios=doMeta();
    }

    // TODO: Figure out what we are returning here.
    public LiveData<ArrayList<String>> doMeta() {
        final MutableLiveData<ArrayList<String>> tempRatios = new MutableLiveData<>();
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@io.reactivex.annotations.NonNull Call<ArrayList<String>> call,
                @io.reactivex.annotations.NonNull Response<ArrayList<String>> response) {

                tempRatios.setValue(response.body());
                // TODO: Fix this log statement
                assert tempRatios.getValue() != null;
                Log.d("ratios------------>","" + tempRatios.getValue().size());
            }
            @Override
            public void onFailure(@io.reactivex.annotations.NonNull Call<ArrayList<String>> call, @io.reactivex.annotations.NonNull Throwable t) {
                Log.d("loadPortfolio",
                    "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
        return tempRatios;
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
    public void loadDataForAParticularCompany(String ticker) {

        ServerInterface client = getClient().create(ServerInterface.class);

        Call<ArrayList<Company>> call = client.doGetCompanies(ticker, 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                @NonNull Response<ArrayList<Company>> response) {

                company.setValue(Objects.requireNonNull(response.body()).get(0));
                //companyView.goToCompanyView();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                //TODO: Add failure handling...
            }
        });
    }

    public MutableLiveData<Company> getCompany() {
        return company;
    }

    public void setCompany(
        Company company) {

        this.company.setValue(company);
    }

    public LiveData<ArrayList<String>> getRatios() {
        return ratios;
    }
}
