package com.hixel.hixel.dashboard;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;

import com.hixel.hixel.models.Company;
import java.util.List;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {
        void portfolioChanged();
    }

    interface Presenter extends BasePresenter {
        void populateGraph();
        List<String> getNames();
        void sortCompaniesBy(String name);
        void loadSearchSuggestion(String query);
        void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion);

        List<Company> getCompanies();
    }
}
