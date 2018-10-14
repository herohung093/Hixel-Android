package com.hixel.hixel.ui.companycomparison;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.api.Client;
import com.hixel.hixel.data.api.ServerInterface;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyComparisonViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private final String TAG = getClass().getSimpleName();

    private CompanyRepository repository;
    private LiveData<List<Company>> dashboardCompanies;

    private MutableLiveData<ArrayList<Company>> companies = new MutableLiveData<>();
    private PublishSubject<String> publishSubject = PublishSubject.create();

    @Inject
    CompanyComparisonViewModel(CompanyRepository repository) {
        this.repository = repository;
    }

    public void init() {
        if (this.dashboardCompanies != null) {
            return;
        }

        Log.d(TAG, "init: HIT!");
        dashboardCompanies = repository.getCompanies(repository.getTickers());
    }


    public void loadSearchResults(String query) {
        publishSubject.onNext(query);
    }

    public void addToCompare(String ticker) {
         //if (listCompareCompanies.size() <= 1) {
            ServerInterface client = Client
                    .getClient()
                    .create(ServerInterface.class);

            Call<ArrayList<Company>> call = client
                    .doGetCompanies(ticker, 5);

            call.enqueue(new Callback<ArrayList<Company>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Company>> call,
                                       @NonNull Response<ArrayList<Company>> response) {
                    try {
                        ArrayList<Company> current = companies.getValue();
                        ArrayList<Company> temp = new ArrayList<>();

                        if (current != null && !current.isEmpty()){
                            for(int i=0;i<current.size();i++) {
                                temp.add(current.get(i));
                            }
                        }

                        temp.add(Objects.requireNonNull(response.body()).get(0));
                        companies.setValue(temp);
                    }
                    catch (Exception e) {
                        Log.e("loadDataForAParticularCompany", "Failed to retrieve data for ticker: %s" + ticker);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Company>> call, @NonNull Throwable t) {
                    Log.d("addToCompare", "Failed to load company data from the server: " + t.getMessage());
                }
            });
    }

    public MutableLiveData<ArrayList<Company>> getCompanies(){
        return companies;
    }
}

