package com.hixel.hixel.comparison;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

import java.util.List;

public interface ComparisonContract {

    interface View extends BaseView<Presenter>{
        void selectedListChanged();

    }

    interface Presenter extends BasePresenter{
        List<Company> getListCompareCompanies();
        void compare();
        void removeCompareFromList(int position);
        int addToCompare(String ticker);
        void removeLastItemFromList();

        List<String> getNames();
        void loadSearchSuggestion(String query);

    }
}
