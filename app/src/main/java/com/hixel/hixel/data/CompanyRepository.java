package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.hixel.hixel.data.database.CompanyDao;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.models.SearchEntry;
import com.hixel.hixel.data.models.resources.Resource;
import com.hixel.hixel.data.models.resources.Status;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Handles calls relating to CompanyData, stores Dashboard calls in the DAO to retrieve without
 * requiring a network call. All return values are either LiveData or Observable so that updates
 * to the data will be reflected in the views.
 */
@Singleton
public class CompanyRepository {

    private ServerInterface serverInterface;
    private final CompanyDao companyDao;

    // TODO: Find a way to not use these vars
    // NOTE: THESE VARIABLES ARE TEMPORARY WORKAROUNDS
    private MutableLiveData<Company> company = new MutableLiveData<>();
    private MutableLiveData<List<Company>> comparisonCompanies = new MutableLiveData<>();

    private MutableLiveData<Resource<List<Company>>> observableCompanies = new MutableLiveData<>();

    /**
     * Constructor for the repository, creates an instance of the server, company dao, and an
     * executor to perform operations off the main UI thread.
     *
     * @param serverInterface an instance of the server interface
     * @param companyDao an instance of the company dao
     */
    @Inject
    public CompanyRepository(ServerInterface serverInterface, CompanyDao companyDao) {
        this.serverInterface = serverInterface;
        this.companyDao = companyDao;
    }
    
    
    // TODO: Temp methods here, need to integrate into original methods.
    public void getAllCompanies() {
        List<Company> loadingCompanies = null;
        
        if (observableCompanies.getValue() != null) {
            loadingCompanies = observableCompanies.getValue().data;
        }

        observableCompanies.setValue(Resource.loading(loadingCompanies));
        loadCompaniesFromDb();
        loadCompaniesFromApi();
    }

    public MutableLiveData<Resource<List<Company>>> getCompanies() {
        // Return the cached LiveData
        return observableCompanies;
    }

    private void loadCompaniesFromApi() {
        // TODO: Get tickers from the User.
        serverInterface.getCompanies("AAPL,TSLA", 1).enqueue(new Callback<ArrayList<Company>>() {
            @Override
            public void onResponse(Call<ArrayList<Company>> call,
                    Response<ArrayList<Company>> response) {
                if (response.isSuccessful()) {
                    setObservableCompaniesStatus(Status.SUCCESS, null);
                    // TODO: See if I can put the response.body() straight into method call.
                    List<Company> companies = response.body();
                    insertCompaniesToDB(companies);
                } else {
                    // There has been some network error.
                    // TODO: Need Timber for logging
                    setObservableCompaniesStatus(Status.ERROR, String.valueOf(response.code()));
                    switch (response.code()) {
                        case 404:
                            // TODO: Need Timber for logging
                            break;
                        case 500:
                            // TODO: Need Timber for logging
                            break;
                        default:
                            // TODO: Need Timber for logging
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Company>> call, Throwable t) {
                // TODO: Need Timber for logging
                setObservableCompaniesStatus(Status.ERROR, t.getMessage());
            }
        });
    }

    // TODO: Need to be able to check if the company is stale.
    private void insertCompaniesToDB(List<Company> companies) {
        new insertAsyncTask(companyDao).execute(companies);
    }

    private void loadCompaniesFromDb() {
        new AsyncTask<Void, Void, List<Company>>() {
            @Override
            protected List<Company> doInBackground(Void... a) {
                return companyDao.load();
            }
        }
    }

    private void setObservableCompaniesStatus(Status status, String message) {

    }

    private static class insertAsyncTask extends AsyncTask<List<Company>, Void, Void> {

        private CompanyDao asyncCompanyDao;


        insertAsyncTask(CompanyDao asyncCompanyDao) {
            this.asyncCompanyDao = asyncCompanyDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Company>... params) {
            asyncCompanyDao.saveCompanies(params[0]);

            return null;
        }
    }
    
    /**
     * Calls the server to refresh the observableCompanies and save them to the dao and then retrieve them
     * from the dao. Subsequent calls simply retrieve from the dao.
     *
     * @param tickers the tickers for the observableCompanies we want to retrieve.
     * @return A list of observableCompanies that can be observer for changes.
     */
    public LiveData<List<Company>> getCompanies(List<String> tickers) {
        String[] tickersArray = new String[tickers.size()];
        refreshCompanies(tickers.toArray(tickersArray)); // try to refresh from the server.

        return companyDao.load(); // return LiveData from the db.
    }

    /**
     * Retrieves a single company from the server, does not save it to the database.
     *
     * @param ticker the ticker of the company
     * @return A company object that can be observer for changes
     */
    public MutableLiveData<Company> getCompany(String ticker) {
        serverInterface.getCompanies(StringUtils.join(ticker, ','), 1)
                .enqueue(new Callback<ArrayList<Company>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Company>> call,
                            @NonNull Response<ArrayList<Company>> response) {

                            List<Company> companies = response.body();

                            if (companies != null) {
                                company.setValue(companies.get(0));
                            }
                        }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Company>> call,
                            @NonNull Throwable t) { }
                });

        return company;
    }

    /**
     * Retrieves a list of observableCompanies from the server, does not save them to the database.
     *
     * @param inputTickers the tickers to be retrieved from the server
     * @return A list of observableCompanies that can be observed for changes.
     */
    // TODO: Change to 'historical' observableCompanies
    public MutableLiveData<List<Company>> getComparisonCompanies(List<String> inputTickers) {
        String[] tickers = new String[inputTickers.size()];
        tickers = inputTickers.toArray(tickers);

        serverInterface.getCompanies(StringUtils.join(tickers, ','), 1)
                .enqueue(new Callback<ArrayList<Company>>() {
                    @Override
                    public void onResponse(@NonNull Call<ArrayList<Company>> call,
                            @NonNull Response<ArrayList<Company>> response) {
                        ArrayList<Company> companies = response.body();

                        if (companies != null) {
                            comparisonCompanies.setValue(companies);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Company>> call,
                            @NonNull Throwable t) { }
                });
        return comparisonCompanies;
    }

    /**
     * Sends a Company to the company dao to be saved into the database.
     *
     * @param company the company to be inserted
     */
    public void saveCompany(Company company) {
        executor.execute(() -> companyDao.saveCompany(company));
    }

    /**
     * Requests a List of observableCompanies from the server.
     * @param tickers The tickers for which observableCompanies we need from the server.
     */
    private void refreshCompanies(final String[] tickers) {
        // Access room off the main thread
        executor.execute(() ->
                serverInterface.getCompanies(StringUtils.join(tickers, ','), 1)
                    .enqueue(new Callback<ArrayList<Company>>() {
                        @Override
                        public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                @NonNull Response<ArrayList<Company>> response) {
                            executor.execute(() -> {
                                List<Company> companies = response.body();
                                companyDao.saveCompanies(companies);
                            });
                        }
                        @Override
                        public void onFailure(@NonNull Call<ArrayList<Company>> call,
                                @NonNull Throwable t) { }
                    }));
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
