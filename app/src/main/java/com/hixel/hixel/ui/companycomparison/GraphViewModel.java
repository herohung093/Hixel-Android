package com.hixel.hixel.ui.companycomparison;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.entities.company.Company;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 * Exposes the Companies the user wishes to compare.
 */
public class GraphViewModel extends ViewModel {

    private CompanyRepository repository;
    private LiveData<Resource<List<Company>>> companies;

    @Inject
    GraphViewModel(CompanyRepository repository) {
        this.repository = repository;
    }

    /**
     * Checks if the companies exist, if not fetches them from the repository.
     * @param tickers The list of tickers for the companies to be compared.
     */
    void init(List<String> tickers) {
        if (this.companies != null) {
            return;
        }

        String[] inputTickers = new String[tickers.size()];
        inputTickers = tickers.toArray(inputTickers);
        companies = repository.loadCompanies(StringUtils.join(inputTickers, ','));

    }

    LiveData<Resource<List<Company>>> getCompanies() {
        return companies;
    }

}
