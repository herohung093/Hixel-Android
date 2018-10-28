package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hixel.hixel.AppExecutors;
import com.hixel.hixel.data.api.ApiResponse;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.database.FinancialDataEntryDao;
import com.hixel.hixel.data.database.IdentifiersDao;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.entities.company.FinancialDataEntries;
import com.hixel.hixel.data.entities.company.Identifiers;
import com.hixel.hixel.data.models.SearchEntry;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Handles requests from ViewModels for Company data. Utilises the NetworkBoundResource to
 * determine where to retrieve the data from, either the api, or db.
 *
 * Only db responses are subscribed to, allowing the db to be a single source of truth for all
 * data.
 */
@Singleton
public class CompanyRepository {

    private final ServerInterface serverInterface;
    private final AppExecutors appExecutors;
    private final IdentifiersDao identifiersDao;
    private final FinancialDataEntryDao financialDataEntryDao;

    /**
     * Constructor for the repository, creates an instance of the server, company dao, and an
     * executor to perform operations off the main UI thread.
     *
     * @param serverInterface an instance of the server interface
     */
    @Inject
    public CompanyRepository(ServerInterface serverInterface, IdentifiersDao identifiersDao,
            FinancialDataEntryDao financialDataEntryDao, AppExecutors appExecutors) {
        this.serverInterface = serverInterface;
        this.appExecutors = appExecutors;
        this.identifiersDao = identifiersDao;
        this.financialDataEntryDao = financialDataEntryDao;
    }


    public LiveData<Resource<List<Company>>> loadCompanies(String tickers) {

        return new NetworkBoundResource<List<Company>, List<Company>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<Company> item) {
                for (Company c : item) {
                    Identifiers i = c.getIdentifiers();
                    identifiersDao.insertIdentifier(i);

                    for (FinancialDataEntries financialDataEntries : c.getDataEntries()) {
                        financialDataEntries.setIdentifierId(c.getIdentifiers().getId());
                        financialDataEntryDao.insertFinancialDataEntry(financialDataEntries);
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Company> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<Company>> loadFromDb() {
                return identifiersDao.loadAllCompanies();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Company>>> createCall() {
                return serverInterface.getCompanies(tickers, 5);
            }
        }.asLiveData();
    }

    /**
     * Makes a call to grab a List of SearchEntries relating to the search term.
     *
     * @param searchTerm The user entered search term.
     * @return An observable List of Search entries;
     */
    public Single<List<SearchEntry>> search(String searchTerm) {
        return serverInterface.doSearchQuery(searchTerm);
    }
}
