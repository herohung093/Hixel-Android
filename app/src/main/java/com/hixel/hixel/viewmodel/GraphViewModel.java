package com.hixel.hixel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphViewModel extends ViewModel {
    private MutableLiveData<Company> companies;
    private MutableLiveData<List<String>> ratios;

    public void loadCompanies() {
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call,
                    @NonNull Response<ArrayList<String>> response) {

                ArrayList<String>stringArrayList=response.body();
                // setRatios(stringArrayList);
                // checkUpFinancialEntry(stringArrayList);

                assert stringArrayList != null;
                // graphView.updateRatios(ratios);

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
            }
        });
    }
}
