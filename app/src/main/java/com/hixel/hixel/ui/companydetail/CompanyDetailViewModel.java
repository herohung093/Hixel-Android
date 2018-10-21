package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * ViewModel for the CompanyDetail screen.
 * <p>
 * The ViewModel exposes both User and Company data to the screen. Allows a user to save the
 * Company to their portfolio.
 * </p>
 */
public class CompanyDetailViewModel extends ViewModel {

    private CompanyRepository companyRepository;

    private LiveData<Resource<Company>> company;

    @Inject
    CompanyDetailViewModel(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    /**
     * Calls the CompanyRepository if no Company exists, otherwise it returns nothing.
     * @param ticker The ticker of the Company
     */
    void loadCompany(String ticker) {
        if (this.company != null) {
            return;
        }

        company = companyRepository.loadCompany(ticker);
    }

    public LiveData<Resource<Company>> getCompany() { return this.company; }

    /**
     * Saves the company to the users portfolio and company database.
     * @param company Company the user wants to save.
     */
    void saveCompany(Company company) {
        List<String> ticker = new ArrayList<>();
        ticker.add(company.getTicker());
        companyRepository.addUserTickers(ticker);
    }

    /**
     * Checks whether the Company is in the users portfolio.
     */
    // TODO: This is an not a good implementation.
    boolean isInPortfolio(String currentTicker) {
        for (String ticker : companyRepository.getUserTickers()) {
            if (currentTicker.equals(ticker)) {
                return true;
            }
        }

        return false;
    }
}
