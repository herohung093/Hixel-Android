package com.hixel.hixel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComparisonViewModel extends ViewModel {
    private MutableLiveData<List<Company>> companies;

    public void addToCompare(String ticker) {
        // if (listCompareCompanies.size() <= 1) {
            ServerInterface client = Client
                    .getClient()
                    .create(ServerInterface.class);

            Call<ArrayList<Company>> call = client
                    .doGetCompanies(ticker, 5);

            call.enqueue(new Callback<ArrayList<Company>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Company>> call,
                        @NonNull Response<ArrayList<Company>> response) {

                    if (response.body() == null) {
                        // comparisonView.userNotification("Company not found!");
                    }
                    else {
                        try {
                            Log.d("addToCompare", Objects.requireNonNull(response.body()).get(0).getIdentifiers().getTicker());

                            // listCompareCompanies.add(Objects.requireNonNull(response.body()).get(0));

                            // comparisonView.selectedListChanged();
                        }
                        catch (Exception e) {
                            Log.e("loadDataForAParticularCompany",
                                    String.format("Failed to retrieve data for ticker: %s", ticker));

                            // comparisonView.userNotification("Company not found!");
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                    Log.d("addToCompare",
                            "Failed to load company data from the server: " + t.getMessage());
                }
            });
    }

}

