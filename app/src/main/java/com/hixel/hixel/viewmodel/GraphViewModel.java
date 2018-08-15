package com.hixel.hixel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
import io.reactivex.annotations.NonNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphViewModel extends AndroidViewModel {
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<ArrayList<Company>> companies;
    private LiveData<ArrayList<String>> ratios;

    public GraphViewModel(@android.support.annotation.NonNull Application application) {
        super(application);
        loadRatios();
    }

    public void loadRatios(){
        ratios=doMeta();
    }
    public LiveData<ArrayList<String>> doMeta() {
        final MutableLiveData<ArrayList<String>> tempRatios = new MutableLiveData<>();
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call,
                @NonNull Response<ArrayList<String>> response) {

                tempRatios.setValue(response.body());
                // TODO: Fix this log statement
                assert tempRatios.getValue() != null;
                Log.d("ratios------------>","" + tempRatios.getValue().size());
            }
            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                Log.d("loadPortfolio",
                    "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
        return tempRatios;
    }

    public MutableLiveData<ArrayList<Company>> getCompanies() {
        return companies;
    }

    public LiveData<ArrayList<String>> getRatios() {
        return ratios;
    }
}
