package com.hixel.hixel.comparisonGraph;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

import java.util.ArrayList;

public interface GraphContract  {
    public interface View extends BaseView<Presenter>{
        void graphChanged(String ratio);
    }
    interface Presenter extends BasePresenter{
    ArrayList<Company> getCompanies();

    }
}
