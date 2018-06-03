package com.hixel.hixel.comparisonGraph;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;
import java.util.ArrayList;

public interface GraphContract  {

    interface View extends BaseView<Presenter>{
    }

    interface Presenter extends BasePresenter{
        ArrayList<Company> getCompanies();
        void checkUpFinancialEntry(ArrayList<String> toBeCheckRatios);
        void doMeta();
        void setRatios(ArrayList<String> ratios);
    }
}
