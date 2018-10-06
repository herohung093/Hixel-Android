package com.hixel.hixel.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.data.database.CompanyDao;
import com.hixel.hixel.data.models.Company;
import com.hixel.hixel.data.api.ServerInterface;
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

    private static int FRESH_TIMEOUT_IN_MINUTES = 5;

    private ServerInterface serverInterface;
    private final CompanyDao companyDao;
    private final Executor executor;

    // NOTE: This is a temporary workaround
    private MutableLiveData<Company> company = new MutableLiveData<>();

    @Inject
    public CompanyRepository(ServerInterface serverInterface, CompanyDao companyDao, Executor executor) {
        this.serverInterface = serverInterface;
        this.companyDao = companyDao;
        this.executor = executor;
    }

    public LiveData<List<Company>> getCompanies(String[] tickers) {
        refreshCompanies(tickers); // try to refresh from the server if possible.

        return companyDao.load(); // return LiveData from the db.
    }

    // TODO: Check the effects of not having an executor here.
    public MutableLiveData<Company> getCompany(String ticker) {
        //executor.execute(() -> {
            serverInterface.getCompanies(StringUtils.join(ticker, ','), 1)
                    .enqueue(new Callback<ArrayList<Company>>() {
                        @Override
                        public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                @NonNull Response<ArrayList<Company>> response) {
                            //executor.execute(() -> {
                                List<Company> companies = response.body();

                                company.setValue(companies.get(0));
                            //});
                        }

                        @Override
                        public void onFailure(@NonNull Call<ArrayList<Company>> call,
                                @NonNull Throwable t) {
                            // TODO: Handle errors.
                        }
                    });
        //});

        return company;
    }

    public void saveCompany(Company company) {
        companyDao.saveCompany(company);
    }

    // TODO: Check if update occurred recently.
    private void refreshCompanies(final String[] tickers) {

        executor.execute(() -> {
            // TODO: Check if the companies were recently fetched.
            boolean companiesExist = true;
            if (companiesExist) {
                // TODO: check efficiency of join.
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
                                    @NonNull Throwable t) {
                                // TODO: Handle errors.
                            }
                        });
            }
        });
    }


}
