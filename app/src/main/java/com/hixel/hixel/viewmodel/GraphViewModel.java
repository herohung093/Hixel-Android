package com.hixel.hixel.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.network.Client;
import com.hixel.hixel.service.network.ServerInterface;
import io.reactivex.annotations.NonNull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GraphViewModel extends ViewModel {
    private final String TAG = getClass().getSimpleName();

    private MutableLiveData<ArrayList<Company>> companies;
    private MutableLiveData<ArrayList<String>> ratios;
    ArrayList<String> tempRatios= new ArrayList<>();

    public void loadCompanies(ArrayList<Company> companies) {

            this.companies=new MutableLiveData<>();
            this.companies.setValue(companies);

    }
    public void loadRatios(){
        if(ratios==null)
        this.ratios=new MutableLiveData<>();
        ratios.setValue(tempRatios);

    }
    public void doMeta() {
        ServerInterface client = Client.getClient().create(ServerInterface.class);
        Call<ArrayList<String>> call = client.doMetaQuery();
        call.enqueue(new Callback<ArrayList<String>>() {

            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call,
                @NonNull Response<ArrayList<String>> response) {

                ArrayList<String>stringArrayList=response.body();
                Log.d(TAG,"respond body: "+ stringArrayList.size());
                setRatios(stringArrayList);
//                checkUpFinancialEntry(ratios.getValue());

                // TODO: Fix this log statement
                assert stringArrayList != null;
                Log.d("ratios------------>","" + stringArrayList.size());

                //graphView.updateRatios(ratios);

            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                Log.d("loadPortfolio",
                    "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });
    }
    public void checkUpFinancialEntry(ArrayList<String> toBeCheckRatios) {
        for (Company c : companies.getValue()) {
            for (int i=0;i<c.getFinancialDataEntries().size();i++) {
                LinkedHashMap<String, Double> ratiosData = c.getFinancialDataEntries().get(i).getRatios();

                for (String k : toBeCheckRatios) {
                    if (ratiosData.get(k) == null) {
                        Log.d(String.valueOf(c.getFinancialDataEntries().get(i).getYear()) + k + ": ", "NULL***");
                        c.getFinancialDataEntries().get(i).getRatios().put(k,0.0);
                    }
                }
            }
        }
    }
    public void setRatios(ArrayList<String> ratios) {
        this.ratios.getValue().addAll(ratios);
        //this.ratios.setValue(ratios);
}

    public MutableLiveData<ArrayList<Company>> getCompanies() {
        return companies;
    }

    public MutableLiveData<ArrayList<String>> getRatios() {
        return ratios;
    }

    public ArrayList<String> getTempRatios() {
        return tempRatios;
    }
}
