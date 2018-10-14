package com.hixel.hixel.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.User;
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

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    private LiveData<List<Company>> companies;
    private LiveData<User> user;

    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public DashboardViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    void init() {
        if (user != null) {
            return;
        }

        user = userRepository.getUser();
    }

    void loadCompanies(List<String> tickers) {
        if (this.companies != null) {
            return;
        }

        companies = companyRepository.getCompanies(tickers);
    }

    public LiveData<User> getUser() {
        return user;
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

    void loadSearchResults(String query) {
        publishSubject.onNext(query);
    }
}
