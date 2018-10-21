package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hixel.hixel.AppExecutors;
import com.hixel.hixel.data.api.ApiResponse;
import com.hixel.hixel.data.database.CompanyDao;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.models.SearchEntry;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

/**
 * Handles calls relating to CompanyData, stores Dashboard calls in the DAO to retrieve without
 * requiring a network call. All return values are either LiveData or Observable so that updates
 * to the data will be reflected in the views.
 */
@Singleton
public class CompanyRepository {

    private final ServerInterface serverInterface;
    private final CompanyDao companyDao;
    private final AppExecutors appExecutors;

    /**
     * Constructor for the repository, creates an instance of the server, company dao, and an
     * executor to perform operations off the main UI thread.
     *
     * @param serverInterface an instance of the server interface
     * @param companyDao an instance of the company dao
     */
    @Inject
    public CompanyRepository(ServerInterface serverInterface, CompanyDao companyDao,
            AppExecutors appExecutors) {
        this.serverInterface = serverInterface;
        this.companyDao = companyDao;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<Company>>> loadCompanies(String tickers) {
        Timber.d(tickers);

        return new NetworkBoundResource<List<Company>, List<Company>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Company> item) {
                Timber.w("Saving companies");
                companyDao.insertCompanies(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Company> data) {
                Timber.w("Is fetching: %b", data == null);
                // TODO: Add a rate limiter so we automatically fetch at an interval.
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<Company>> loadFromDb() {
                Timber.w("Getting from the db");
                return companyDao.loadCompanies();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Company>>> createCall() {
                Timber.w("Talking to the server...");
                return serverInterface.getCompanies(tickers, 1);
            }
        }.asLiveData();
    }

    /**
     * Makes a call to grab a List of SearchEntries relating to the search term.
     *
     * @param searchTerm The user entered search term.
     * @return An observable List of Search entries;
     */
    // TODO: Move this out into its own class.
    public Single<List<SearchEntry>> search(String searchTerm) {
        return serverInterface.doSearchQuery(searchTerm);
    }
}
