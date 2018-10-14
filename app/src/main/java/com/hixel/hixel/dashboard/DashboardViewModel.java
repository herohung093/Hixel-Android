package com.hixel.hixel.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
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


public class DashboardViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private static final String TAG = DashboardViewModel.class.getSimpleName();

    private static final String[] tickers = { "AAPL", "TSLA", "TWTR"};

    private LiveData<List<Company>> companies;
    private CompanyRepository repository;

    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public DashboardViewModel(CompanyRepository repository) {
        this.repository = repository;
    }

    void init() {
        if (this.companies != null) {
            return;
        }

        companies = repository.getCompanies(tickers);
    }

    public LiveData<List<Company>> getCompanies() {
        return this.companies;
    }


    void setupSearch(DisposableObserver<List<SearchEntry>> observer) {
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
        publishSubject.onNext(query);
    }
}
