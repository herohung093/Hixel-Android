package com.hixel.hixel.company;

import com.hixel.hixel.BasePresenter;
import com.hixel.hixel.BaseView;
import com.hixel.hixel.models.Company;

public interface CompanyContract {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        String getCompanyName();
        void setCompany(Company company);
        String getHealth();
        String getLeverage();
        String getLiquidity();

        int setLeverageColor();
        int setLiquidityColor();
        int setHealthColor();

    }
}
