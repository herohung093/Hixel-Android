package com.hixel.hixel.ui.companycomparison;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.entities.User;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class CompanyComparisonViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private final String TAG = CompanyComparisonViewModel.class.getSimpleName();

    private CompanyRepository companyRepository;
    private UserRepository userRepository;

    private LiveData<List<Company>> dashboardCompanies;
    private LiveData<User> user;

    private MutableLiveData<ArrayList<Company>> companies = new MutableLiveData<>();
    private PublishSubject<String> publishSubject = PublishSubject.create();

    @Inject
    CompanyComparisonViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public void init() {
        if (this.user != null) {
            return;
        }

        user = userRepository.getUser();
    }


    void loadDashboardCompanies(List<String> tickers) {
        if (this.dashboardCompanies != null) {
            return;
        }

        dashboardCompanies = companyRepository.getCompanies(tickers);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<List<Company>> getDashboardCompanies() {
        return dashboardCompanies;
    }


    void loadSearchResults(String query) {
        publishSubject.onNext(query);
    }

    /*
    void addToCompare(String ticker) {
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
    }*/

    public MutableLiveData<ArrayList<Company>> getCompanies(){
        return companies;
    }
}

