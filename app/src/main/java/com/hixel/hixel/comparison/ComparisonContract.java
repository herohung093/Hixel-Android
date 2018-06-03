package com.hixel.hixel.comparison;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;
import com.hixel.hixel.search.SearchEntry;
import java.util.List;

public interface ComparisonContract {

    interface View extends BaseView<Presenter>{
        void selectedListChanged();
        void searchResultReceived(List<SearchEntry> result);
        void userNotification(String message);
    }

    interface Presenter extends BasePresenter{
        List<Company> getListCompareCompanies();
        void addToCompare(String ticker);
        void loadSearchResult(String query);
    }
}
