package com.hixel.hixel.viewmodel;

import static com.hixel.hixel.service.network.Client.getClient;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.hixel.hixel.R;
import com.hixel.hixel.SnackbarMessage;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.SearchEntry;
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
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<List<Company>> portfolioCompanies;
    private CompositeDisposable disposable = new CompositeDisposable();
    private PublishSubject<String> publishSubject = PublishSubject.create();
    private List<SearchEntry> searchResults = new ArrayList<>();

    private final SnackbarMessage mSnackbarText = new SnackbarMessage();

    public final ObservableBoolean progressVisible = new ObservableBoolean();


    public LiveData<List<Company>> getPortfolio() {
        if (portfolioCompanies == null) {
            portfolioCompanies = new MutableLiveData<>();
            loadPortfolio();
        }

        return portfolioCompanies;
    }

    private void loadPortfolio() {
        progressVisible.set(true);

        // TODO: Create a repository for this data to ease communication.
        String[] companies = {"AAPL", "TSLA", "TWTR", "SNAP", "FB", "AMZN"};

        Call<ArrayList<Company>> call = getClient()
                .create(ServerInterface.class)
                .doGetCompanies(StringUtils.join(companies, ','), 1);

        call.enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Company>> call,
                    @NonNull Response<ArrayList<Company>> response) {
                portfolioCompanies.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                mSnackbarText.setValue(R.string.load_error_message);
            }
        });

        progressVisible.set(false);
    }

    public void setupSearch() {
        disposable.add(publishSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .filter(text -> !text.isEmpty())
                .switchMapSingle((Function<String, Single<List<SearchEntry>>>) searchTerm -> getClient()
                        .create(ServerInterface.class)
                        .doSearchQuery(searchTerm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribeWith(getSearchObserver()));
    }

    public void loadSearchResults(String query) {
        publishSubject.onNext(query);
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

    public List<SearchEntry> getSearchResults() {
        return searchResults;
    }

    public SnackbarMessage getSnackbarMessage() {
        return mSnackbarText;
    }
}
