package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.hixel.hixel.data.database.CompanyDao;
import com.hixel.hixel.data.api.ServerInterface;
import com.hixel.hixel.data.entities.Company;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  Gets data from the server if the timeout has not occurred, otherwise gets it from the database.
 */
@Singleton
public class CompanyRepository {

    @SuppressWarnings("unused")
    private static final String TAG = CompanyRepository.class.getSimpleName();

    private ServerInterface serverInterface;
    private final CompanyDao companyDao;
    private final Executor executor;

    // TODO: Find a way to not use these vars
    // NOTE: THESE VARIABLES ARE TEMPORARY WORKAROUNDS
    private MutableLiveData<Company> company = new MutableLiveData<>();
    private String[] userTickers;

    @Inject
    public CompanyRepository(ServerInterface serverInterface, CompanyDao companyDao, Executor executor) {
        this.serverInterface = serverInterface;
        this.companyDao = companyDao;
        this.executor = executor;
    }

    public LiveData<List<Company>> getCompanies(String[] tickers) {
        userTickers = tickers;
        refreshCompanies(tickers); // try to refresh from the server if possible.

        return companyDao.load(); // return LiveData from the db.
    }

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
                    public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) { }
                });

        return company;
    }

    public void saveCompany(Company company) {
        companyDao.saveCompany(company);
    }

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
                        public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) { }
                    }));
    }

    public String[] getTickers() {
        return userTickers;
    }
}
