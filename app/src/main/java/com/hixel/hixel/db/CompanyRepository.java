package com.hixel.hixel.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.hixel.hixel.db.local.CompanyDao;
import com.hixel.hixel.db.remote.ServerInterface;
import com.hixel.hixel.service.models.database.CompanyEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Tells Dagger to only construct the class once.
@Singleton
public class CompanyRepository {

    private static int FRESH_TIMEOUT_IN_MINUTES = 5;

    private ServerInterface serverInterface;
    private final CompanyDao companyDao;
    private final Executor executor;

    @Inject
    public CompanyRepository(ServerInterface serverInterface, CompanyDao companyDao, Executor executor) {
        this.serverInterface = serverInterface;
        this.companyDao = companyDao;
        this.executor = executor;
    }

    public LiveData<List<CompanyEntity>> getCompanies(String[] tickers) {
        refreshCompanies(tickers); // try to refresh from the server if possible.

        return companyDao.load(); // return LiveData from the db.
    }

    // TODO: Check if update occurred recently.
    private void refreshCompanies(final String[] tickers) {

        executor.execute(() -> {
            // TODO: Check if the companies were recently fetched.
            boolean companiesExist = true;

            if (!companiesExist) {
                // TODO: check efficiency of join.
                serverInterface.getCompanies(StringUtils.join(tickers, ','), 1)
                        .enqueue(new Callback<ArrayList<CompanyEntity>>() {
                            @Override
                            public void onResponse(Call<ArrayList<CompanyEntity>> call,
                                    Response<ArrayList<CompanyEntity>> response) {
                                executor.execute(() -> {
                                    List<CompanyEntity> companies = response.body();
                                    companyDao.saveCompanies(companies);
                                });
                            }

                            @Override
                            public void onFailure(Call<ArrayList<CompanyEntity>> call,
                                    Throwable t) {
                                // TODO: Handle errors.
                            }
                        });
            }
        });
    }

}
