package com.hixel.hixel.companycomparison;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphViewModel extends AndroidViewModel {
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<ArrayList<Company>> companies;
    private LiveData<ArrayList<String>> ratios;

    public GraphViewModel(@NonNull Application application) {
        super(application);
        loadRatios();
    }

    private void loadRatios(){
        ratios = doMeta();
    }

    private LiveData<ArrayList<String>> doMeta() {
        final MutableLiveData<ArrayList<String>> tempRatios = new MutableLiveData<>();
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call,
                                   @NonNull Response<ArrayList<String>> response) {
                tempRatios.setValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                Log.d("loadPortfolio",
                    "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });

        //TODO: You can't just return the result right after starting an asynchronous API call...
        return tempRatios;
    }

    public MutableLiveData<ArrayList<Company>> getCompanies() {
        return companies;
    }

    public LiveData<ArrayList<String>> getRatios() {
        return ratios;
    }
}
