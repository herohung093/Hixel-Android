package com.hixel.hixel.company;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.data.Company;

public interface CompanyContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        String getCompanyName();
        void setCompany(Company company);
        String getHealth();
        String getLeverage();
        String getLiquidity();

    }
}
