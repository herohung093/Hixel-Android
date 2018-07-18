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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyViewModel extends ViewModel {

    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<Company> company;

    // TODO: Figure out what we are returning here.
    public LiveData<String> getCompany() {
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                ArrayList<String> ratios = response.body();

                // TODO: Update the view???
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                Log.d("Search Suggestion",
                        "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });

        return null;
    }
}
