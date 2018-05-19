package com.hixel.hixel.dashboard;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;

public interface DashboardContract {

    interface View extends BaseView<Presenter> {
        void portfolioChanged();
        // void showMainGraph(ArrayList<Company> companies);
    }

    interface Presenter extends BasePresenter {
        void populateGraph();
        ArrayList<Company> getCompanies();
        void sortCompaniesBy(String name);
        void loadSearchSuggestion(String query);
        ArrayList<String> getnames();
    }
}
