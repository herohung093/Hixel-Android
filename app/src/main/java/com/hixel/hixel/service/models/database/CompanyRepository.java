package com.hixel.hixel.service.models.database;

import android.arch.lifecycle.LiveData;
import com.hixel.hixel.service.network.ServerInterface;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class CompanyRepository {

    private final ServerInterface serverInterface;
    private final CompanyDao companyDao;
    private final Executor executor;

    @Inject
    public CompanyRepository(ServerInterface serverInterface, CompanyDao companyDao, Executor executor) {
        this.serverInterface = serverInterface;
        this.companyDao = companyDao;
        this.executor = executor;
    }

    public LiveData<CompanyEntity> getCompany(String cik) {
        refreshCompany(cik); // try and refresh from the server if possible
        return companyDao.loadCompanies(); // returns live data from the db
    }

    private void refreshCompany(final String cik) {
        executor.execute(() -> {
            // Check if the company was recently fetched
            boolean companyExists = (companyDao.hasCompany(cik) != null);

            if (!companyExists) {
                serverInterface.getCompanies("x", -1).enqueue(new Callback<ArrayList<CompanyEntity>>() {

                    @Override
                    public void onResponse(Call<ArrayList<CompanyEntity>> call,
                            Response<ArrayList<CompanyEntity>> response) {
                        executor.execute(() -> {
                            ArrayList<CompanyEntity> companies = response.body();

                            // TODO: Save to Room

                        });
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CompanyEntity>> call, Throwable t) { }
                });
            }
        });
    }

}
