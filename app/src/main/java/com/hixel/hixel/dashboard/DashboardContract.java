package com.hixel.hixel.dashboard;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

import java.util.List;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {
        void portfolioChanged();
        // void showMainGraph(ArrayList<Company> companies);
    }

    interface Presenter extends BasePresenter {
        void populateGraph();
        List<Company> getCompanies();
        void sortCompaniesBy(String name);
        List<String> getNames();
        void loadSearchSuggestion(String query);

    }
}
