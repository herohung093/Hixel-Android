package com.hixel.hixel.company;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.service.models.Company;
import com.hixel.hixel.service.models.SearchEntry;
import java.util.ArrayList;
import java.util.List;

public interface CompanyContract {

    interface View extends BaseView<Presenter> {
        void updateRatios(ArrayList<String> ratios1);
        void showSearchResults(List<SearchEntry> searchEntries);
        void goToCompanyView();
    }

    interface Presenter extends BasePresenter {
        String getCompanyName();
        void setCompany(Company company);
        void doMeta();

        String getRatio(String name, int year);
        int getColorIndicator(String ratio, double value);
        ArrayList<String> getRatios1();
        void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion);
        Company getCompany();

        void loadSearchResults(String query);
        void loadDataForAParticularCompany(String ticker);
    }
}
