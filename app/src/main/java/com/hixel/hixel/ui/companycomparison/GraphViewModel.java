package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.Company;
import java.util.List;
import javax.inject.Inject;

public class GraphViewModel extends ViewModel {

    @SuppressWarnings("unused")
    private final String TAG = GraphViewModel.class.getSimpleName();

    private CompanyRepository repository;
    // TODO: Change to List
    private MutableLiveData<List<Company>> companies;

    @Inject
    GraphViewModel(CompanyRepository repository) {
        this.repository = repository;
    }

    void init(List<String> tickers) {
        if (this.companies != null) {
            return;
        }

        companies = repository.getComparisonCompanies(tickers);
    }

    MutableLiveData<List<Company>> getCompanies() {
        return companies;
    }

}
