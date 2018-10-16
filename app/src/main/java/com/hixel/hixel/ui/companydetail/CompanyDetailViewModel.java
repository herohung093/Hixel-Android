package com.hixel.hixel.ui.companydetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.hixel.hixel.data.UserRepository;
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
    private UserRepository userRepository;

    private MutableLiveData<Company> company;
    private LiveData<User> user;
    private boolean isInPortfolio;

    @Inject
    CompanyDetailViewModel(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Calls the UserRepository if no User exists, otherwise it returns nothing.
     */
    void init() {
        if (this.user != null) {
            return;
        }

        user = userRepository.getUser();
    }

    /**
     * Calls the CompanyRepository if no Company exists, otherwise it returns nothing.
     * @param ticker The ticker of the Company
     */
    void loadCompany(String ticker) {
        if (this.company != null) {
            return;
        }

        company = companyRepository.getCompany(ticker);
    }

    public LiveData<User> getUser() { return user; }

    public MutableLiveData<Company> getCompany() { return this.company; }

    /**
     * Saves the company to the users portfolio and company database.
     * @param savedCompany Company the user wants to save.
     * @param updatedUser The updated user object.
     */
    void saveCompany(Company savedCompany, User updatedUser) {
        userRepository.updateUser(updatedUser);
        companyRepository.saveCompany(savedCompany);
    }

    /**
     * Checks whether the Company is in the users portfolio.
     * @param usersTickers Users tickers.
     * @param companyTicker Current companies ticker.
     */
    void setIsInPortfolio(List<String> usersTickers, String companyTicker) {
        for (String t : usersTickers) {
            if (t.equals(companyTicker)) {
                isInPortfolio = true;
            }
        }
    }

    boolean getIsInPortfolio() {
        return isInPortfolio;
    }
}
