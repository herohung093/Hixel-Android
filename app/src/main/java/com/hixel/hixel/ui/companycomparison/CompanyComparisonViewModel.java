package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.User;
import com.hixel.hixel.data.models.SearchEntry;
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
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyComparisonViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private final String TAG = CompanyComparisonViewModel.class.getSimpleName();

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    private LiveData<List<Company>> dashboardCompanies;
    private LiveData<User> user;

    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();

    private MutableLiveData<List<Company>> comparisonCompanies = new MutableLiveData<>();

    @Inject
    CompanyComparisonViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public void init() {
        if (this.user != null) {
            return;
        }

        user = userRepository.getUser();
    }

    void loadDashboardCompanies(List<String> tickers) {
        if (this.dashboardCompanies != null) {
            return;
        }

        dashboardCompanies = companyRepository.getCompanies(tickers);
    }

    public LiveData<User> getUser() {
        return user;
    }

    LiveData<List<Company>> getDashboardCompanies() {
        return dashboardCompanies;
    }

    LiveData<List<Company>> getComparisonCompanies() {
        return comparisonCompanies;
    }

    void setupSearch(DisposableObserver<List<SearchEntry>> observer) {
        Log.d(TAG, "setupSearch: HERE");
        disposable.add(publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(text -> !text.isEmpty())
                .switchMapSingle((Function<String, Single<List<SearchEntry>>>) searchTerm -> Client.getClient()
                        .create(ServerInterface.class)
                        .doSearchQuery(searchTerm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribeWith(observer));
    }

    void loadSearchResults(String query) {
        Log.d(TAG, "loadSearchResults: " + query);
        publishSubject.onNext(query);
    }

    void addToComparisonCompanies(String ticker) {
        Client.getClient().create(ServerInterface.class)
                .doGetCompanies(ticker, 1)
                .enqueue(new Callback<ArrayList<Company>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Company>> call,
                            @NonNull Response<ArrayList<Company>> response) {
                        List<Company> temp = response.body();
                        comparisonCompanies.setValue(temp);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) { }
                });
    }
}

