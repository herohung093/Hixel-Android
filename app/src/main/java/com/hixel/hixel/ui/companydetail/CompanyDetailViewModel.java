package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.entities.Company;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.User;
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

    private LiveData<Resource<List<Company>>> company;

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

        company = companyRepository.loadCompanies(ticker);
    }

    public LiveData<Resource<List<Company>>> getCompany() { return this.company; }

    /**
     * Saves the company to the users portfolio and company database.
     * @param savedCompany Company the user wants to save.
     * @param updatedUser The updated user object.
     */
    void saveCompany(Company savedCompany, User updatedUser) {
        // userRepository.updateUser(updatedUser);
        //companyRepository.saveCompany(savedCompany);
    }

    /**
     * Checks whether the Company is in the users portfolio.
     * @param usersTickers Users tickers.
     * @param companyTicker Current companies ticker.
     */
    void setIsInPortfolio(List<String> usersTickers, String companyTicker) {
        for (String t : usersTickers) {
            if (t.equals(companyTicker)) {
                // isInPortfolio = true;
            }
        }
    }

    boolean getIsInPortfolio() {
        return false; //isInPortfolio;
    }
}
