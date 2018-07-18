package com.hixel.hixel.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardViewModel extends ViewModel {

    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<List<Company>> portfolioCompanies;

    public LiveData<List<Company>> getPortfolio() {
        if (portfolioCompanies == null) {
            portfolioCompanies = new MutableLiveData<>();
            loadPortfolio();
        }

        return portfolioCompanies;
    }


    private void loadPortfolio() {
        // Dummy data before DB is hooked up.
        String[] companies = {"AAPL", "TSLA", "TWTR", "SNAP", "FB", "AMZN"};

        Call<ArrayList<Company>> call = Client.getClient()
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
                Log.d(TAG, "Failed to load company data from server\n" + t.getMessage());
            }
        });
    }
}
