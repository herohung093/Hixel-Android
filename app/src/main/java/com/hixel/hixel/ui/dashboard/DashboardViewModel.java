package com.hixel.hixel.ui.dashboard;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.CompanyData;
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
import org.apache.commons.lang3.StringUtils;

/**
 * Exposes the list of companies in the users portfolio to the dashboard screen.
 */
public class DashboardViewModel extends ViewModel {

    private CompanyRepository companyRepository;

    private LiveData<Resource<List<Company>>> companies;
    private LiveData<Resource<List<CompanyData>>> companyData;

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
        companies = companyRepository.loadCompanies(StringUtils.join(inputTickers, ','));
        companyRepository.addUserTickers(tickers);
    }

    void loadCompanyData(List<String> tickers) {
        String[] inputTickers = new String[tickers.size()];
        inputTickers = tickers.toArray(inputTickers);
        companyData = companyRepository.loadCompanyData(StringUtils.join(inputTickers, ','));
    }

    public LiveData<Resource<List<Company>>> getCompanies() {
        return this.companies;
    }

    public LiveData<Resource<List<CompanyData>>> getCompanyData() {
        return companyData;
    }

    public List<Float> getChartData(List<CompanyData> companyData) {
        List<Float> aggregateScores = new ArrayList<>();

        float returnsScore = 0.01f;
        float performanceScore = 0.01f;
        float strengthScore = 0.01f;
        float healthScore = 0.01f;
        float safetyScore = 0.01f;
        int size = companyData.size();

        for (CompanyData cd : companyData) {
            returnsScore += cd.getReturnsScore();
            performanceScore += cd.getPerformanceScore();
            strengthScore += cd.getStrengthScore();
            healthScore += cd.getHealthScore();
            safetyScore += cd.getSafetyScore();
        }

        aggregateScores.add(returnsScore / size);
        aggregateScores.add(performanceScore / size);
        aggregateScores.add(strengthScore / size);
        aggregateScores.add(healthScore / size);
        aggregateScores.add(safetyScore / size);

        return aggregateScores;
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
