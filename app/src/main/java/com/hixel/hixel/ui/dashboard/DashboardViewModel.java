package com.hixel.hixel.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.user.User;
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

/**
 * Exposes the list of companies in the users portfolio to the dashboard screen.
 */
public class DashboardViewModel extends ViewModel {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    private LiveData<Resource<List<Company>>> companies;
    private LiveData<User> user;

    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public DashboardViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    void loadUser() {
        if (this.user != null) {
            return;
        }

        user = userRepository.getUser();
    }

    void loadCompanies(List<String> userTickers) {
        if (this.companies != null) {
            return;
        }

        companies = companyRepository.loadPortfolioCompanies(userTickers);
    }

    public LiveData<Resource<List<Company>>> getCompanies() {
        return this.companies;
    }

    public LiveData<User> getUser() {
        return user;
    }

    // ****************************************
    // *              SEARCH                  *
    // ****************************************
    void setupSearch(DisposableObserver<List<SearchEntry>> observer) {
        disposable.add(publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(text -> !text.isEmpty())
                .switchMapSingle((Function<String, Single<List<SearchEntry>>>) searchTerm ->
                        companyRepository.search(searchTerm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribeWith(observer));
    }

    void loadSearchResults(String query) {
        publishSubject.onNext(query);
    }
}
