package com.hixel.hixel.comparison;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;

public interface ComparisonContract {
    interface View extends BaseView<Presenter>{
    void selectedListChanged();
    }
    interface Presenter extends BasePresenter{
        ArrayList<Company> getListCompareCompanies();
        void compare();
        void removeCompareFromList(int position);
        int addToCompare(String ticker);
        void removeLastItemFromList();

        ArrayList<String> getnames();
        void loadSearchSuggestion(String query);

    }
}
