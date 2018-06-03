package com.hixel.hixel.company;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

public interface CompanyContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        String getCompanyName();
        void setCompany(Company company);
        void doMeta();

        // String getRatio(String name, int year);
        // int getColorIndicator(String ratio, double value);
        // ArrayList<String> getRatios1();
        // void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion);
    }
}
