package com.hixel.hixel.service.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import io.reactivex.annotations.NonNull;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatiosList {
    String TAG=getClass().getSimpleName();
    public LiveData<ArrayList<String>> doMeta() {
        final MutableLiveData<ArrayList<String>> tempRatios = new MutableLiveData<>();
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call,
                @NonNull Response<ArrayList<String>> response) {

                tempRatios.setValue(response.body());
                Log.d(TAG,"respond body: "+ tempRatios.getValue().size());

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

}
