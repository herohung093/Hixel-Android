package com.hixel.hixel.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
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
import org.apache.commons.lang3.StringUtils;

/**
 * Exposes the list of companies in the users portfolio to the dashboard screen.
 */
public class DashboardViewModel extends ViewModel {

    private CompanyRepository companyRepository;

    private LiveData<Resource<List<Company>>> companies;

    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Inject
    public DashboardViewModel(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    void loadCompanies(List<String> tickers) {
        if (this.companies != null) {
            return;
        }

        String[] inputTickers = new String[tickers.size()];
        inputTickers = tickers.toArray(inputTickers);
        companies = companyRepository.loadCompanies(StringUtils.join(inputTickers));
    }

    public LiveData<Resource<List<Company>>> getCompanies() {
        return this.companies;
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
