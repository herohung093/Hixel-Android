package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
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
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class CompanyComparisonViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private final String TAG = CompanyComparisonViewModel.class.getSimpleName();

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    private LiveData<List<Company>> dashboardCompanies;
    private LiveData<User> user;

    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();

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

    public LiveData<List<Company>> getDashboardCompanies() {
        return dashboardCompanies;
    }


    public void setupSearch(DisposableObserver<List<SearchEntry>> observer) {
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

    public void loadSearchResults(String query) {
        Log.d(TAG, "loadSearchResults: " + query);
        publishSubject.onNext(query);
    }
}

