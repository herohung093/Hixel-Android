package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.Resource;
import com.hixel.hixel.data.UserRepository;
import com.hixel.hixel.data.entities.company.Company;
import com.hixel.hixel.data.CompanyRepository;
import com.hixel.hixel.data.entities.user.User;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

/**
 * ViewModel for the CompanyDetail screen.
 * <p>
 * The ViewModel exposes both User and Company data to the screen. Allows a user to save the
 * Company to their portfolio.
 * </p>
 */
public class CompanyDetailViewModel extends ViewModel {

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private LiveData<Company> company;
    private LiveData<User> user;

    @Inject
    CompanyDetailViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Calls the CompanyRepository if no Company exists, otherwise it returns nothing.
     * @param ticker The ticker of the Company
     */
    void loadCompany(String ticker) {
        if (this.company != null) {
            return;
        }

        LiveData<Resource<List<Company>>> response = companyRepository.loadCompanies(ticker);
        company = Transformations.map(response,
                input -> {
                    if (input.data != null) {
                        for (Company c : input.data) {
                            if (c.getIdentifiers().getTicker().equals(ticker)) {
                                return c;
                            }
                        }
                    }

                    return null;
                });
    }

    void loadUser() {
        user = userRepository.getUser();
    }

    public LiveData<Company> getCompany() { return this.company; }

    public LiveData<User> getUser() {
        return user;
    }

    /**
     * Saves the company to the users portfolio and company database.
     * @param company Company the user wants to save.
     */
    void saveCompany(Company company) {
        List<String> ticker = new ArrayList<>();
        ticker.add(company.getIdentifiers().getTicker());
        Timber.d("ADDING TICKER %s", ticker);
        userRepository.addCompany(ticker);
    }

    /**
     * Checks whether the Company is in the users portfolio.
     */
    boolean isInPortfolio(String currentTicker, List<String> userTickers) {
        for (String ticker : userTickers) {
            if (currentTicker.equals(ticker)) {
                return true;
            }
        }

        return false;
    }
}
