package com.hixel.hixel.dashboard;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;

import com.hixel.hixel.models.Company;
import com.hixel.hixel.search.SearchEntry;

import io.reactivex.subjects.PublishSubject;
import java.util.List;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {
        void setupChart();
        void setupDashboardAdapter();
        void populateChart();
        void showLoadingIndicator(final boolean active);
        void showLoadingError();
        void showSuggestions(List<SearchEntry> searchEntries);
    }

    interface Presenter extends BasePresenter {
        void loadPortfolio();
        void sortCompaniesBy(String name);
        void search(PublishSubject<String> subject);
        List<Company> getCompanies();

    }
    interface Adapter extends BasePresenter
    {

    }

}
