package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.user.User;
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
 * ViewModel for interacting with the ComparisonActivity, pulls data from the
 * User and Company repositories to display to the user.
 *
 * NOTE: No data should be saved into a DAO.
 */
public class CompanyComparisonViewModel extends ViewModel {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private LiveData<Resource<List<Company>>> dashboardCompanies;
    private LiveData<User> user;
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<List<Company>> comparisonCompanies = new MutableLiveData<>();
    private List<Company> compCompanies = new ArrayList<>();

    @Inject
    CompanyComparisonViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Method to be called after instantiation, checks to see if a user object exists
     * if not it fetches the user from the repository.
     */
    void init() {
        if (this.user != null) {
            return;
        }

        user = userRepository.getUser();
    }

    /**
     * Method checks if the dashboard companies object exists, if not it fetches the
     * data from the repository.
     * @param tickers The list of dashboard companies to be fetched
     */
    void loadDashboardCompanies(List<String> tickers) {
        if (this.dashboardCompanies != null) {
            return;
        }
        String[] inputTickers = new String[tickers.size()];
        inputTickers = tickers.toArray(inputTickers);
        dashboardCompanies = companyRepository.loadCompanies(StringUtils.join(inputTickers, ','));
    }

    public LiveData<User> getUser() {
        return user;
    }

    LiveData<Resource<List<Company>>> getDashboardCompanies() {
        return dashboardCompanies;
    }

    LiveData<List<Company>> getComparisonCompanies() {
        return comparisonCompanies;
    }

    List<Company> getCompCompanies() {
        return compCompanies;
    }

    /**
     * Method sets up the search for the Activity
     * @param observer A list of search entries wrapped in a DisposableObserver
     */
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

    void addToComparisonCompanies(List<String> tickersList) {
        String[] tickers = new String[tickersList.size()];
        tickers = tickersList.toArray(tickers);

        //Client.getClient().create(ServerInterface.class)
                //.getCompanies(StringUtils.join(tickers, ','), 1)
                /*
                .enqueue(new Callback<ArrayList<Company>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Company>> call,
                            @NonNull Response<ArrayList<Company>> response) {


                        List<Company> current = comparisonCompanies.getValue();
                        ArrayList<Company> temp = new ArrayList<>();

                        if (current != null && !current.isEmpty()){
                            for(int i=0;i<current.size();i++)
                            {
                                temp.add(current.get(i));
                            }
                        }
                        temp.add(Objects.requireNonNull(response.body()).get(0));
                        comparisonCompanies.setValue(temp);
                    }
                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Company>> call,
                            @NonNull Throwable t) { }
                });*/
    }

}

