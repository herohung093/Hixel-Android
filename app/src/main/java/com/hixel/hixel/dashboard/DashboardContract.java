package com.hixel.hixel.dashboard;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.search.SearchEntry;

import java.util.List;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {
        void setupChart();
        void setupDashboardAdapter();
        void populateChart();
        void showLoadingIndicator(final boolean active);
        void showLoadingError();
        void searchResultReceived(List<SearchEntry> result);
    }

    interface Presenter extends BasePresenter {
        void loadPortfolio();
        void populateGraph();
        void sortCompaniesBy(String name);
        void loadSearchResult(String query);
        void setTickerFromSearchSuggestion(String tickerFromSearchSuggestion);
        List<Company> getCompanies();
    }
}
