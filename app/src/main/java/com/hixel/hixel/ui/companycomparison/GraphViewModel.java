package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.Company;
import java.util.List;
import javax.inject.Inject;

public class GraphViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private final String TAG = GraphViewModel.class.getSimpleName();

    private CompanyRepository companyRepository;
    private LiveData<List<Company>> companies;

    @Inject
    GraphViewModel(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    void init(List<String> tickers) {
        if (this.companies != null) {
            return;
        }

        companies = companyRepository.getCompanies(tickers);
    }

    LiveData<List<Company>> getCompanies() {
        return companies;
    }

    /*
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
                Log.d(TAG, "Failed to load Search suggestions from the server: " + t.getMessage());
            }
        });

        // TODO: You can't just return the result right after starting an asynchronous API call...
        return tempRatios;
    }

    public MutableLiveData<ArrayList<Company>> getCompanies() {
        return companies;
    }

    LiveData<ArrayList<String>> getRatios() {
        return ratios;
    }*/
}
